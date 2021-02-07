package upgrade.volcano.domain;

import upgrade.volcano.domain.model.Booking;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class BookingManagerImpl implements BookingManager {

    private final BookingRepository bookingRepository;
    private final BookingValidator bookingValidator;

    public BookingManagerImpl(final BookingRepository bookingRepository, final BookingValidator bookingValidator) {
        this.bookingRepository = bookingRepository;
        this.bookingValidator = bookingValidator;
    }


    @Override
    public UUID book(final Booking request) {
        bookingValidator.validate(request);
        final var booking = bookingRepository.book(request);
        return booking.getId();
    }

    @Override
    public void cancel(final UUID bookingId, final String email) {
        bookingRepository.cancel(bookingId, email);
    }

    @Override
    public List<LocalDate> availableDates(final LocalDate startDate, final LocalDate endDate) {
        // get list of available dates for the full month.

        return null;
    }

}
