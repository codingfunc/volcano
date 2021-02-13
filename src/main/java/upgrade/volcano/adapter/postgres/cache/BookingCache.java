package upgrade.volcano.adapter.postgres.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import upgrade.volcano.domain.model.Booking;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * A fast cache to maintain all active bookings.
 */
public class BookingCache {

    private Cache<UUID, Booking> cache;

    public BookingCache(final Integer cacheDurationInDays) {
        // cache duration is the number of days in advance a user can book
        // this also determines how many entries there can be.
        cache = Caffeine.newBuilder()
                .expireAfterWrite(Duration.of(cacheDurationInDays, ChronoUnit.DAYS))
                .maximumSize(cacheDurationInDays)
                .build();
    }

    public void invalidateAll() {
        cache.invalidateAll();
    }

    public void invalidate(final UUID bookingId) {
        cache.invalidate(bookingId);
    }


    public Optional<Booking> get(final UUID bookingId) {
        return Optional.ofNullable(cache.getIfPresent(bookingId));
    }

//    public Set<Booking> findByEmail(final String email){
//        cache.
//    }

}
