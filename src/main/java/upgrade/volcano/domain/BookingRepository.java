package upgrade.volcano.domain;

import upgrade.volcano.domain.model.Booking;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface BookingRepository {

    /**
     * Add or update a new booking
     *
     * @param booking booking details
     */
    void book(Booking booking);

    /**
     * Cancel booking
     *
     * @param bookingId booking id to cancel
     */
    void cancel(UUID bookingId);

    /**
     * find booking for the given bookingId
     *
     * @param bookingId booking identifier
     * @return
     */
    Optional<Booking> findByBookingId(UUID bookingId);


    /**
     * Return all active bookings between the given period
     *
     * @param startingDate start date
     * @param endDate      end data
     * @return a list of all active bookings
     */
    Set<Booking> findActiveBookings(LocalDate startingDate, LocalDate endDate);
}
