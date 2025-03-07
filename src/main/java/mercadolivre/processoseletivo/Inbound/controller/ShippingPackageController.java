package mercadolivre.processoseletivo.Inbound.controller;

import lombok.RequiredArgsConstructor;
import mercadolivre.processoseletivo.Inbound.controller.dto.ShippingPackageRequestDto;
import mercadolivre.processoseletivo.Inbound.controller.dto.ShippingPackageResponseDto;
import mercadolivre.processoseletivo.Inbound.service.ShippingPackageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/shippingpackage")
@RequiredArgsConstructor
public class ShippingPackageController {

    private final ShippingPackageService shippingPackageService;

    @PostMapping
    public ResponseEntity<ShippingPackageResponseDto> createShippingPackageService(@RequestBody ShippingPackageRequestDto shippingPackageDto) {
        ShippingPackageResponseDto response = shippingPackageService.createShippingPackageService(shippingPackageDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
