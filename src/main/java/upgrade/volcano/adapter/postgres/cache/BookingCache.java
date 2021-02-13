package upgrade.volcano.adapter.postgres.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import upgrade.volcano.domain.model.Booking;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * A fast cache to maintain all active bookings.
 */
public class BookingCache {


    private Cache<UUID, Set<LocalDate>> cache;

    public BookingCache(final Integer cacheDurationInDays) {
        // cache duration is the number of days in advance a user can book
        // this also determines how many entries there can be.
        cache = Caffeine.newBuilder()
                .expireAfterWrite(Duration.of(cacheDurationInDays, ChronoUnit.DAYS))
                .maximumSize(cacheDurationInDays)
                .build();
    }

    public void put(Booking booking){
        cache.invalidate(booking.getId());
        Set<LocalDate> dates = booking.getStartDate().datesUntil(booking.getEndDate()).collect(Collectors.toSet());
        cache.put(booking.getId(), dates);
    }
    public void invalidateAll() {
        cache.invalidateAll();
    }

    public void invalidate(final UUID bookingId) {
        cache.invalidate(bookingId);
    }

    public Set<LocalDate> bookedDates() {
        return cache.asMap().values().stream().map(s -> s.stream()).flatMap(s -> s).collect(Collectors.toSet());
    }
}
