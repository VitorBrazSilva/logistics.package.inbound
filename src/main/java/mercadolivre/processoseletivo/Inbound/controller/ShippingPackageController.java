package mercadolivre.processoseletivo.Inbound.controller;

import lombok.RequiredArgsConstructor;
import mercadolivre.processoseletivo.Inbound.entity.ShippingPackage;
import mercadolivre.processoseletivo.Inbound.service.ShippingPackageService;
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
    public ResponseEntity<ShippingPackage> createShippingPackageService(@RequestBody ShippingPackage shippingPackage) {
        ShippingPackage newShippingPackageService = shippingPackageService.createShippingPackageService(shippingPackage);
        return ResponseEntity.ok(newShippingPackageService);
    }
}
