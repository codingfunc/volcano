package upgrade.volcano.adapter.validation;

import org.junit.jupiter.api.Test;
import upgrade.volcano.domain.BookingValidator;
import upgrade.volcano.domain.exception.BookingException;
import upgrade.volcano.domain.model.Booking;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BookingValidatorImplTest {

    private Integer bookingMaxDuration = 3;
    private Long bookingMinDaysInAdvance = 1L;
    private Long bookingMaxDaysInAdvance = 30L;
    private BookingValidator validator = new BookingValidatorImpl(bookingMaxDuration, bookingMinDaysInAdvance, bookingMaxDaysInAdvance);


    @Test
    public void testCantBookToday() {
        final LocalDate startDate = LocalDate.now();
        final LocalDate endDate = startDate.plusDays(3L);
        var booking = Booking.builder().forClient("test").forEmail("test@test.com").startingAt(startDate).endingAt(endDate).build();
        BookingException exception = assertThrows(BookingException.class, () -> {
            validator.validate(booking);
        });
        assertTrue(BookingException.ErrorType.INVALID_DATES.equals(exception.getErrorType()));
    }

    @Test
    public void testCantBeyondAMonth() {
        final LocalDate startDate = LocalDate.now().plusDays(1L);
        final LocalDate endDate = startDate.plusDays(31L);
        var booking = Booking.builder().forClient("test").forEmail("test@test.com").startingAt(startDate).endingAt(endDate).build();
        BookingException exception = assertThrows(BookingException.class, () -> {
            validator.validate(booking);
        });
        assertTrue(BookingException.ErrorType.INVALID_DATES.equals(exception.getErrorType()));
    }


    @Test
    public void testValidDateOrder() {
        final LocalDate startDate = LocalDate.now().plusDays(1L);
        final LocalDate endDate = startDate.plusDays(2L);
        var booking = Booking.builder().forClient("test").forEmail("test@test.com").startingAt(startDate).endingAt(endDate).build();
        validator.validate(booking);
    }

    @Test
    public void testInvalidDateOrder() {
        final LocalDate startDate = LocalDate.now().plusDays(3L);
        final LocalDate endDate = LocalDate.now().plusDays(2L);
        var booking = Booking.builder().forClient("test").forEmail("test@test.com").startingAt(startDate).endingAt(endDate).build();
        BookingException exception = assertThrows(BookingException.class, () -> {
            validator.validate(booking);
        });
        assertTrue(BookingException.ErrorType.INVALID_INPUT.equals(exception.getErrorType()));
    }


    @Test
    public void testValidBookingDuration() {

    }

    @Test
    public void testInvalidBookingDuration() {

    }

    @Test
    public void testValidAdvanceBooking() {

    }

    @Test
    public void testInvalidAdvanceBooking() {

    }


    @Test
    void validate() {
    }
}