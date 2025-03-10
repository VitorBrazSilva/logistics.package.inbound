package mercadolivre.processoseletivo.Inbound.controller.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import mercadolivre.processoseletivo.Inbound.entity.TrackingEvent;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class TrackingEventPackageResponse {
    private UUID shipping_package_id;
    private String location;
    private String description;
    private LocalDateTime date;

    public TrackingEventPackageResponse(TrackingEvent event) {
        this.shipping_package_id = event.getShippingPackage().getId();
        this.location = event.getLocation();
        this.description = event.getDescription();
        this.date = event.getCreatedAt();
    }
}
