package mercadolivre.processoseletivo.Inbound.service;

import lombok.RequiredArgsConstructor;
import mercadolivre.processoseletivo.Inbound.controller.dto.TrackingEventRequestDto;
import mercadolivre.processoseletivo.Inbound.entity.ShippingPackage;
import mercadolivre.processoseletivo.Inbound.entity.TrackingEvent;
import mercadolivre.processoseletivo.Inbound.repository.ShippingPackageRepository;
import mercadolivre.processoseletivo.Inbound.repository.TrackingEvenRepository;
import org.hibernate.StaleObjectStateException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TrackingEventService {

    private final ShippingPackageRepository shippingPackageRepository;
    private final TrackingEvenRepository trackingEvenRepository;

    public void createEvent(TrackingEventRequestDto trackingEventRequestDto) {

        ShippingPackage shippingPackage = shippingPackageRepository.findById(trackingEventRequestDto.getId())
                .orElseThrow(() -> new RuntimeException("Package not found"));

        TrackingEvent trackingEvent = new TrackingEvent();
        trackingEvent.setId(UUID.randomUUID());
        trackingEvent.setShippingPackage(shippingPackage);
        trackingEvent.setLocation(trackingEventRequestDto.getLocation());
        trackingEvent.setDescription(trackingEventRequestDto.getDescription());
        trackingEvent.setDate(LocalDateTime.now());
        try {
            trackingEvenRepository.save(trackingEvent);
        } catch (StaleObjectStateException e) {
            throw new RuntimeException("Concurrency conflict detected", e);
        }

    }
}
