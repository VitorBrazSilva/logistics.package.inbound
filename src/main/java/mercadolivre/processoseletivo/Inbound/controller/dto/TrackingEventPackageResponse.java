package mercadolivre.processoseletivo.Inbound.controller.dto;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
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
    private LocalDateTime createdAt;

    public TrackingEventPackageResponse(TrackingEvent event) {
        this.shipping_package_id = event.getShippingPackage().getId();
        this.location = event.getLocation();
        this.description = event.getDescription();
        this.createdAt = event.getCreatedAt();
    }
}
