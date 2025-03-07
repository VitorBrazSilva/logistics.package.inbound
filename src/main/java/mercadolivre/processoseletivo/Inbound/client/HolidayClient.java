package mercadolivre.processoseletivo.Inbound.client;

import mercadolivre.processoseletivo.Inbound.client.dto.holidayDto.HolidayResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "feriadoClient", url = "${client.feriado.url}")
public interface HolidayClient {

    @GetMapping("/{year}/{countryCode}")
    List<HolidayResponseDto> getPublicHolidays(@PathVariable("year") Integer ano, @PathVariable("countryCode") String countryCode);

}
