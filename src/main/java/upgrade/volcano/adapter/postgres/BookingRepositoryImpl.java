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
import java.util.*;

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
            final BookingEntity entity = entityMapper.map(booking);
            jpaRepository.save(entity);
            return entity.getId();
        } else {
            final Optional<BookingEntity> entity = jpaRepository.findById(booking.getId());
            if (entity.isEmpty()) {
                throw new BookingException(BookingException.ErrorType.BOOKING_ID_NOT_FOUND, "Booking id: {" + booking.getId() + "} not found");
            }
            entityMapper.populateEntity(entity.get(), booking);
            return entity.get().getId();
        }

    }


    @Override
    @Transactional
    public void cancel(UUID bookingId, String email) {
        BookingEntity entity = jpaRepository.findByIdAndEmail(bookingId, email);
        if (Objects.isNull(entity)) {
            throw new BookingException(BookingException.ErrorType.BOOKING_ID_NOT_FOUND, "Booking id: {" + bookingId + "} not found");
        }
        entity.setIsCancelled(true);
    }

    @Override
    public List<Booking> availableDates(LocalDate startingDate, LocalDate endDate) {
        return new ArrayList<>();
//        return availableDates(startingDate, endDate);
    }
}
