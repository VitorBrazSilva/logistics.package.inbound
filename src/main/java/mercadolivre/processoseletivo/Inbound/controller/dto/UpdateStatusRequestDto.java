package mercadolivre.processoseletivo.Inbound.controller.dto;

import mercadolivre.processoseletivo.Inbound.enums.ShippingPackageStatus;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UpdateStatusRequestDto {
    private ShippingPackageStatus status;
}
