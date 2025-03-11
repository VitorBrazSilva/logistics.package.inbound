package mercadolivre.processoseletivo.Inbound.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mercadolivre.processoseletivo.Inbound.controller.dto.TrackingEventRequestDto;
import mercadolivre.processoseletivo.Inbound.service.TrackingEventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trackingevent")
@RequiredArgsConstructor
public class TrackingEventController {

    private final TrackingEventService trackingEventService;

    @PostMapping
    public ResponseEntity<Void> createEvent(@RequestBody @Valid TrackingEventRequestDto trackingEventRequestDto) {
        trackingEventService.createEvent(trackingEventRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
