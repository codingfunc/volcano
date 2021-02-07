package upgrade.volcano.adapter.postgres;

import org.springframework.stereotype.Component;
import upgrade.volcano.adapter.cache.BookingCache;
import upgrade.volcano.domain.BookingRepository;
import upgrade.volcano.domain.model.Booking;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Component
public class BookingRepositoryImpl implements BookingRepository {

    private final BookingRepository bookingRepository;
    private final BookingCache bookingCache;

    public BookingRepositoryImpl(final BookingRepository bookingRepository, final BookingCache bookingCache){
        this.bookingRepository = bookingRepository;
        this.bookingCache = bookingCache;
    }

    @Override
    public Booking book(Booking booking) {
        return bookingRepository.book(booking);
    }

    @Override
    public void cancel(UUID bookingId, String email) {
        bookingRepository.cancel(bookingId, email);

    }

    @Override
    public List<Booking> availableDates(LocalDate startingDate, LocalDate endDate) {
        return availableDates(startingDate, endDate);
    }
}
