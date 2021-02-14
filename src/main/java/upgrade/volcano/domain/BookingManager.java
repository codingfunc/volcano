package upgrade.volcano.domain;

import upgrade.volcano.domain.model.Booking;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface BookingManager {
    /**
     * create or update an existing booking
     *
     * @param booking booking details
     * @return booking id
     */
    UUID book(Booking booking);

    /**
     * cancel a booking
     *
     * @param bookingId booking id
     */
    void cancel(UUID bookingId);

    /**
     * Return list of available dates for booking
     *
     * @param startDate start date. if null then current date is used
     * @param endDate   end date. If null then current date + 30 days is used
     * @return returns a list of days for booking.
     */
    List<LocalDate> findAvailableDates(LocalDate startDate, LocalDate endDate);
}
