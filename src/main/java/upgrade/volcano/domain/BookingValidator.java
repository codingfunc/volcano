package upgrade.volcano.domain;

import upgrade.volcano.domain.model.Booking;

import java.time.LocalDate;

public interface BookingValidator {

    void validate(Booking booking);
    void validateDateOrder(LocalDate startDate, LocalDate endDate);
}
