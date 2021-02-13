package upgrade.volcano.domain;

import upgrade.volcano.adapter.postgres.cache.BookingCache;
import upgrade.volcano.domain.model.Booking;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public class BookingManagerImpl implements BookingManager {

    private final BookingRepository bookingRepository;
    private final BookingValidator bookingValidator;
    private final BookingCache bookingCache;

    public BookingManagerImpl(final BookingRepository bookingRepository, final BookingValidator bookingValidator, final BookingCache bookingCache) {
        this.bookingRepository = bookingRepository;
        this.bookingValidator = bookingValidator;
        this.bookingCache = bookingCache;
        initCache();
    }

    private void initCache() {
        bookingCache.invalidateAll();
        var bookings = bookingRepository.getActiveBookings()
    }


    @Override
    public UUID book(final Booking request) {
        bookingValidator.validate(request);
        return bookingRepository.book(request);
    }

    @Override
    public void cancel(final UUID bookingId, final String email) {
        bookingRepository.cancel(bookingId, email);
        bookingCache.invalidate(bookingId);
    }

    public Set<Booking> getActiveBookings(final LocalDate startDate, final LocalDate endDate){
        return bookingRepository.getActiveBookings(startDate, endDate);
    }

    @Override
    public Set<LocalDate> availableDates(final LocalDate startDate, final LocalDate endDate) {
        bookingValidator.validateDateOrder(startDate, endDate);

        // get list of available dates for the full month.
        bookingCache.

        return null;
    }

//    @Override
//    public Set<Booking> findAllByEmail(final String email){
//        return bookingCache.findAllByEmail(email);
//    }



}
