package upgrade.volcano.domain.model;

import org.apache.commons.lang3.Validate;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class BookingRequest {
    private final String clientName;
    private final LocalDate startDate;
    private final LocalDate endDate;

    private BookingRequest(final String clientName, final LocalDate startDate, final LocalDate endDate) {
        this.clientName = clientName;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getClientName() {
        return clientName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingRequest booking = (BookingRequest) o;
        return clientName.equals(booking.clientName) && startDate.equals(booking.startDate) && endDate.equals(booking.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientName, startDate, endDate);
    }

    @Override
    public String toString() {
        return "Booking{" +
                ", clientName='" + clientName + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }

    public static class Builder {
        private String clientName;
        private LocalDate startDate;
        private LocalDate endDate;

        public BookingRequest build() {
            Validate.notBlank(clientName);
            Validate.notNull(startDate);
            Validate.notNull(endDate);
            return new BookingRequest(clientName, startDate, endDate);
        }

        public Builder forClient(final String clientName) {
            this.clientName = clientName;
            return this;
        }

        public Builder startingAt(final LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder endingAt(final LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }


    }
}
