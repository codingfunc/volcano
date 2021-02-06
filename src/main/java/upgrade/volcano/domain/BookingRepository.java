package upgrade.volcano.domain;

import upgrade.volcano.domain.model.Booking;

import java.util.UUID;

public interface BookingRepository {
    Booking book(Booking booking);

    void cancel(UUID bookingId, String email);

}
