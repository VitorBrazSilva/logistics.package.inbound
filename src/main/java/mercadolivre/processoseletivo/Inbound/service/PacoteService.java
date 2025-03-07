package mercadolivre.processoseletivo.Inbound.service;

import lombok.RequiredArgsConstructor;
import mercadolivre.processoseletivo.Inbound.client.DogApiClient;
import mercadolivre.processoseletivo.Inbound.client.FeriadoClient;
import mercadolivre.processoseletivo.Inbound.client.dto.dogApi.DogApiClientDto;
import mercadolivre.processoseletivo.Inbound.client.dto.feriadoApi.FeriadoClientDto;
import mercadolivre.processoseletivo.Inbound.entity.Pacote;
import mercadolivre.processoseletivo.Inbound.enums.PacoteStatus;
import mercadolivre.processoseletivo.Inbound.repository.PacoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PacoteService {

    private final PacoteRepository pacoteRepository;
    private final FeriadoClient feriadoClient;
    private final DogApiClient dogApiClient;

    @Transactional
    public Pacote criarPacote(Pacote pacote) {

        LocalDateTime estimatedDeliveryDate = pacote.getEstimatedDeliveryDate();

        pacote.setHoliday(getIsHolliday(estimatedDeliveryDate));

        pacote.setFunFact(getDogFactBody());

        pacote.setStatus(PacoteStatus.CREATED);
        pacote.setCreatedAt(LocalDateTime.now());
        pacote.setUpdatedAt(LocalDateTime.now());

        return pacoteRepository.save(pacote);
    }

    private String getDogFactBody() {
        DogApiClientDto response = dogApiClient.getRandomDogFact(1);
        if (!response.getData().isEmpty()) {
            return response.getData().getFirst().getAttributes().getBody();
        }
        return "Nenhum fato encontrado.";
    }

    private Boolean getIsHolliday(LocalDateTime estimatedDeliveryDate){
        int ano = estimatedDeliveryDate.getYear();
        List<FeriadoClientDto> feriados = feriadoClient.getFeriado(ano);
        return feriados.stream()
                .anyMatch(obj -> obj.getDate().equals(estimatedDeliveryDate));
    }

}
