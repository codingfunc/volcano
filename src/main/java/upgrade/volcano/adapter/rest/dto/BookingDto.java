package upgrade.volcano.adapter.rest.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class BookingDto {
    private UUID bookingId;
    private String name;
    private String email;
    private LocalDate startDate;
    private LocalDate endDate;


}
