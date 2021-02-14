package upgrade.volcano.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import upgrade.volcano.adapter.validation.BookingValidatorImpl;
import upgrade.volcano.domain.model.Booking;
import upgrade.volcano.domain.model.ConstraintsConfig;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;

class BookingManagerImplTest {
    private BookingRepository bookingRepository;
    private BookingValidator bookingValidator;
    private BookingCache bookingCache;
    private ConstraintsConfig config;
    private BookingManager bookingManager;

    @BeforeEach
    void setUp() {
        config = new ConstraintsConfig(3, 1, 30);
        bookingValidator = new BookingValidatorImpl(config);
        bookingRepository = Mockito.mock(BookingRepository.class);
        bookingCache = Mockito.mock(BookingCache.class);
        bookingManager = new BookingManagerImpl(config, bookingRepository, bookingValidator, bookingCache);
    }

    @Test
    void testNewBooking() {
        final var start = LocalDate.now().plusDays(1);
        final var end = start.plusDays(2);
        final var booking = booking(null, start, end);
        UUID bookingId = bookingManager.book(booking);
        final var expected = booking(bookingId, start, end);
        Mockito.verify(bookingRepository).book(eq(expected));
        Mockito.verify(bookingCache).cache(eq(expected));

    }

    @Test
    void testUpdateBooking() {
        final var start = LocalDate.now().plusDays(1);
        final var end = start.plusDays(2);
        final var bookingId = UUID.randomUUID();
        final var booking = booking(bookingId, start, end);
        UUID actualId = bookingManager.book(booking);
        assertTrue(bookingId.equals(actualId));

        Mockito.verify(bookingRepository).book(eq(booking));
        Mockito.verify(bookingCache).cache(eq(booking));
    }

    @Test
    void testFindAvailableDates() {
        final var start = LocalDate.now().plusDays(1);
        final var end = start.plusDays(10);

        final var bookedDates = start.datesUntil(end.minusDays(2)).collect(Collectors.toSet());
        final var expectedFreeDates = end.minusDays(2).datesUntil(end.plusDays(1)).collect(Collectors.toList());
        Mockito.when(bookingCache.findBookedDates()).thenReturn(bookedDates);
        final var availableDates = bookingManager.findAvailableDates(start, end);
        assertEquals(expectedFreeDates, availableDates);
        Mockito.verify(bookingCache).findBookedDates();

    }

    @Test
    void testCancel(){
        final var bookingId = UUID.randomUUID();
        bookingManager.cancel(bookingId);

        Mockito.verify(bookingRepository).cancel(eq(bookingId));
        Mockito.verify(bookingCache).invalidate(eq(bookingId));

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