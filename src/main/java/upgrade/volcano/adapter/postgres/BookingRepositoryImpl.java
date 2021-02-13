package upgrade.volcano.adapter.postgres;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import upgrade.volcano.adapter.postgres.cache.BookingCache;
import upgrade.volcano.adapter.postgres.entity.BookingEntity;
import upgrade.volcano.adapter.postgres.jpa.BookingJpaRepository;
import upgrade.volcano.domain.BookingRepository;
import upgrade.volcano.domain.exception.BookingException;
import upgrade.volcano.domain.model.Booking;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class BookingRepositoryImpl implements BookingRepository {

    private final BookingJpaRepository jpaRepository;
    private final BookingCache bookingCache;
    private final EntityMapper entityMapper = new EntityMapper();

    @Autowired
    public BookingRepositoryImpl(final BookingCache bookingCache, final BookingJpaRepository jpaRepository) {
        this.bookingCache = bookingCache;
        this.jpaRepository = jpaRepository;
        initCache();
    }

    private void initCache() {
        var activeBookings = jpaRepository.findByIsCancelledFalse();nd
        bookingCache.populatefinal Set<BookingEntity>
    }

    @Override
    @Transactional
    public UUID book(final Booking booking) {
        if (Objects.isNull(booking.getId())) {
            final var entity = entityMapper.map(booking);
            jpaRepository.save(entity);
            return entity.getId();
        } else {
            final var optEntity = jpaRepository.findOptionalByIdAndEmail(booking.getId(), booking.getEmail());
            if (optEntity.isEmpty()) {
                throw new BookingException(BookingException.ErrorType.BOOKING_NOT_FOUND, "Booking id:[" + booking.getId() + "], email:[" + booking.getEmail() + "] not found");
            }
            final var entity = optEntity.get();
            entityMapper.populateEntity(entity, booking);
            return entity.getId();
        }
    }


    @Override
    @Transactional
    public void cancel(final UUID bookingId, final String email) {
        final var entity = jpaRepository.findOptionalByIdAndEmail(bookingId, email);
        if (entity.isEmpty()) {
            throw new BookingException(BookingException.ErrorType.BOOKING_NOT_FOUND, "Booking id: {" + bookingId + "} not found");
        }

        entity.get().setIsCancelled(true);
    }

    @Override
    public Set<Booking> getActiveBookings(final LocalDate startingDate, final LocalDate endDate) {

        final Set<BookingEntity> activeBookings = jpaRepository.findByIsCancelledFalseAndStartDateBetween(startingDate, endDate);
        return activeBookings.stream().map(e -> entityMapper.map(e)).collect(Collectors.toSet());
    }
}
