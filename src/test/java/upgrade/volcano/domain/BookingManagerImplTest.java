package upgrade.volcano.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import upgrade.volcano.adapter.cache.DefaultCache;
import upgrade.volcano.adapter.validation.BookingValidatorImpl;
import upgrade.volcano.domain.exception.BookingException;
import upgrade.volcano.domain.model.Booking;
import upgrade.volcano.domain.model.ConstraintsConfig;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;
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
        bookingCache = new DefaultCache(config);
        bookingManager = new BookingManagerImpl(config, bookingRepository, bookingValidator, bookingCache);
    }

    @Test
    void testNewBooking() {
        final var arrival = LocalDate.now().plusDays(config.getMinDaysInAdvance());
        final var departure = arrival.plusDays(config.getMaxDuration());
        final var booking = booking(null, arrival, departure);
        bookingCache.invalidateAll();

        UUID bookingId = bookingManager.book(booking);
        final var expected = booking(bookingId, arrival, departure);
        Mockito.verify(bookingRepository).book(eq(expected));
        Optional<Booking> cached = bookingCache.get(bookingId);
        assertTrue(cached.isPresent());
        assertEquals(cached.get().getName(), booking.getName());
        assertEquals(cached.get().getEmail(), booking.getEmail());
        assertEquals(cached.get().getArrivalDate(), booking.getArrivalDate());
        assertEquals(cached.get().getDepartureDate(), booking.getDepartureDate());
    }

    @Test
    void testBackToBackBooking() {
        final var arrival = LocalDate.now().plusDays(config.getMinDaysInAdvance());
        final var departure = arrival.plusDays(config.getMaxDuration());
        final var booking1 = booking(null, arrival, departure);
        bookingCache.invalidateAll();

        UUID bookingId = bookingManager.book(booking1);
        final var booking2 = booking(null, departure, departure.plusDays(1));
        UUID bookingId2 = bookingManager.book(booking2);

        Optional<Booking> cached = bookingCache.get(bookingId);
        assertTrue(cached.isPresent());
        assertEquals(cached.get().getName(), booking1.getName());
        assertEquals(cached.get().getEmail(), booking1.getEmail());
        assertEquals(cached.get().getArrivalDate(), booking1.getArrivalDate());
        assertEquals(cached.get().getDepartureDate(), booking1.getDepartureDate());
    }



    @Test
    void testNewBookingNotAvailable() {
        final var arrival = LocalDate.now().plusDays(1);
        final var departure = arrival.plusDays(2);
        final var booking = booking(null, arrival, departure);

        // cache an existing booking with overlapping days
        var existingBooking = booking(UUID.randomUUID(), arrival.plusDays(1), arrival.plusDays(2));
        bookingCache.cache(existingBooking);

        BookingException exception = assertThrows(BookingException.class, () -> {
            UUID bookingId = bookingManager.book(booking);
        });
        assertTrue(BookingException.ErrorType.DATES_NOT_AVAILABLE.equals(exception.getErrorType()));
    }

    @Test
    void testUpdateBookingNewDates() {
        final var bookingId = UUID.randomUUID();

        final var arrival = LocalDate.now().plusDays(1);
        final var departure = arrival.plusDays(2);
        final var booking = booking(bookingId, arrival, departure);

        final var prevBooking = booking(bookingId, departure.plusDays(1), departure.plusDays(2));
        bookingCache.invalidateAll();
        bookingCache.cache(prevBooking);

        UUID actualId = bookingManager.book(booking);
        assertTrue(bookingId.equals(actualId));

        Mockito.verify(bookingRepository).book(eq(booking));

        // check cache
        Optional<Booking> cached = bookingCache.get(bookingId);
        assertTrue(cached.isPresent());
        assertEquals(cached.get().getName(), booking.getName());
        assertEquals(cached.get().getEmail(), booking.getEmail());
        assertEquals(cached.get().getArrivalDate(), booking.getArrivalDate());
        assertEquals(cached.get().getDepartureDate(), booking.getDepartureDate());
    }

    @Test
    void testExtendBooking() {
        final var bookingId = UUID.randomUUID();

        final var arrival = LocalDate.now().plusDays(1);
        final var departure = arrival.plusDays(2);
        final var booking = booking(bookingId, arrival, departure);

        final var prevBooking = booking(bookingId, arrival, arrival.plusDays(1));
        bookingCache.invalidateAll();
        bookingCache.cache(prevBooking);

        UUID actualId = bookingManager.book(booking);
        assertTrue(bookingId.equals(actualId));

        Mockito.verify(bookingRepository).book(eq(booking));
        Optional<Booking> cached = bookingCache.get(bookingId);
        assertTrue(cached.isPresent());
        assertEquals(cached.get().getName(), booking.getName());
        assertEquals(cached.get().getEmail(), booking.getEmail());
        assertEquals(cached.get().getArrivalDate(), booking.getArrivalDate());
        assertEquals(cached.get().getDepartureDate(), booking.getDepartureDate());

    }

    @Test
    void testUpdateBookingReduceBooking() {
        final var arrival = LocalDate.now().plusDays(1);
        final var departure = arrival.plusDays(2);
        final var bookingId = UUID.randomUUID();
        final var booking = booking(bookingId, arrival, departure);

        // previous 3 day booking
        final var prevBooking = booking(bookingId, arrival, arrival.plusDays(2));
        bookingCache.invalidateAll();
        bookingCache.cache(prevBooking);

        UUID actualId = bookingManager.book(booking);
        assertTrue(bookingId.equals(actualId));
        Mockito.verify(bookingRepository).book(eq(booking));

        Optional<Booking> cached = bookingCache.get(bookingId);
        assertTrue(cached.isPresent());
        assertEquals(cached.get().getName(), booking.getName());
        assertEquals(cached.get().getEmail(), booking.getEmail());
        assertEquals(cached.get().getArrivalDate(), booking.getArrivalDate());
        assertEquals(cached.get().getDepartureDate(), booking.getDepartureDate());
    }

    @Test
    void testFindAvailableDatesForGivenRange() {
        var booking1 = booking(UUID.randomUUID(), LocalDate.now().plusDays(1), LocalDate.now().plusDays(2));
        bookingCache.invalidateAll();
        bookingCache.cache(booking1);

        Set<LocalDate> booked = bookingCache.findBookedDates();

        LocalDate arrival = LocalDate.now();
        LocalDate departure = arrival.plusDays(5);
        final var requestedRange = arrival.datesUntil(departure.plusDays(1)).collect(Collectors.toSet());
        final var availableDates = bookingManager.findAvailableDates(arrival, departure);

        requestedRange.removeAll(booked);
        assertEquals(requestedRange, availableDates);
    }

    @Test
    void testFindAvailableDates() {
        var booking1 = booking(UUID.randomUUID(), LocalDate.now().plusDays(1), LocalDate.now().plusDays(2));
        var booking2 = booking(UUID.randomUUID(), LocalDate.now().plusDays(10), LocalDate.now().plusDays(12));
        var booking3 = booking(UUID.randomUUID(), LocalDate.now().plusDays(20), LocalDate.now().plusDays(22));
        bookingCache.invalidateAll();
        bookingCache.cache(booking1);
        bookingCache.cache(booking2);
        bookingCache.cache(booking3);

        Set<LocalDate> booked = bookingCache.findBookedDates();
        final var availableDates = bookingManager.findAvailableDates(null, null);
        // available should not have any booked
        Set<LocalDate> common = new HashSet<>(availableDates);
        common.retainAll(booked);

        assertTrue(common.isEmpty());
    }

    @Test
    void testCancel() {
        final var bookingId = UUID.randomUUID();
        bookingCache.invalidateAll();
        var booking = booking(bookingId, LocalDate.now().plusDays(1), LocalDate.now().plusDays(2));
        bookingCache.cache(booking);
        bookingManager.cancel(booking.getId());

        Mockito.verify(bookingRepository).cancel(eq(bookingId));
        Optional<Booking> cached = bookingCache.get(booking.getId());
        assertTrue(cached.isEmpty());
    }

    @Test
    void testConcurrentBooking() throws ExecutionException, InterruptedException {
        bookingCache.invalidateAll();
        final var start = LocalDate.now().plusDays(1);
        final var end = start.plusDays(1);

        final var threadCount = 5;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        List<Future<Optional<UUID>>> bookingThreads = new ArrayList<>();
        IntStream.range(0, threadCount).forEach(i ->{
            final Callable<Optional<UUID>> task = task(booking(null, start, end));
            bookingThreads.add(executor.submit(task));
        } );

        // only one booking
        final var bookings  = bookingThreads.stream().map(f -> {
            try {
                return f.get();
            } catch (Exception e) {
                throw new RuntimeException("test failed");
            }
        }).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());

        // only one future is present
        assertEquals(1, bookings.size());
        var bookingId = bookings.get(0);
        var cached = bookingCache.get(bookingId);
        assertTrue(cached.isPresent());
        var expected = booking(bookingId, start, end);
        validateCached(expected, cached.get());
    }

    private void validateCached(final Booking booking, final Booking cached) {
        assertEquals(cached.getName(), booking.getName());
        assertEquals(cached.getEmail(), booking.getEmail());
        assertEquals(cached.getArrivalDate(), booking.getArrivalDate());
        assertEquals(cached.getDepartureDate(), booking.getDepartureDate());
    }

    @Test
    void testBooking() {
        final var arrival = LocalDate.now().plusDays(1);
        final var departure = arrival.plusDays(1);

        final var threadCount = 5;
        List<Booking> bookings = new ArrayList<>();
        IntStream.range(0, threadCount).forEach(i -> bookings.add(booking(null, arrival, departure)));

        final var booking1 = booking(null, arrival, departure);
        final var booking2 = booking(null, arrival, departure);
        bookingManager.book(booking1);

        BookingException exception = assertThrows(BookingException.class, () -> {
            UUID bookingId = bookingManager.book(booking2);
        });
        assertTrue(BookingException.ErrorType.DATES_NOT_AVAILABLE.equals(exception.getErrorType()));
    }

    private Callable<Optional<UUID>> task(final Booking booking) {
        return () -> {
            try {
                sleep(5);
                return Optional.of(bookingManager.book(booking));
            } catch (Exception e) {
                return Optional.empty();
            }
        };
    }


    private Booking booking(UUID id, LocalDate arrivalDate, LocalDate departureDate) {
        return
                Booking.builder()
                        .forId(id)
                        .forClient("John Doe")
                        .forEmail("test@test.com")
                        .arrivingAt(arrivalDate)
                        .departingAt(departureDate)
                        .build();
    }
}