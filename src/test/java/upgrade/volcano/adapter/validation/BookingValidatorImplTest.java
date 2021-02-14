package upgrade.volcano.adapter.validation;

import org.junit.jupiter.api.Test;
import upgrade.volcano.domain.BookingValidator;
import upgrade.volcano.domain.exception.BookingException;
import upgrade.volcano.domain.model.Booking;
import upgrade.volcano.domain.model.ConstraintsConfig;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BookingValidatorImplTest {

    private ConstraintsConfig config = new ConstraintsConfig(3, 1, 30);
    private BookingValidator validator = new BookingValidatorImpl(config);

    @Test
    public void testCantBookInThePast() {
        final LocalDate startDate = LocalDate.now().minusDays(2L);
        final LocalDate endDate = LocalDate.now();
        var booking = Booking.builder().forClient("test").forEmail("test@test.com").arrivingAt(startDate).departingAt(endDate).build();
        BookingException exception = assertThrows(BookingException.class, () -> {
            validator.validate(booking);
        });
        assertTrue(BookingException.ErrorType.INVALID_DATES.equals(exception.getErrorType()));
    }

    @Test
    public void testCantBookToday() {
        final LocalDate startDate = LocalDate.now();
        final LocalDate endDate = startDate.plusDays(2L);
        var booking = Booking.builder().forClient("test").forEmail("test@test.com").arrivingAt(startDate).departingAt(endDate).build();
        BookingException exception = assertThrows(BookingException.class, () -> {
            validator.validate(booking);
        });
        assertTrue(BookingException.ErrorType.INVALID_DATES.equals(exception.getErrorType()));
    }

    @Test
    public void testCantBookBeyondAMonth() {
        final LocalDate startDate = LocalDate.now().plusDays(30L);
        final LocalDate endDate = startDate.plusDays(1L);
        var booking = Booking.builder().forClient("test").forEmail("test@test.com").arrivingAt(startDate).departingAt(endDate).build();
        BookingException exception = assertThrows(BookingException.class, () -> {
            validator.validate(booking);
        });
        assertTrue(BookingException.ErrorType.INVALID_DATES.equals(exception.getErrorType()));
    }


    @Test
    public void testValidDateOrder() {
        final LocalDate startDate = LocalDate.now().plusDays(1L);
        final LocalDate endDate = startDate.plusDays(2L);
        var booking = Booking.builder().forClient("test").forEmail("test@test.com").arrivingAt(startDate).departingAt(endDate).build();
        validator.validate(booking);
    }

    @Test
    public void testInvalidDateOrder() {
        final LocalDate startDate = LocalDate.now().plusDays(3L);
        final LocalDate endDate = LocalDate.now().plusDays(2L);
        var booking = Booking.builder().forClient("test").forEmail("test@test.com").arrivingAt(startDate).departingAt(endDate).build();
        BookingException exception = assertThrows(BookingException.class, () -> {
            validator.validate(booking);
        });
        assertTrue(BookingException.ErrorType.INVALID_INPUT.equals(exception.getErrorType()));
    }


    @Test
    public void testValidBookingDuration() {
        final LocalDate startDate = LocalDate.now().plusDays(1L);
        final LocalDate endDate = startDate.plusDays(config.getMaxDuration() - 1);
        var booking = Booking.builder().forClient("test").forEmail("test@test.com").arrivingAt(startDate).departingAt(endDate).build();
        validator.validate(booking);

    }

    @Test
    public void testInvalidBookingDuration() {
        final LocalDate startDate = LocalDate.now().plusDays(1L);
        final LocalDate endDate = startDate.plusDays(config.getMaxDuration());
        var booking = Booking.builder().forClient("test").forEmail("test@test.com").arrivingAt(startDate).departingAt(endDate).build();
        BookingException exception = assertThrows(BookingException.class, () -> {
            validator.validate(booking);
        });
        assertTrue(BookingException.ErrorType.INVALID_DURATION.equals(exception.getErrorType()));
    }
}