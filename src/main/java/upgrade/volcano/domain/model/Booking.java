package upgrade.volcano.domain.model;

import org.apache.commons.lang3.Validate;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class Booking {
    private final Optional<UUID> id;
    private final String clientName;
    private final String email;
    private final LocalDate startDate;
    private final LocalDate endDate;

    private Booking(final UUID id, final String clientName, final String email, final LocalDate startDate, final LocalDate endDate) {
        this.id = Optional.ofNullable(id);
        this.clientName = clientName;
        this.email = email;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return id.equals(booking.id) && clientName.equals(booking.clientName) && email.equals(booking.email) && startDate.equals(booking.startDate) && endDate.equals(booking.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, clientName, email, startDate, endDate);
    }

    public UUID getId() {
        return id.orElse(null);
    }

    public String getClientName() {
        return clientName;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", clientName='" + clientName + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }

    public static Builder builder(){
        return new Builder();
    }
    public static class Builder {
        private UUID id;
        private String clientName;
        private String email;
        private LocalDate startDate;
        private LocalDate endDate;

        public Booking build() {
            Validate.notBlank(clientName);
            Validate.notBlank(clientName);
            Validate.notNull(startDate);
            Validate.notNull(endDate);
            return new Booking(id, clientName, email, startDate, endDate);
        }

        public Builder forClient(final String clientName) {
            this.clientName = clientName;
            return this;
        }

        public Builder forEmail(final String email){
            this.email = email;
            return this;
        }

        public Builder forId(final UUID id) {
            this.id = id;
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
