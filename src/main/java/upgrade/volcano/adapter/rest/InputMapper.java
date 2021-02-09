package upgrade.volcano.adapter.rest;

import upgrade.volcano.adapter.rest.dto.BookingDto;
import upgrade.volcano.domain.model.Booking;

public class InputMapper {

    public Booking map(BookingDto dto) {
        return Booking.builder().forId(dto.getBookingId())
                .forClient(dto.getName())
                .forEmail(dto.getEmail())
                .startingAt(dto.getStartDate())
                .endingAt(dto.getEndDate())
                .build();
    }
}
