package upgrade.volcano.adapter.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import upgrade.volcano.adapter.rest.dto.BookingRequest;
import upgrade.volcano.domain.BookingManager;
import upgrade.volcano.domain.model.Booking;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
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
    @Operation(summary = "Returns available dates for booking",
            responses = {
                    @ApiResponse(description = "List of availabe dates for booking",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class)))
            })
    public ResponseEntity<Collection<String>> getAvailableDates(
            @RequestParam(value = "startDate", required = false) String start,
            @RequestParam(value = "endDate", required = false) String end) {
        LocalDate startDate = StringUtils.isNotBlank(start) ? inputMapper.mapDate(start) : null;
        LocalDate endDate = StringUtils.isNotBlank(end) ? inputMapper.mapDate(end) : null;
        Set<LocalDate> availableDates = bookingManager.findAvailableDates(startDate, endDate);
        log.debug("available dates {}", availableDates);
        return ResponseEntity.ok(availableDates.stream().map(LocalDate::toString).sorted().collect(Collectors.toList()));
    }

    @PutMapping(path = "/book")
    @Operation(summary = "Booking request",
            responses = {
                    @ApiResponse(description = "Book new or update an existing reservation",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
                    @ApiResponse(responseCode = "201", description = "Booking created"),
                    @ApiResponse(responseCode = "200", description = "Booking updated"),
                    @ApiResponse(responseCode = "400", description = "Booking not found"),
                    @ApiResponse(responseCode = "409", description = "Booking date conflict"),
                    @ApiResponse(responseCode = "500", description = "An internal error")
            })
    public ResponseEntity book(@RequestBody BookingRequest booking) {
        final Booking input = inputMapper.map(booking);
        final var bookingId = bookingManager.book(input);
        log.debug("booking request {}", booking);

        return Objects.isNull(booking.getBookingId())
                ? new ResponseEntity(bookingId, HttpStatus.CREATED) : ResponseEntity.ok(bookingId);
    }

    @DeleteMapping(path = "/{bookingId}")
    @ResponseStatus(value = HttpStatus.OK)
    public void cancel(@PathVariable(name = "bookingId", required = true) String bookingId) {
        final UUID id = inputMapper.mapBookingId(bookingId);
        bookingManager.cancel(id);
        log.debug("deleted booking {}", bookingId);
    }
}

