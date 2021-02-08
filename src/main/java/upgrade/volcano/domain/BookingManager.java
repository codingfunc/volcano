package upgrade.volcano.domain;

import upgrade.volcano.domain.model.Booking;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public interface BookingManager {
    /**
     * create or update an existing booking
     * @param booking booking details
     * @return booking id
     */
    UUID book(Booking booking);

    /**
     * cancel a booking
     * @param bookingId booking id
     * @param email client email
     *
     */
    void cancel(UUID bookingId, String email);

    /**
     * Return list of available dates for booking
     * @param startDate start date. if null then current date is used
     * @param endDate end date. If null then current date + 30 days is used
     * @return returns a list of days for booking.
     */
    Set<LocalDate> availableDates(LocalDate startDate, LocalDate endDate);
}
