package upgrade.volcano.adapter.postgres;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import upgrade.volcano.adapter.postgres.jpa.BookingJpaRepository;
import upgrade.volcano.domain.BookingRepository;
import upgrade.volcano.domain.model.Booking;

import java.time.LocalDate;
import java.util.UUID;

class BookingRepositoryImplTest {

    private BookingJpaRepository jpaRepository;
    private BookingRepository repository;

    @BeforeEach
    void setup() {
        jpaRepository = Mockito.mock(BookingJpaRepository.class);
        repository = new BookingRepositoryImpl(jpaRepository);
    }


    @Test
    void testNewBooking() {


    }

    @Test
    void findByBookingId() {
    }

    @Test
    void cancel() {
    }

    @Test
    void findActiveBookings() {
    }

    private Booking booking(UUID id, LocalDate startDate, LocalDate endDate) {
        return
                Booking.builder()
                        .forId(id)
                        .forClient("John Doe")
                        .forEmail("test@test.com")
                        .startingAt(startDate)
                        .endingAt(endDate)
                        .build();
    }
}