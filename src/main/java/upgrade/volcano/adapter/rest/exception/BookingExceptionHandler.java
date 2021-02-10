package upgrade.volcano.adapter.rest.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import upgrade.volcano.domain.exception.BookingException;

@ControllerAdvice
@Slf4j
public class BookingExceptionHandler {
    @ExceptionHandler({BookingException.class})

    public ResponseEntity<BookingErrorWS> handleException(BookingException e, WebRequest request) {
        log.error("Exception thrown by API {}", request.getContextPath(), e);
        BookingErrorWS error = toErrorWS(e);
        return new ResponseEntity<>(error, null, HttpStatus.valueOf(error.getStatus()));
    }

    private HttpStatus getExceptionHttpStatus(BookingException exception) {
        switch (exception.getErrorType()) {
            case INVALID_DATES:
            case INVALID_INPUT:
            case BOOKING_NOT_FOUND:
                return HttpStatus.BAD_REQUEST;
            case DATES_NOT_AVAILABLE:
                return HttpStatus.CONFLICT;
            default:
                return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    private BookingErrorWS toErrorWS(BookingException exception) {
        BookingErrorWS errorWS = new BookingErrorWS();
        errorWS.setStatus(getExceptionHttpStatus(exception).value());
        errorWS.setCode(exception.getErrorType().toString());
        errorWS.setMessage(exception.getMessage());
        return errorWS;
    }
}
