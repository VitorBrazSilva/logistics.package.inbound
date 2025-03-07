package mercadolivre.processoseletivo.Inbound.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import mercadolivre.processoseletivo.Inbound.client.DogApiClient;
import mercadolivre.processoseletivo.Inbound.client.HolidayClient;
import mercadolivre.processoseletivo.Inbound.client.dto.dogApi.DogFactResponseDto;
import mercadolivre.processoseletivo.Inbound.client.dto.holidayDto.HolidayResponseDto;
import mercadolivre.processoseletivo.Inbound.controller.dto.ShippingPackageRequestDto;
import mercadolivre.processoseletivo.Inbound.controller.dto.ShippingPackageResponseDto;
import mercadolivre.processoseletivo.Inbound.entity.ShippingPackage;
import mercadolivre.processoseletivo.Inbound.enums.ShippingPackageStatus;
import mercadolivre.processoseletivo.Inbound.mapper.ShippingPackageMapper;
import mercadolivre.processoseletivo.Inbound.repository.ShippingPackageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShippingPackageService {

    private final ShippingPackageRepository shippingPackageRepository;
    private final HolidayClient holidayClient;
    private final DogApiClient dogApiClient;
    private final ShippingPackageMapper shippingPackageMapper;

    @Transactional
    public ShippingPackageResponseDto createShippingPackageService(ShippingPackageRequestDto shippingPackageDto) {

        ShippingPackage shippingPackage = shippingPackageMapper.toEntity(shippingPackageDto);

        shippingPackage.setHoliday(getIsHollidayInBR(shippingPackage.getEstimatedDeliveryDate()));

        shippingPackage.setFunFact(getDogFactBody());

        shippingPackage.setStatus(ShippingPackageStatus.CREATED);
        shippingPackage.setCreatedAt(LocalDateTime.now());
        shippingPackage.setUpdatedAt(LocalDateTime.now());

        shippingPackageRepository.save(shippingPackage);
        return shippingPackageMapper.toDTO(shippingPackage);
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
        return shippingPackageMapper.toDTO(shippingPackage);
    }


    private String getDogFactBody() {

        DogFactResponseDto response = dogApiClient.getRandomDogFact(1);
        return response.getFirstFact();

    }

    private Boolean getIsHollidayInBR(LocalDate estimatedDeliveryDate){

        int year = estimatedDeliveryDate.getYear();
        List<HolidayResponseDto> holidays = holidayClient.getPublicHolidays(
                year, "BR");

       return holidays.stream()
                .anyMatch(h -> h.getDate().equals(estimatedDeliveryDate));

    }

    private boolean isStatusUpdatable(ShippingPackageStatus currentStatus
            , ShippingPackageStatus newStatus) {
        return (currentStatus == ShippingPackageStatus.CREATED && newStatus == ShippingPackageStatus.IN_TRANSIT) ||
                (currentStatus == ShippingPackageStatus.IN_TRANSIT && newStatus == ShippingPackageStatus.DELIVERED);
    }
}
