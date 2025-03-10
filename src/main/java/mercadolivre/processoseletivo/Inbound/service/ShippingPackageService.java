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
import mercadolivre.processoseletivo.Inbound.controller.dto.TrackingEventPackageResponse;
import mercadolivre.processoseletivo.Inbound.entity.ShippingPackage;
import mercadolivre.processoseletivo.Inbound.enums.ShippingPackageStatus;
import mercadolivre.processoseletivo.Inbound.mapper.ShippingPackageMapper;
import mercadolivre.processoseletivo.Inbound.repository.ShippingPackageRepository;
import mercadolivre.processoseletivo.Inbound.repository.TrackingEvenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShippingPackageService {

    private final ShippingPackageRepository shippingPackageRepository;
    private final DogApiClient dogApiClient;
    private final ShippingPackageMapper shippingPackageMapper;
    private final TrackingEvenRepository trackingEvenRepository;
    private final RabbitMQMessagePublisher rabbitMQMessagePublisher;
    private final HollidaysService hollidaysService;

    @Transactional
    public ShippingPackageResponseDto createShippingPackageService(ShippingPackageRequestDto shippingPackageDto) {

        ShippingPackage shippingPackage = shippingPackageMapper.toEntity(shippingPackageDto);

        shippingPackage.setStatus(ShippingPackageStatus.CREATED);

        shippingPackageRepository.save(shippingPackage);
        rabbitMQMessagePublisher.sendPackageCreated(shippingPackage, RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY_HOLIDAY);

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
    public ShippingPackageEventsResponseDto getShippingPackage(UUID id, boolean includeEvents) {
        ShippingPackage shippingPackage = shippingPackageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Package not found"));

        List<TrackingEventPackageResponse> events = includeEvents
                ? trackingEvenRepository.findByShippingPackage(shippingPackage).stream()
                .map(TrackingEventPackageResponse::new)
                .toList()
                : Collections.emptyList();

        return new ShippingPackageEventsResponseDto(shippingPackage, events);
    }

    @Transactional(readOnly = true)
    public List<ShippingPackageResponseDto> listPackage(String sender, String recipient) {
        List<ShippingPackage> shippingPackage = shippingPackageRepository.filterShippingPackage(sender, recipient);
        return shippingPackage.stream().map(ShippingPackageResponseDto::new).toList();
    }

    @Transactional()
    public void updateHolliday(ShippingPackage shippingPackage) {

        shippingPackage.setHoliday(getIsHollidayInBR(shippingPackage.getEstimatedDeliveryDate()));
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

    private boolean isStatusUpdatable(ShippingPackageStatus currentStatus
            , ShippingPackageStatus newStatus) {
        return (currentStatus == ShippingPackageStatus.CREATED && newStatus == ShippingPackageStatus.IN_TRANSIT) ||
                (currentStatus == ShippingPackageStatus.IN_TRANSIT && newStatus == ShippingPackageStatus.DELIVERED);
    }
}
