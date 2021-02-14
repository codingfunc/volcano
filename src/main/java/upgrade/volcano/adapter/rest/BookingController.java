package upgrade.volcano.adapter.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import upgrade.volcano.adapter.rest.dto.BookingDto;
import upgrade.volcano.domain.BookingManager;
import upgrade.volcano.domain.model.Booking;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
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
    @Operation(summary = "Returns available dates for booking",
            responses = {
                    @ApiResponse(description = "List of availabe dates for booking",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class)))
            })
    public ResponseEntity<Collection<LocalDate>> getAvailableDates(
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        return ResponseEntity.ok(bookingManager.findAvailableDates(startDate, endDate));
    }

    @PutMapping(path = "/book")
    @Operation(summary = "Booking request",
            responses = {
                    @ApiResponse(description = "Book new or update an existing reservation",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "200", description = "Booking confirmed"),
                    @ApiResponse(responseCode = "400", description = "Booking not found"),
                    @ApiResponse(responseCode = "409", description = "Booking date conflict"),
                    @ApiResponse(responseCode = "500", description = "An internal error")
            })

    public ResponseEntity<String> book(
            @RequestBody(required = true,
                    content = @Content(
                            schema = @Schema(implementation = BookingDto.class)))
            BookingDto booking) {
        final Booking input = inputMapper.map(booking);
        return ResponseEntity.ok(bookingManager.book(input).toString());
    }

    @DeleteMapping(path = "/{bookingId}")
    @ResponseStatus(value = HttpStatus.OK)
    public void cancel(@PathVariable(name = "bookingId", required = true) String bookingId) {
        final UUID id = inputMapper.mapBookingId(bookingId);
        bookingManager.cancel(id);
    }
}

