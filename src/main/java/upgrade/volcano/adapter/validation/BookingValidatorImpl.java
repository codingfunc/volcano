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
        final LocalDate arrivalDate = booking.getArrivalDate();
        final LocalDate DepartureDate = booking.getDepartureDate();

        validateDateOrder(arrivalDate, DepartureDate);
        validateMaxDuration(arrivalDate, DepartureDate);
        validateAdvanceBooking(arrivalDate, DepartureDate);
    }

    @Override
    public void validateDateOrder(LocalDate arrivalDate, LocalDate departureDate) {
        if (arrivalDate.isEqual(departureDate) || arrivalDate.isAfter(departureDate)) {
            throw new BookingException(BookingException.ErrorType.INVALID_INPUT,
                    "arrival date [" + arrivalDate + "] must be before departure date [" + departureDate + "]");
        }
    }

    private void validateAdvanceBooking(LocalDate arrivalDate, LocalDate departureDate) {
        //  The campsite can be reserved minimum 1 day(s) ahead of arrival and up to 1 month in advance.
        final LocalDate today = LocalDate.now();
        final LocalDate startPeriod = LocalDate.now().plusDays(config.getMinDaysInAdvance());
        final LocalDate endPeriod = LocalDate.now().plusDays(config.getMaxDaysInAdvance());

        if (arrivalDate.isBefore(startPeriod) || arrivalDate.isAfter(endPeriod)
                || departureDate.isBefore(startPeriod) || departureDate.isAfter(endPeriod)) {
            throw new BookingException(BookingException.ErrorType.INVALID_DATES,
                    "The campsite can be reserved minimum 1 day(s) ahead of arrival and up to 1 month in advance ");
        }
    }

    private void validateMaxDuration(final LocalDate startDate, final LocalDate endDate) {
        // The campsite can be reserved for max MAX_DURATION days.
        final long daysBetween = DAYS.between(startDate, endDate);
        if (daysBetween > config.getMaxDuration()) {
            throw new BookingException(BookingException.ErrorType.INVALID_DURATION,
                    "The campsite can be reserved for max " + config.getMaxDuration() + " days");
        }
    }
}
