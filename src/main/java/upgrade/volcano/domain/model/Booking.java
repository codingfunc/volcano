package upgrade.volcano.domain.model;

import org.apache.commons.lang3.Validate;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class Booking {
    private Optional<UUID> id;
    private final String name;
    private final String email;
    private final LocalDate arrivalDate;
    private final LocalDate departureDate;

    private Booking(final UUID id, final String name, final String email, final LocalDate arrivalDate, final LocalDate departureDate) {
        this.id = Optional.ofNullable(id);
        this.name = name;
        this.email = email;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return id.equals(booking.id) && name.equals(booking.name) && email.equals(booking.email) && arrivalDate.equals(booking.arrivalDate) && departureDate.equals(booking.departureDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, arrivalDate, departureDate);
    }

    public void setId(final UUID id){
        this.id = Optional.of(id);
    }
    public UUID getId() {
        return id.orElse(null);
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", arrivalDate=" + arrivalDate +
                ", departureDate=" + departureDate +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private String name;
        private String email;
        private LocalDate arrivalDate;
        private LocalDate departureDate;

        public Booking build() {
            Validate.notBlank(name);
            Validate.notBlank(name);
            Validate.notNull(arrivalDate);
            Validate.notNull(departureDate);
            return new Booking(id, name, email, arrivalDate, departureDate);
        }

        public Builder forClient(final String clientName) {
            this.name = clientName;
            return this;
        }

        public Builder forEmail(final String email) {
            this.email = email;
            return this;
        }

        public Builder forId(final UUID id) {
            this.id = id;
            return this;
        }

        public Builder arrivingAt(final LocalDate arrivalDate) {
            this.arrivalDate = arrivalDate;
            return this;
        }

        public Builder departingAt(final LocalDate departureDate) {
            this.departureDate = departureDate;
            return this;
        }
    }
}
