package mercadolivre.processoseletivo.Inbound.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mercadolivre.processoseletivo.Inbound.controller.dto.TrackingEventRequestDto;
import mercadolivre.processoseletivo.Inbound.entity.ShippingPackage;
import mercadolivre.processoseletivo.Inbound.entity.TrackingEvent;
import mercadolivre.processoseletivo.Inbound.repository.ShippingPackageRepository;
import mercadolivre.processoseletivo.Inbound.repository.TrackingEvenRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TrackingEventService {

    private final ShippingPackageRepository shippingPackageRepository;
    private final TrackingEvenRepository trackingEvenRepository;

    @Transactional
    public void createEvent(TrackingEventRequestDto trackingEventRequestDto) {

        ShippingPackage shippingPackage = shippingPackageRepository.findById(trackingEventRequestDto.getId())
                .orElseThrow(() -> new RuntimeException("Package not found"));

        TrackingEvent trackingEvent = new TrackingEvent();
        trackingEvent.setShippingPackage(shippingPackage);
        trackingEvent.setLocation(trackingEventRequestDto.getLocation());
        trackingEvent.setDescription(trackingEventRequestDto.getDescription());
        trackingEvent.setDate(LocalDateTime.now());
        trackingEvenRepository.save(trackingEvent);

    }
}
