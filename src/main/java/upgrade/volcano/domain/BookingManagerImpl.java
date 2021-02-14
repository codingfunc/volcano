package upgrade.volcano.domain;

import upgrade.volcano.domain.exception.BookingException;
import upgrade.volcano.domain.model.Booking;
import upgrade.volcano.domain.model.ConstraintsConfig;

import java.time.LocalDate;
import java.util.List;
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
        bookings.stream().forEach(bookingCache::cache);
    }


    @Override
    public UUID book(final Booking request) {
        bookingValidator.validate(request);

        // new request
        if (Objects.isNull(request.getId())) {
            request.setId(UUID.randomUUID());
        }

        bookingRepository.book(request);
        bookingCache.cache(request);
        return request.getId();
    }

    @Override
    public void cancel(final UUID bookingId) {
        bookingRepository.cancel(bookingId);
        bookingCache.invalidate(bookingId);
    }

    @Override
    public List<LocalDate> findAvailableDates(LocalDate startDate, LocalDate endDate) {
        if (Objects.isNull(startDate) || Objects.isNull(endDate)) {
            startDate = LocalDate.now();
            endDate = startDate.plusDays(config.getMaxDaysInAdvance());
        }

        // check for current
        bookingValidator.validateDateOrder(startDate, endDate);

        if(startDate.isBefore(LocalDate.now())){
            throw new BookingException(BookingException.ErrorType.INVALID_INPUT, "start date is in the past");
        }

        // cache should have list of all days
        // check cache
        final Set<LocalDate> booked = bookingCache.findBookedDates();
        return startDate.datesUntil(endDate).filter(d -> !booked.contains(d)).sorted().collect(Collectors.toList());
    }
}
