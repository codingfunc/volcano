package upgrade.volcano.adapter.postgres;

import org.springframework.stereotype.Component;
import upgrade.volcano.adapter.postgres.entity.BookingEntity;
import upgrade.volcano.domain.model.Booking;

@Component
public class EntityMapper {

    public BookingEntity map(final Booking booking) {
        BookingEntity entity = new BookingEntity();
        entity.setId(booking.getId());
        entity.setName(booking.getName());
        entity.setEmail(booking.getEmail());
        entity.setStartDate(booking.getStartDate());
        entity.setEndDate(booking.getEndDate());
        return entity;
    }

    public void populateEntity(final BookingEntity entity, final Booking booking) {
        entity.setName(booking.getName());
        entity.setStartDate(booking.getStartDate());
        entity.setEndDate(booking.getEndDate());
    }

}
