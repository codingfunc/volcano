package upgrade.volcano.adapter.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import upgrade.volcano.adapter.rest.dto.BookingDto;
import upgrade.volcano.domain.BookingManager;
import upgrade.volcano.domain.model.BookingRequest;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(
        value = "/api/v1/booking",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE)
public class BookingController {

    private final BookingManager bookingManager;
    private final InputMapper inputMapper = new InputMapper();

    @Autowired
    public BookingController(final BookingManager bookingManager) {
        this.bookingManager = bookingManager;
    }

    @GetMapping(path = "/availableDates")
    public ResponseEntity<Collection<LocalDate>> getAvailableDates(
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<LocalDate> startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<LocalDate> endDate) {
        return ResponseEntity.ok(bookingManager.availableDates(startDate, endDate));
    }

    @PutMapping(path = "/book")
    public ResponseEntity<UUID> book(
            @RequestBody(required = true) BookingDto booking) {
        final BookingRequest input = inputMapper.map(booking);
        return ResponseEntity.ok(bookingManager.book(input));
    }

    @DeleteMapping(path = "/{bookingId}")
    @ResponseStatus(value = HttpStatus.OK)
    public void cancel(@PathVariable(name = "bookingId", required = true) String bookingId) {
        final UUID id = inputMapper.mapBookingId(bookingId);
        bookingManager.cancel(id);
    }
}

