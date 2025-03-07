package mercadolivre.processoseletivo.Inbound.service;

import lombok.RequiredArgsConstructor;
import mercadolivre.processoseletivo.Inbound.client.DogApiClient;
import mercadolivre.processoseletivo.Inbound.client.HolidayClient;
import mercadolivre.processoseletivo.Inbound.client.dto.dogApi.DogFactResponseDto;
import mercadolivre.processoseletivo.Inbound.client.dto.holidayDto.HolidayResponseDto;
import mercadolivre.processoseletivo.Inbound.entity.Pacote;
import mercadolivre.processoseletivo.Inbound.enums.PacoteStatus;
import mercadolivre.processoseletivo.Inbound.repository.PacoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PacoteService {

    private final PacoteRepository pacoteRepository;
    private final HolidayClient holidayClient;
    private final DogApiClient dogApiClient;

    @Transactional
    public Pacote criarPacote(Pacote pacote) {

        pacote.setHoliday(getIsHollidayInBR(pacote.getEstimatedDeliveryDate()));

        pacote.setFunFact(getDogFactBody());

        pacote.setStatus(PacoteStatus.CREATED);
        pacote.setCreatedAt(LocalDateTime.now());
        pacote.setUpdatedAt(LocalDateTime.now());

        return pacoteRepository.save(pacote);
    }

    private String getDogFactBody() {

        DogFactResponseDto response = dogApiClient.getRandomDogFact(1);
        return response.getFirstFact();

    }

    private Boolean getIsHollidayInBR(LocalDate estimatedDeliveryDate){

        int year = estimatedDeliveryDate.getYear();
        List<HolidayResponseDto> holidays = holidayClient.getPublicHolidays(
                year, "BR");

       return holidays.stream()
                .anyMatch(h -> h.getDate().equals(estimatedDeliveryDate));

    }
}
