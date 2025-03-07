package mercadolivre.processoseletivo.Inbound.controller;

import lombok.RequiredArgsConstructor;
import mercadolivre.processoseletivo.Inbound.controller.dto.ShippingPackageRequestDto;
import mercadolivre.processoseletivo.Inbound.controller.dto.ShippingPackageResponseDto;
import mercadolivre.processoseletivo.Inbound.controller.dto.UpdateStatusRequestDto;
import mercadolivre.processoseletivo.Inbound.service.ShippingPackageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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

    @PatchMapping("/{id}/status")
    public ResponseEntity<ShippingPackageResponseDto> updateStatus(@PathVariable UUID id, @RequestBody UpdateStatusRequestDto newStatus) {
        ShippingPackageResponseDto packageStatusUpdated = shippingPackageService.updateStatus(id, newStatus.getStatus());
        return ResponseEntity.ok(packageStatusUpdated);
    }
}
