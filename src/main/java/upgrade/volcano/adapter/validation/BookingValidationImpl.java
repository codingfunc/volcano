package upgrade.volcano.adapter.validation;

import upgrade.volcano.domain.BookingValidator;
import upgrade.volcano.domain.exception.BookingException;
import upgrade.volcano.domain.model.Booking;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

public class BookingValidationImpl implements BookingValidator {

    // move to config
    private static Integer MAX_DURATION = 3;
    private static Long BOOKING_MIN_DAYS_IN_ADV = 1L;
    private static Long BOOKING_MAX_DAYS_IN_ADV = 30L;


    @Override
    public void validate(Booking booking) {
        validateMaxDuration(booking);
        validateAdvanceBooking(booking);
    }

    private void validateAdvanceBooking(final Booking booking) {
        //  The campsite can be reserved minimum 1 day(s) ahead of arrival and up to 1 month in advance.
        final LocalDate today = LocalDate.now();
        final LocalDate startPeriod = LocalDate.now().plusDays(BOOKING_MIN_DAYS_IN_ADV);
        final LocalDate endPeriod = LocalDate.now().plusDays(BOOKING_MAX_DAYS_IN_ADV);

        // change
        if (!isEqualOrAfter(booking.getStartDate(), startPeriod)
                || !isBeforeOrEqual(booking.getStartDate(), endPeriod)) {
            throw new BookingException(BookingException.ErrorType.INVALID_DATES, "The campsite can be reserved minimum 1 day(s) ahead of arrival and up to 1 month in advance ");
        }
    }

    private boolean isEqualOrAfter(final LocalDate date1, final LocalDate date2) {
        return date1.isEqual(date2) || date1.isAfter(date2);
    }

    private boolean isBeforeOrEqual(final LocalDate date1, final LocalDate date2) {
        return date1.isBefore(date2) || date1.isEqual(date2);
    }


    private void validateMaxDuration(final Booking booking) {
        // The campsite can be reserved for max MAX_DURATION days.
        final long daysBetween = DAYS.between(booking.getStartDate(), booking.getEndDate());
        if (daysBetween > MAX_DURATION) {
            throw new BookingException(BookingException.ErrorType.INVALID_DATES, "The campsite can be reserved for max " + MAX_DURATION + " days");
        }
    }


}
