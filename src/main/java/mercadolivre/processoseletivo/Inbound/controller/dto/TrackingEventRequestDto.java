package mercadolivre.processoseletivo.Inbound.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class TrackingEventRequestDto {
    @NotNull(message = "Status is required")
    private UUID id;
    @NotBlank(message = "Location is required")
    private String location;
    @NotBlank(message = "Description is required")
    private String description;
}
