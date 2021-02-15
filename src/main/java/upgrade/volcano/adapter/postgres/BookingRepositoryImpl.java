package upgrade.volcano.adapter.postgres;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import upgrade.volcano.adapter.postgres.entity.BookingEntity;
import upgrade.volcano.adapter.postgres.jpa.BookingJpaRepository;
import upgrade.volcano.domain.BookingRepository;
import upgrade.volcano.domain.exception.BookingException;
import upgrade.volcano.domain.model.Booking;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class BookingRepositoryImpl implements BookingRepository {

    private final BookingJpaRepository jpaRepository;
    private final EntityMapper entityMapper = new EntityMapper();

    @Autowired
    public BookingRepositoryImpl(final BookingJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    @Transactional
    synchronized public void book(final Booking booking) {
        final var optEntity = jpaRepository.findOptionalByBookingId(booking.getId().toString());
        if (optEntity.isEmpty()) {
            final var entity = entityMapper.map(booking);
            jpaRepository.save(entity);
        } else {
            final var entity = optEntity.get();
            if (Boolean.TRUE.equals(entity.getIsCancelled())) {
                throw new BookingException(BookingException.ErrorType.BOOKING_IS_CANCELLED, "Booking id: {" + entity.getBookingId() + "} has been cancelled and can't be updated");
            }
            entityMapper.updateEntity(entity, booking);
            jpaRepository.save(entity);
        }
    }

    @Override
    @Transactional
    public Optional<Booking> findByBookingId(final UUID bookingId) {
        final var optEntity = jpaRepository.findOptionalByBookingId(bookingId.toString());
        if (optEntity.isPresent()) {
            return Optional.of(entityMapper.map(optEntity.get()));
        }
        return Optional.empty();
    }


    @Override
    @Transactional
    public void cancel(final UUID bookingId) {
        final var entity = jpaRepository.findOptionalByBookingId(bookingId.toString());
        if (entity.isEmpty()) {
            throw new BookingException(BookingException.ErrorType.BOOKING_NOT_FOUND, "Booking id: {" + bookingId + "} not found");
        }
        entity.get().setIsCancelled(true);
    }

    @Override
    public Set<Booking> findActiveBookings(final LocalDate startingDate, final LocalDate endDate) {
        final Set<BookingEntity> activeBookings = jpaRepository.findByIsCancelledIsNullAndDepartureDateBetween(startingDate, endDate);
        return activeBookings.stream().map(e -> entityMapper.map(e)).collect(Collectors.toSet());
    }
}
