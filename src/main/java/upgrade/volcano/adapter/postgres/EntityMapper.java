package upgrade.volcano.adapter.postgres;

import org.springframework.stereotype.Component;
import upgrade.volcano.adapter.postgres.entity.BookingEntity;
import upgrade.volcano.domain.model.Booking;

@Component
public class EntityMapper {

    public BookingEntity map(final Booking booking){
        BookingEntity entity = new BookingEntity();
        entity.setId(booking.getId());
        entity.setEmail(booking.getClientName());
        entity.setStartDate(booking.getStartDate());
        entity.setEndDate(booking.getEndDate());
        return entity;
    }

}
