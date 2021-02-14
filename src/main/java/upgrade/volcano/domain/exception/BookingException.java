package upgrade.volcano.domain.exception;

public class BookingException extends RuntimeException {

    public enum ErrorType {
        INVALID_INPUT,
        INVALID_DATES,
        INVALID_DURATION,
        BOOKING_NOT_FOUND,
        DATES_NOT_AVAILABLE,
        MULTIPLE_BOOKINGS_NOT_ALLOWED
    }

    private final ErrorType errorType;

    public BookingException(final ErrorType errorType, final String message) {
        super(message);
        this.errorType = errorType;
    }

    public BookingException(final ErrorType errorType, final String message, final Throwable throwable) {
        super(message, throwable);
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return errorType;
    }
}
