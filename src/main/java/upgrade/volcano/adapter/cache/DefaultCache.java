package upgrade.volcano.adapter.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import upgrade.volcano.domain.BookingCache;
import upgrade.volcano.domain.model.Booking;
import upgrade.volcano.domain.model.ConstraintsConfig;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * A fast synchronized cache to maintain all active bookings.
 * For distributed systems a distributed cache like Radis can be injected in-place
 */
public class DefaultCache implements BookingCache {


    private Cache<UUID, Booking> cache;
    private ConstraintsConfig config;

    public DefaultCache(final ConstraintsConfig config) {
        this.config = config;
        // cache duration is the number of days in advance a user can book
        // this also determines how many entries there can be.
        cache = Caffeine.newBuilder()
                .expireAfterWrite(Duration.of(config.getMaxDaysInAdvance(), ChronoUnit.DAYS))
                .maximumSize(config.getMaxDaysInAdvance())
                .build();
    }

    @Override
    public void cache(Booking booking) {
        cache.invalidate(booking.getId());
        cache.put(booking.getId(), booking);
    }

    public Optional<Booking> get(UUID bookingId) {
        return Optional.ofNullable(cache.getIfPresent(bookingId));
    }

    @Override
    public void invalidateAll() {
        cache.invalidateAll();
    }

    @Override
    public void invalidate(final UUID bookingId) {
        cache.invalidate(bookingId);
    }

    @Override
    public Set<LocalDate> findBookedDates() {

        final LocalDate start = LocalDate.now().minusDays(config.getMaxDuration());
        // how to handle expired ones
        // always check from current date
        return cache.asMap().values()
                .stream()
                .filter(booking -> start.isBefore(booking.getArrivalDate()))
                .flatMap(booking -> booking.getArrivalDate().datesUntil(booking.getDepartureDate().plusDays(1))).collect(Collectors.toSet());
    }

    @Override
    public Set<Booking> findBookings() {

        final LocalDate start = LocalDate.now().minusDays(config.getMaxDuration());
        // how to handle expired ones
        // always check from current date
        return cache.asMap().values()
                .stream()
                .filter(booking -> start.isBefore(booking.getArrivalDate()))
                .collect(Collectors.toSet());
    }

}
