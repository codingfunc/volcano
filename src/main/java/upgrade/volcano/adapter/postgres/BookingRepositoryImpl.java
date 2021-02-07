package upgrade.volcano.adapter.postgres;

import org.springframework.stereotype.Component;
import upgrade.volcano.adapter.cache.BookingCache;
import upgrade.volcano.adapter.postgres.jpa.BookingJpaRepository;
import upgrade.volcano.domain.BookingRepository;
import upgrade.volcano.domain.model.Booking;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Component
public class BookingRepositoryImpl implements BookingRepository {

    private final BookingJpaRepository jpaRepository;
    private final BookingCache bookingCache;

    public BookingRepositoryImpl(final BookingJpaRepository jpaRepository, final BookingCache bookingCache){
        this.jpaRepository = jpaRepository;
        this.bookingCache = bookingCache;
    }

    @Override
    @Transactional
    public Booking book(Booking booking) {
//        var entity =  jpaRepository.save(booking);
        return null;

    }

    @Override
    @Transactional
    public void cancel(UUID bookingId, String email) {
//        jpaRepository.cancel(bookingId, email);

    }

    @Override
    public List<Booking> availableDates(LocalDate startingDate, LocalDate endDate) {
        return availableDates(startingDate, endDate);
    }
}
