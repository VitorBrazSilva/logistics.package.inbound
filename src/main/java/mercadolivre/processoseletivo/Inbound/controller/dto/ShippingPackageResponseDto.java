package mercadolivre.processoseletivo.Inbound.controller.dto;

import lombok.Getter;
import lombok.Setter;
import mercadolivre.processoseletivo.Inbound.entity.ShippingPackage;

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

    public ShippingPackageResponseDto(ShippingPackage shippingPackage) {
        this.id = shippingPackage.getId();
        this.description = shippingPackage.getDescription();
        this.sender = shippingPackage.getSender();
        this.recipient = shippingPackage.getRecipient();
        this.status = shippingPackage.getStatus().name();
        this.createdAt = shippingPackage.getCreatedAt();
        this.updatedAt = shippingPackage.getUpdatedAt();
    }
}
