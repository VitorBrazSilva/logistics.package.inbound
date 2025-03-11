package mercadolivre.processoseletivo.Inbound.client.dto.dogApi;

import lombok.Data;
import java.util.List;

@Data
public class DogFactResponseDto {
    private List<DogFactData> data;

    public String getFirstFact() {
        return (data != null && !data.isEmpty()) ? data.getFirst().getAttributes().getBody() : "No fact available";
    }
}