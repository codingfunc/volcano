package upgrade.volcano.domain;

import upgrade.volcano.domain.model.Booking;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface BookingCache {
    void cache(Booking booking);
    void invalidateAll();
    void invalidate(UUID bookingId);
    Optional<Booking> get(UUID bookingId);
    Set<LocalDate> findBookedDates();
    Set<Booking> findBookings();
}
