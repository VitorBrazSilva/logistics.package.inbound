package mercadolivre.processoseletivo.Inbound.client.dto.holidayDto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter

public class HolidayResponseDto {
    private LocalDate date;
    private String localName;
}
