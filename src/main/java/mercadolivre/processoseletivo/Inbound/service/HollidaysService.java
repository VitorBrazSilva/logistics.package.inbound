package mercadolivre.processoseletivo.Inbound.service;

import lombok.RequiredArgsConstructor;
import mercadolivre.processoseletivo.Inbound.client.HolidayClient;
import mercadolivre.processoseletivo.Inbound.client.dto.holidayDto.HolidayResponseDto;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import java.util.List;

@EnableCaching
@Service
@RequiredArgsConstructor

public class HollidaysService {

    private final HolidayClient holidayClient;

    @Cacheable(value = "holidays", key = "#year + '_' + #country")
    public List<HolidayResponseDto> getHolidays(int year, String country) {

        System.out.println("Consultando API externa para feriados de " + year + ", país: " + country);

        return holidayClient.getPublicHolidays(year, country);

    }
}
