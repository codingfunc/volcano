package upgrade.volcano.adapter.validation;

import upgrade.volcano.domain.BookingValidator;
import upgrade.volcano.domain.exception.BookingException;
import upgrade.volcano.domain.model.Booking;
import upgrade.volcano.domain.model.ConstraintsConfig;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

public class BookingValidatorImpl implements BookingValidator {
    private final ConstraintsConfig config;

    public BookingValidatorImpl(final ConstraintsConfig config) {
        this.config = config;
    }

    @Override
    public void validate(final Booking booking) {
        final LocalDate startDate = booking.getArrivalDate();
        final LocalDate endDate = booking.getDepartureDate();

        validateDateOrder(startDate, endDate);
        validateMaxDuration(startDate, endDate);
        validateAdvanceBooking(booking);
    }

    @Override
    public void validateDateOrder(LocalDate startDate, LocalDate endDate) {
        if (!isBeforeOrEqual(startDate, endDate)) {
            throw new BookingException(BookingException.ErrorType.INVALID_INPUT,
                    "Start date [" + startDate + "] must be same or before departure date [" + endDate + "]");
        }
    }

    private void validateAdvanceBooking(final Booking booking) {
        //  The campsite can be reserved minimum 1 day(s) ahead of arrival and up to 1 month in advance.
        final LocalDate today = LocalDate.now();
        final LocalDate startPeriod = LocalDate.now().plusDays(config.getMinDaysInAdvance());
        final LocalDate endPeriod = LocalDate.now().plusDays(config.getMaxDaysInAdvance());

        // change
        if (!isEqualOrAfter(booking.getArrivalDate(), startPeriod)
                || !isBeforeOrEqual(booking.getArrivalDate(), endPeriod)
                || !isBeforeOrEqual(booking.getDepartureDate(), endPeriod)) {
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

    private void validateMaxDuration(final LocalDate startDate, final LocalDate endDate) {
        // The campsite can be reserved for max MAX_DURATION days.
        final long daysBetween = DAYS.between(startDate, endDate) + 1;
        if (daysBetween > config.getMaxDuration()) {
            throw new BookingException(BookingException.ErrorType.INVALID_DURATION,
                    "The campsite can be reserved for max " + config.getMaxDuration() + " days");
        }
    }
}
