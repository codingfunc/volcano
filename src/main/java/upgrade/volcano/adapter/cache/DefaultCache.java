package upgrade.volcano.adapter.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import upgrade.volcano.domain.BookingCache;
import upgrade.volcano.domain.model.Booking;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * A fast cache to maintain all active bookings.
 */
public class DefaultCache implements BookingCache {


    private Cache<UUID, Booking> cache;

    public DefaultCache(final Integer cacheDurationInDays) {
        // cache duration is the number of days in advance a user can book
        // this also determines how many entries there can be.
        cache = Caffeine.newBuilder()
                .expireAfterWrite(Duration.of(cacheDurationInDays, ChronoUnit.DAYS))
                .maximumSize(cacheDurationInDays)
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

        // how to handle expired ones
        // always check from current date
        return cache.asMap().values()
                .stream()
                .filter(booking -> LocalDate.now().isBefore(booking.getStartDate()))
                .map(booking -> {
                    return booking.getStartDate().datesUntil(booking.getEndDate().plusDays(1));
                }).flatMap(s -> s).collect(Collectors.toSet());
    }

}
