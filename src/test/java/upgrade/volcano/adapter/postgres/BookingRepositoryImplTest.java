package upgrade.volcano.adapter.postgres;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import upgrade.volcano.adapter.postgres.entity.BookingEntity;
import upgrade.volcano.adapter.postgres.jpa.BookingJpaRepository;
import upgrade.volcano.domain.BookingRepository;
import upgrade.volcano.domain.model.Booking;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class BookingRepositoryImplTest {

    private BookingJpaRepository jpaRepository;
    private BookingRepository repository;
    private EntityMapper mapper;

    @BeforeEach
    void setup() {
        jpaRepository = Mockito.mock(BookingJpaRepository.class);
        repository = new BookingRepositoryImpl(jpaRepository);
        mapper = new EntityMapper();
    }


    @Test
    void testNewBooking() {
        var booking = booking();
        when(jpaRepository.findOptionalByBookingId(eq(booking.getId().toString()))).thenReturn(Optional.empty());

        repository.book(booking);
        Mockito.verify(jpaRepository).findByIsCancelledIsNullAndStartDateBetween(eq(booking.getStartDate()), eq(booking.getEndDate()));
        Mockito.verify(jpaRepository).findOptionalByBookingId(eq(booking.getId().toString()));

        var entity = mapper.map(booking);
        Mockito.verify(jpaRepository).save(eq(entity));
    }

    @Test
    void testExistingBooking() {
        var booking = booking();

        // existing booking has values set
        var existing = mapper.map(booking);
        existing.setEmail("existing-" + booking.getName());
        existing.setEmail("existing-" + booking.getEmail());
        existing.setStartDate(booking.getStartDate().plusDays(1));
        existing.setEndDate(booking.getEndDate().plusDays(1));

        when(jpaRepository.findOptionalByBookingId(eq(booking.getId().toString()))).thenReturn(Optional.of(existing));

        repository.book(booking);
        Mockito.verify(jpaRepository).findByIsCancelledIsNullAndStartDateBetween(eq(booking.getStartDate()), eq(booking.getEndDate()));
        Mockito.verify(jpaRepository).findOptionalByBookingId(eq(booking.getId().toString()));


        var expected = mapper.map(booking);
        // updated entity with dates
        mapper.updateEntity(expected, booking);
        Mockito.verify(jpaRepository).save(eq(expected));

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

    private BookingEntity entity(UUID id, LocalDate startDate, LocalDate endDate){
        var booking = booking(id, startDate, endDate);
        return mapper.map(booking);
    }

    private Booking booking() {
        return booking(UUID.randomUUID(), LocalDate.now().plusDays(1), LocalDate.now().plusDays(2));
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