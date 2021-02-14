package upgrade.volcano.adapter.rest.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@ApiModel(value = "Booking Request", description = "Booking Request")
public class BookingRequest {

    @ApiModelProperty(value = "Booking id if available", dataType = "String", required = false)
    private UUID bookingId;

    @ApiModelProperty(value = "Client name", dataType = "String", required = true)
    private String name;

    @ApiModelProperty(value = "Client email", dataType = "String", required = true)
    private String email;

    @ApiModelProperty(value = "Reservation start date", dataType = "String", required = true)
    private String arrivalDate;

    @ApiModelProperty(value = "Reservation end date", dataType = "String", required = true)
    private String departureDate;
}
