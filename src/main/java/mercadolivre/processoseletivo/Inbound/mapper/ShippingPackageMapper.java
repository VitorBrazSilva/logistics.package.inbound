package mercadolivre.processoseletivo.Inbound.mapper;

import mercadolivre.processoseletivo.Inbound.controller.dto.ShippingPackageRequestDto;
import mercadolivre.processoseletivo.Inbound.entity.ShippingPackage;
import org.springframework.stereotype.Component;

@Component
public class ShippingPackageMapper {
    public ShippingPackage toEntity(ShippingPackageRequestDto dto){
        ShippingPackage shippingpackage = new ShippingPackage();
        shippingpackage.setDescription(dto.getDescription());
        shippingpackage.setSender(dto.getSender());
        shippingpackage.setRecipient(dto.getRecipient());
        shippingpackage.setEstimatedDeliveryDate(dto.getEstimatedDeliveryDate());
        return shippingpackage;
    }
}
