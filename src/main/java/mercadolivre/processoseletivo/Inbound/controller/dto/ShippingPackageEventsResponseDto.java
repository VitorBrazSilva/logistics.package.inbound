package mercadolivre.processoseletivo.Inbound.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import mercadolivre.processoseletivo.Inbound.entity.ShippingPackage;
import mercadolivre.processoseletivo.Inbound.entity.TrackingEvent;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class ShippingPackageEventsResponseDto {
    private UUID id;
    private String description;
    private String sender;
    private String recipient;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<TrackingEventPackageResponse> events;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;

    public ShippingPackageEventsResponseDto(ShippingPackage shippingPackage, Page<TrackingEvent> eventsPage) {
        this.id = shippingPackage.getId();
        this.description = shippingPackage.getDescription();
        this.sender = shippingPackage.getSender();
        this.recipient = shippingPackage.getRecipient();
        this.status = shippingPackage.getStatus().name();
        this.createdAt = shippingPackage.getCreatedAt();
        this.updatedAt = shippingPackage.getUpdatedAt();
        this.events = eventsPage.getContent().stream().map(TrackingEventPackageResponse::new).toList();
        this.page = eventsPage.getNumber();
        this.size = eventsPage.getSize();
        this.totalElements = eventsPage.getTotalElements();
        this.totalPages = eventsPage.getTotalPages();
    }
}
