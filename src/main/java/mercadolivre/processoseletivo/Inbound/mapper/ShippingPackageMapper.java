package mercadolivre.processoseletivo.Inbound.mapper;

import mercadolivre.processoseletivo.Inbound.controller.dto.ShippingPackageRequestDto;
import mercadolivre.processoseletivo.Inbound.controller.dto.ShippingPackageResponseDto;
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

    public ShippingPackageResponseDto toDTO(ShippingPackage shippingPackage) {
        ShippingPackageResponseDto dto = new ShippingPackageResponseDto();
        dto.setId(shippingPackage.getId());
        dto.setDescription(shippingPackage.getDescription());
        dto.setSender(shippingPackage.getSender());
        dto.setRecipient(shippingPackage.getRecipient());
        dto.setStatus(shippingPackage.getStatus().name());
        dto.setCreatedAt(shippingPackage.getCreatedAt());
        dto.setUpdatedAt(shippingPackage.getUpdatedAt());
        return dto;
    }
}
