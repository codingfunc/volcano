package upgrade.volcano.domain.exception;

public class BookingException extends RuntimeException {

    public static enum ErrorType {
        INVALID_DATES,
        BOOKING_ID_NOT_FOUND

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
}
