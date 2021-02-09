package upgrade.volcano.domain;

import upgrade.volcano.domain.model.Booking;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface BookingRepository {
    UUID book(Booking booking);

    void cancel(UUID bookingId, String email);

    List<Booking> availableDates(LocalDate startingDate, LocalDate endDate);

}
