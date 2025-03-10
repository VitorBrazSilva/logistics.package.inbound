package mercadolivre.processoseletivo.Inbound.controller.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class  ShippingPackageRequestDto {
    @NotBlank(message = "Description is required")
    private  String description;
    @NotBlank(message = "Sender is required")
    private  String sender;
    @NotBlank(message = "Recipient is required")
    private String recipient;
    @Future(message = "Estimated date must be in the future")
    private LocalDate estimatedDeliveryDate;
}
