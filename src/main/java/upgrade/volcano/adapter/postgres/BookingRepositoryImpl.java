package upgrade.volcano.adapter.postgres;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import upgrade.volcano.adapter.cache.BookingCache;
import upgrade.volcano.adapter.postgres.entity.BookingEntity;
import upgrade.volcano.adapter.postgres.jpa.BookingJpaRepository;
import upgrade.volcano.domain.BookingRepository;
import upgrade.volcano.domain.exception.BookingException;
import upgrade.volcano.domain.model.Booking;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
public class BookingRepositoryImpl implements BookingRepository {

    private final BookingJpaRepository jpaRepository;
    private final BookingCache bookingCache;
    private final EntityMapper entityMapper = new EntityMapper();

    @Autowired
    public BookingRepositoryImpl(final BookingJpaRepository jpaRepository, final BookingCache bookingCache) {
        this.jpaRepository = jpaRepository;
        this.bookingCache = bookingCache;
    }

    @Override
    @Transactional
    public UUID book(Booking booking) {
        if (Objects.isNull(booking.getId())) {
            final var entity = entityMapper.map(booking);
            jpaRepository.save(entity);
            return entity.getId();
        } else {
            final var entity = jpaRepository.findByIdAndEmail(booking.getId(), booking.getEmail());
            if (Objects.isNull(entity)) {
                throw new BookingException(BookingException.ErrorType.BOOKING_NOT_FOUND, "Booking id:{" + booking.getId() + "}, email:{" + booking.getEmail() + "not found");
            }
            entityMapper.populateEntity(entity, booking);
            return entity.getId();
        }
    }


    @Override
    @Transactional
    public void cancel(UUID bookingId, String email) {
        BookingEntity entity = jpaRepository.findByIdAndEmail(bookingId, email);
        if (Objects.isNull(entity)) {
            throw new BookingException(BookingException.ErrorType.BOOKING_NOT_FOUND, "Booking id: {" + bookingId + "} not found");
        }
        entity.setIsCancelled(true);
    }

    @Override
    public List<Booking> availableDates(LocalDate startingDate, LocalDate endDate) {
        return new ArrayList<>();
//        return availableDates(startingDate, endDate);
    }
}
