package mercadolivre.processoseletivo.Inbound.controller;

import lombok.RequiredArgsConstructor;
import mercadolivre.processoseletivo.Inbound.entity.Pacote;
import mercadolivre.processoseletivo.Inbound.service.PacoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pacotes")
@RequiredArgsConstructor
public class pacoteController {

    private final PacoteService pacoteService;

    @PostMapping
    public ResponseEntity<Pacote> criarPacote(@RequestBody Pacote pacote) {
        Pacote novoPacote = pacoteService.criarPacote(pacote);
        return ResponseEntity.ok(novoPacote);
    }

}
