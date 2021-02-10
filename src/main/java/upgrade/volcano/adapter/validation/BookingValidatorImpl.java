package upgrade.volcano.adapter.validation;

import upgrade.volcano.domain.BookingValidator;
import upgrade.volcano.domain.exception.BookingException;
import upgrade.volcano.domain.model.Booking;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

public class BookingValidatorImpl implements BookingValidator {

    // move to config
    private final Integer bookingMaxDuration;
    private final Long bookingMinDaysInAdv;
    private final Long bookingMaxDaysInAdv;

    public BookingValidatorImpl(final Integer bookingMaxDuration, final Long bookingMinDaysInAdv, final Long bookingMaxDaysInAdv) {
        this.bookingMaxDuration = bookingMaxDuration;
        this.bookingMinDaysInAdv = bookingMinDaysInAdv;
        this.bookingMaxDaysInAdv = bookingMaxDaysInAdv;
    }

    @Override
    public void validate(Booking booking) {
        validateDateOrder(booking);
        validateMaxDuration(booking);
        validateAdvanceBooking(booking);
    }

    private void validateDateOrder(Booking booking) {
        if(!isBeforeOrEqual(booking.getStartDate(),booking.getEndDate())){
            throw new BookingException(BookingException.ErrorType.INVALID_DATES,
                    "Start date [" + booking.getStartDate().toString() +"] must be same or before departure date [" + booking.getEndDate() +"]");

        }
    }

    private void validateAdvanceBooking(final Booking booking) {
        //  The campsite can be reserved minimum 1 day(s) ahead of arrival and up to 1 month in advance.
        final LocalDate today = LocalDate.now();
        final LocalDate startPeriod = LocalDate.now().plusDays(bookingMinDaysInAdv);
        final LocalDate endPeriod = LocalDate.now().plusDays(bookingMaxDaysInAdv);

        // change
        if (!isEqualOrAfter(booking.getStartDate(), startPeriod)
                || !isBeforeOrEqual(booking.getStartDate(), endPeriod)) {
            throw new BookingException(BookingException.ErrorType.INVALID_DATES,
                    "The campsite can be reserved minimum 1 day(s) ahead of arrival and up to 1 month in advance ");
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
        if (daysBetween > bookingMaxDuration) {
            throw new BookingException(BookingException.ErrorType.INVALID_DATES,
                    "The campsite can be reserved for max " + bookingMaxDuration + " days");
        }
    }


}
