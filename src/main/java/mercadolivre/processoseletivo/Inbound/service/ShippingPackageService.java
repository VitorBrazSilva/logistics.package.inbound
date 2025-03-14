package mercadolivre.processoseletivo.Inbound.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mercadolivre.processoseletivo.Inbound.client.DogApiClient;
import mercadolivre.processoseletivo.Inbound.client.dto.dogApi.DogFactResponseDto;
import mercadolivre.processoseletivo.Inbound.client.dto.holidayDto.HolidayResponseDto;
import mercadolivre.processoseletivo.Inbound.config.RabbitMQConfig;
import mercadolivre.processoseletivo.Inbound.controller.dto.ShippingPackageEventsResponseDto;
import mercadolivre.processoseletivo.Inbound.controller.dto.ShippingPackageRequestDto;
import mercadolivre.processoseletivo.Inbound.controller.dto.ShippingPackageResponseDto;
import mercadolivre.processoseletivo.Inbound.entity.ShippingPackage;
import mercadolivre.processoseletivo.Inbound.entity.TrackingEvent;
import mercadolivre.processoseletivo.Inbound.enums.ShippingPackageStatus;
import mercadolivre.processoseletivo.Inbound.mapper.ShippingPackageMapper;
import mercadolivre.processoseletivo.Inbound.repository.ShippingPackageRepository;
import mercadolivre.processoseletivo.Inbound.repository.TrackingEventRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShippingPackageService {

    private final ShippingPackageRepository shippingPackageRepository;
    private final DogApiClient dogApiClient;
    private final ShippingPackageMapper shippingPackageMapper;
    private final TrackingEventRepository trackingEventRepository;
    private final RabbitMQMessagePublisher rabbitMQMessagePublisher;
    private final HollidaysService hollidaysService;


    @Transactional
    public ShippingPackageResponseDto createShippingPackageService(ShippingPackageRequestDto shippingPackageDto) {
        if (shippingPackageDto == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }

        ShippingPackage shippingPackage = shippingPackageMapper.toEntity(shippingPackageDto);
        if (shippingPackage == null) {
            throw new IllegalStateException("Failed to map request to entity");
        }

        shippingPackage.setStatus(ShippingPackageStatus.CREATED);
        shippingPackage.setCreatedAt(LocalDateTime.now());
        shippingPackage.setUpdatedAt(LocalDateTime.now());

        shippingPackageRepository.save(shippingPackage);
        sendPackageCreatedAsync(shippingPackage);

        return new ShippingPackageResponseDto(shippingPackage);
    }

    public ShippingPackageResponseDto updateStatus(UUID id, ShippingPackageStatus newStatus) {
        ShippingPackage shippingPackage = shippingPackageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Package not found"));

        if (!isStatusUpdatable(shippingPackage.getStatus(), newStatus)) {
            throw new IllegalStateException("Invalid status transition");
        }

        shippingPackage.setStatus(newStatus);
        shippingPackage.setUpdatedAt(LocalDateTime.now());

        if (newStatus == ShippingPackageStatus.DELIVERED) {
            shippingPackage.setDeliveredAt(LocalDateTime.now());
        }

        shippingPackageRepository.save(shippingPackage);
        return new ShippingPackageResponseDto(shippingPackage);
    }

    @Transactional(readOnly = true)
    public ShippingPackageEventsResponseDto getShippingPackage(UUID id, boolean includeEvents, int page, int size) {
        ShippingPackage shippingPackage = shippingPackageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Package not found"));

        Page<TrackingEvent> eventsPage = Page.empty();
        if (includeEvents) {
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            eventsPage = trackingEventRepository.findByShippingPackage(shippingPackage, pageable);
        }

        return new ShippingPackageEventsResponseDto(shippingPackage, eventsPage);
    }

    @Transactional(readOnly = true)
    public List<ShippingPackageResponseDto> listPackage(String sender, String recipient) {
        List<ShippingPackage> shippingPackage = shippingPackageRepository.filterShippingPackage(sender, recipient);
        return shippingPackage.stream().map(ShippingPackageResponseDto::new).toList();
    }

    @Transactional()
    public void updateHolliday(ShippingPackage shippingPackage) {

        shippingPackage.setIsHoliday(getIsHollidayInBR(shippingPackage.getEstimatedDeliveryDate()));
        shippingPackageRepository.save(shippingPackage);
        log.info("🎄 Holiday updated successfully: {}", shippingPackage.getId() );
        rabbitMQMessagePublisher.sendPackageCreated(shippingPackage, RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY_FUNFACT);
    }

    @Transactional()
    public void updateFunFact(ShippingPackage shippingPackage) {

        shippingPackage.setFunFact(getDogFactBody());
        shippingPackageRepository.save(shippingPackage);
        log.info("🐶 Fun fact updated successfully: {}", shippingPackage.getId());
    }


    private String getDogFactBody() {

        DogFactResponseDto response = dogApiClient.getRandomDogFact(1);
        return response.getFirstFact();

    }


    private Boolean getIsHollidayInBR(LocalDate estimatedDeliveryDate){

        int year = estimatedDeliveryDate.getYear();
        List<HolidayResponseDto> holidays = hollidaysService.getHolidays(year, "BR");
        return holidays.stream()
                .anyMatch(h -> h.getDate().equals(estimatedDeliveryDate));

    }

    private Boolean isStatusUpdatable(ShippingPackageStatus currentStatus
            , ShippingPackageStatus newStatus) {
        return (currentStatus == ShippingPackageStatus.CREATED && newStatus == ShippingPackageStatus.IN_TRANSIT) ||
                (currentStatus == ShippingPackageStatus.IN_TRANSIT && newStatus == ShippingPackageStatus.DELIVERED) ||
                (currentStatus == ShippingPackageStatus.CREATED && newStatus == ShippingPackageStatus.CANCELED);
    }

    @Async
    public void sendPackageCreatedAsync(ShippingPackage shippingPackage) {
        rabbitMQMessagePublisher.sendPackageCreated(shippingPackage, RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY_HOLIDAY);
    }
}
