package mercadolivre.processoseletivo.Inbound.client.dto.feriadoApi;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter

public class FeriadoClientDto {
    public LocalDateTime date;
    public String localName;
    public String name;
    public String countryCode ;
    public Boolean fixed;
    public Boolean global;
    public String[] counties;
    public String[] types;
}
