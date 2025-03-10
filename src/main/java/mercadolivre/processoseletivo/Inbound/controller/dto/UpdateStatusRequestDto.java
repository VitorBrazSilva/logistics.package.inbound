package mercadolivre.processoseletivo.Inbound.controller.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import mercadolivre.processoseletivo.Inbound.enums.ShippingPackageStatus;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UpdateStatusRequestDto {
    @NotNull(message = "Status is required")
    private ShippingPackageStatus status;
}
