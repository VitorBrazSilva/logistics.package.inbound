package mercadolivre.processoseletivo.Inbound.client;


import mercadolivre.processoseletivo.Inbound.client.dto.dogApi.DogFactResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "dogApiClient", url = "${client.dogApi.url}")
public interface DogApiClient {

    @GetMapping("/facts")
    DogFactResponseDto getRandomDogFact(@RequestParam("limit") int limit);
}