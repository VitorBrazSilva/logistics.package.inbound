package mercadolivre.processoseletivo.Inbound.controller.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class ShippingPackageResponseDto {
    private UUID id;
    private String description;
    private String sender;
    private String recipient;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
