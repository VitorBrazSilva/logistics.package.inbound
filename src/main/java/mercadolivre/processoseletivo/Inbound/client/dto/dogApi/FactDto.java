package mercadolivre.processoseletivo.Inbound.client.dto.dogApi;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FactDto {
    private String id;
    private String type;
    private AttributesDto attributes;

}
