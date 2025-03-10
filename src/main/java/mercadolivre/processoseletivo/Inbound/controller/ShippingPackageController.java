package mercadolivre.processoseletivo.Inbound.controller;

import lombok.RequiredArgsConstructor;
import mercadolivre.processoseletivo.Inbound.controller.dto.ShippingPackageEventsResponseDto;
import mercadolivre.processoseletivo.Inbound.controller.dto.ShippingPackageRequestDto;
import mercadolivre.processoseletivo.Inbound.controller.dto.ShippingPackageResponseDto;
import mercadolivre.processoseletivo.Inbound.controller.dto.UpdateStatusRequestDto;
import mercadolivre.processoseletivo.Inbound.service.ShippingPackageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;


import java.util.List;
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

    @GetMapping("/{id}")
    public ResponseEntity<ShippingPackageEventsResponseDto> getShippingPackage(
            @PathVariable UUID id,
            @RequestParam(defaultValue = "true") boolean includeEvents) {

        ShippingPackageEventsResponseDto response = shippingPackageService.getShippingPackage(id, includeEvents);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ShippingPackageResponseDto>> listPackage(
            @RequestParam(required = false) String sender,
            @RequestParam(required = false) String recipient) {

        List<ShippingPackageResponseDto> packageResponse = shippingPackageService.listPackage(sender, recipient);
        return ResponseEntity.ok(packageResponse);
    }
}
