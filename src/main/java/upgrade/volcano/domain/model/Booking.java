package upgrade.volcano.domain.model;

import org.apache.commons.lang3.Validate;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class Booking {
    private final Optional<UUID> id;
    private final String name;
    private final String email;
    private final LocalDate startDate;
    private final LocalDate endDate;

    private Booking(final UUID id, final String name, final String email, final LocalDate startDate, final LocalDate endDate) {
        this.id = Optional.ofNullable(id);
        this.name = name;
        this.email = email;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return id.equals(booking.id) && name.equals(booking.name) && email.equals(booking.email) && startDate.equals(booking.startDate) && endDate.equals(booking.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, startDate, endDate);
    }

    public boolean isNew() {
        return id.isEmpty();
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
                ", name='" + name + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private String name;
        private String email;
        private LocalDate startDate;
        private LocalDate endDate;

        public Booking build() {
            Validate.notBlank(name);
            Validate.notBlank(name);
            Validate.notNull(startDate);
            Validate.notNull(endDate);
            return new Booking(id, name, email, startDate, endDate);
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
