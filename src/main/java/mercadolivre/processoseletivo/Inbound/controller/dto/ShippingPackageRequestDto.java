package mercadolivre.processoseletivo.Inbound.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ShippingPackageRequestDto {
    private String description;
    private String sender;
    private String recipient;
    private LocalDate estimatedDeliveryDate;
}
