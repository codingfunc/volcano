package upgrade.volcano.domain;

import upgrade.volcano.domain.model.Booking;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface BookingRepository {

    /**
     * Add or update a new booking
     * @param booking booking details
     * @return a unique id
     */
    UUID book(Booking booking);

    /**
     * Cancel booking
     * @param bookingId booking id to cancel
     * @param email email of the user.
     */
    void cancel(UUID bookingId, String email);


    /**
     * Return all active bookings between the given period
     * @param startingDate start date
     * @param endDate end data
     * @return a list of all active bookings
     */
    Set<Booking> getActiveBookings(LocalDate startingDate, LocalDate endDate);

//    /**
//     * Retrieve all active bookings registed to given email.
//     * @param email email address
//     * @return set of all bookings registered to given email
//     */
//    Set<Booking> findAllByEmail(final String email);

}
