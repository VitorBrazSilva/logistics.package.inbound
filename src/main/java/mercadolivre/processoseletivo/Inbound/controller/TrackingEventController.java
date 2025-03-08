package mercadolivre.processoseletivo.Inbound.controller;


import lombok.RequiredArgsConstructor;
import mercadolivre.processoseletivo.Inbound.controller.dto.TrackingEventRequestDto;
import mercadolivre.processoseletivo.Inbound.service.TrackingEventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trackingevent")
@RequiredArgsConstructor
public class TrackingEventController {

    private final TrackingEventService trackingEventService;

    @PostMapping
    public ResponseEntity<Void> createEvent(@RequestBody TrackingEventRequestDto trackingEventRequestDto) {

        try {
            trackingEventService.createEvent(trackingEventRequestDto);

        } catch (Exception e) {
            System.err.println("Erro inesperado: " + e.getMessage());
            throw new RuntimeException("Erro ao salvar o evento.", e);
        }

        return null;
    }

}
