package upgrade.volcano.adapter.validation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import upgrade.volcano.domain.BookingValidator;
import upgrade.volcano.domain.model.Booking;

import java.time.LocalDate;

@ActiveProfiles("test")
class BookingValidatorImplTest {

    @Value("${booking.duration.max}")
    private Integer bookingMaxDuration = 3;

    @Value("${booking.advance.min}")
    private Long bookingMinDaysInAdvance = 1L;

    @Value("${booking.advance.max}")
    private Long bookingMaxDaysInAdvance = 30L;

    private BookingValidator validator = new BookingValidatorImpl(bookingMaxDuration, bookingMinDaysInAdvance, bookingMaxDaysInAdvance);

    @Test
    public void testValidBookingDuration() {
        var booking = Booking.builder().forClient("test").forEmail("test@test.com").startingAt(LocalDate.now()).endingAt(LocalDate.now().plusDays(3)).build();
        validator.validate(booking);
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