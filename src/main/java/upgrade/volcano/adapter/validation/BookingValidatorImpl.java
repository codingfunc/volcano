package upgrade.volcano.adapter.validation;

import upgrade.volcano.domain.BookingManager;
import upgrade.volcano.domain.BookingValidator;
import upgrade.volcano.domain.exception.BookingException;
import upgrade.volcano.domain.model.Booking;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import static java.time.temporal.ChronoUnit.DAYS;

public class BookingValidatorImpl implements BookingValidator {

    private final BookingManager bookingManager;
    private final Integer bookingMaxDuration;
    private final Integer bookingMinDaysInAdv;
    private final Integer bookingMaxDaysInAdv;

    public BookingValidatorImpl(final BookingManager bookingManager, final Integer bookingMaxDuration, final Integer bookingMinDaysInAdv, final Integer bookingMaxDaysInAdv) {
        this.bookingManager = bookingManager;
        this.bookingMaxDuration = bookingMaxDuration;
        this.bookingMinDaysInAdv = bookingMinDaysInAdv;
        this.bookingMaxDaysInAdv = bookingMaxDaysInAdv;
    }

    @Override
    public void validate(Booking booking) {
        final LocalDate startDate = booking.getStartDate();
        final LocalDate endDate = booking.getEndDate();

        validateDateOrder(startDate, endDate);
        validateMaxDuration(startDate, endDate);
        validateAdvanceBooking(booking);

        // can validate more if needed,
        // a user email can only book once in one month
        // can't have two active booking with same user
        // validaNoOtherBookingForSameEmail(booking);
    }


    private void validaNoOtherBookingForSameEmail(final Booking booking) {
        if (booking.isNew()) {
            return;
        }

        // TODO: change repo id to string
        final UUID bookindId = booking.getId();
        final String email = booking.getEmail();

        Set<Booking> activeBookings = bookingManager.findAllByEmail(booking.getEmail());
        boolean activeBookingFound = activeBookings.stream().filter(b -> !b.getId().equals(bookindId)).findAny().isPresent();
        if (activeBookingFound) {
            throw new BookingException(BookingException.ErrorType.MULTIPLE_BOOKINGS_NOT_ALLOWED,
                    "Email [" + booking.getEmail() + "] is already registerd for an active booking.");
        }
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

    private void validateMaxDuration(final LocalDate startDate, final LocalDate endDate) {
        // The campsite can be reserved for max MAX_DURATION days.
        final long daysBetween = DAYS.between(startDate, endDate) + 1;
        if (daysBetween > bookingMaxDuration) {
            throw new BookingException(BookingException.ErrorType.INVALID_DATES,
                    "The campsite can be reserved for max " + bookingMaxDuration + " days");
        }
    }
}
