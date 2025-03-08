package mercadolivre.processoseletivo.Inbound.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class TrackingEventRequestDto {
    private UUID id;
    private String location;
    private String description;
}
