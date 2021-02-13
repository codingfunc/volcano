package upgrade.volcano.adapter.rest;

import org.apache.commons.lang3.StringUtils;
import upgrade.volcano.adapter.rest.dto.BookingDto;
import upgrade.volcano.domain.exception.BookingException;
import upgrade.volcano.domain.model.BookingRequest;

import java.util.UUID;

public class InputMapper {

    public BookingRequest map(BookingDto dto) {
        if(StringUtils.isBlank(dto.getName())
                || StringUtils.isBlank(dto.getEmail())){
            throw new BookingException(BookingException.ErrorType.INVALID_INPUT, "name and/or email can't be empty");
        }

        return BookingRequest.builder().forId(dto.getBookingId())
                .forClient(dto.getName())
                .forEmail(dto.getEmail())
                .startingAt(dto.getStartDate())
                .endingAt(dto.getEndDate())
                .build();
    }

    public UUID mapBookingId(final String id){
        try {
            return UUID.fromString(id);
        } catch(Exception e){
            throw new BookingException(BookingException.ErrorType.INVALID_INPUT, "invalid booking id provided");
        }
    }
}
