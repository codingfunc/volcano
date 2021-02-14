package upgrade.volcano.adapter.rest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import upgrade.volcano.adapter.rest.dto.BookingRequest;
import upgrade.volcano.domain.exception.BookingException;
import upgrade.volcano.domain.model.Booking;

import java.time.LocalDate;
import java.util.UUID;

public class InputMapper {

    public Booking map(BookingRequest dto) {
        if (StringUtils.isBlank(dto.getName())
                || StringUtils.isBlank(dto.getEmail())) {
            throw new BookingException(BookingException.ErrorType.INVALID_INPUT, "name and/or email can't be empty");
        }

        return Booking.builder().forId(dto.getBookingId())
                .forClient(dto.getName())
                .forEmail(validateEmail(dto.getEmail()))
                .arrivingAt(mapDate(dto.getArrivalDate()))
                .departingAt(mapDate(dto.getDepartureDate()))
                .build();
    }

    private String validateEmail(final String email){
        EmailValidator validator = EmailValidator.getInstance();
        if(!validator.isValid(email)){
            throw new BookingException(BookingException.ErrorType.INVALID_INPUT, "invalid email address");
        }
        return email;
    }
    public UUID mapBookingId(final String id) {
        try {
            return UUID.fromString(id);
        } catch (Exception e) {
            throw new BookingException(BookingException.ErrorType.INVALID_INPUT, "invalid booking id provided");
        }
    }

    public LocalDate mapDate(final String date){
        try {
            return LocalDate.parse(date);
        } catch(Exception e){
            throw new BookingException(BookingException.ErrorType.INVALID_INPUT, "invalid date");
        }
    }
}
