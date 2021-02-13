package upgrade.volcano.domain;

import upgrade.volcano.adapter.postgres.cache.BookingCache;
import upgrade.volcano.domain.model.Booking;
import upgrade.volcano.domain.model.ConstraintsConfig;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


public class BookingManagerImpl implements BookingManager {

    private final BookingRepository bookingRepository;
    private final BookingValidator bookingValidator;
    private final BookingCache bookingCache;
    private final ConstraintsConfig config;

    public BookingManagerImpl(final ConstraintsConfig config,
                              final BookingRepository bookingRepository,
                              final BookingValidator bookingValidator,
                              final BookingCache bookingCache) {
        this.config = config;
        this.bookingRepository = bookingRepository;
        this.bookingValidator = bookingValidator;
        this.bookingCache = bookingCache;
        initCache();
    }

    private void initCache() {
        bookingCache.invalidateAll();
        LocalDate startPeriod = LocalDate.now().minusDays(config.getMaxDuration());
        LocalDate endPeriod = LocalDate.now().plusDays(config.getMaxDaysInAdvance());
        // get the bookings
        var bookings =
                bookingRepository.findActiveBookings(startPeriod, endPeriod);
        bookings.stream().forEach(booking -> bookingCache.put(booking));
    }


    @Override
    public UUID book(final Booking request) {
        bookingValidator.validate(request);

        // new request
        if (Objects.isNull(request.getId())) {
            request.setId(UUID.randomUUID());
        }

        bookingRepository.book(request);
        bookingCache.put(request);
        return request.getId();
    }

    @Override
    public void cancel(final UUID bookingId) {
        bookingRepository.cancel(bookingId);
        bookingCache.invalidate(bookingId);
    }

    @Override
    public Set<LocalDate> findAvailableDates(LocalDate startDate, LocalDate endDate) {
        if (Objects.isNull(startDate) || Objects.isNull(endDate)) {
            startDate = LocalDate.now();
            endDate = startDate.plusDays(config.getMaxDaysInAdvance());
        }

        bookingValidator.validateDateOrder(startDate, endDate);

        // cache should have list of all days
        // check cache
        final Set<LocalDate> booked = bookingCache.bookedDates();
        return startDate.datesUntil(endDate).filter(d -> !booked.contains(d)).collect(Collectors.toSet());
    }
}
