package mercadolivre.processoseletivo.Inbound.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import mercadolivre.processoseletivo.Inbound.entity.ShippingPackage;
import mercadolivre.processoseletivo.Inbound.enums.ShippingPackageStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShippingPackageResponseDto {
    private UUID id;
    private String description;
    private String sender;
    private String recipient;
    private ShippingPackageStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isHoliday;
    private String funFact;
    private LocalDateTime deliveredAt;
    private LocalDate estimatedDeliveryDate;

    public ShippingPackageResponseDto(ShippingPackage shippingPackage) {
        this.id = shippingPackage.getId();
        this.description = shippingPackage.getDescription();
        this.sender = shippingPackage.getSender();
        this.recipient = shippingPackage.getRecipient();
        this.status = shippingPackage.getStatus();
        this.createdAt = shippingPackage.getCreatedAt();
        this.updatedAt = shippingPackage.getUpdatedAt();
        this.isHoliday = shippingPackage.getIsHoliday();
        this.funFact = shippingPackage.getFunFact();
        this.deliveredAt = shippingPackage.getDeliveredAt();
        this.estimatedDeliveryDate = shippingPackage.getEstimatedDeliveryDate();
    }
}
