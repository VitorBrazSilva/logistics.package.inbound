package mercadolivre.processoseletivo.Inbound.client;

import mercadolivre.processoseletivo.Inbound.client.dto.feriadoApi.FeriadoClientDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "feriadoClient", url = "${client.feriado.url}")
public interface FeriadoClient {

    @GetMapping("/{ano}/BR")
    List<FeriadoClientDto> getFeriado(@PathVariable("ano") Integer ano);

}
