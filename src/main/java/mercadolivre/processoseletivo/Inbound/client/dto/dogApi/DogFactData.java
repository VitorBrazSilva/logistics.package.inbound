package mercadolivre.processoseletivo.Inbound.client.dto.dogApi;

import lombok.Data;

@Data
public class DogFactData {
    private String id;
    private String type;
    private DogFactAttributes attributes;
}
