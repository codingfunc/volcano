package upgrade.volcano.adapter.rest.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BookingErrorWS {

    @JsonProperty("status")
    private Integer status;

    @JsonProperty("code")
    private String code = null;

    @JsonProperty("message")
    private String message = null;
}
