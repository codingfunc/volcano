package upgrade.volcano.adapter.postgres;

import org.springframework.stereotype.Component;
import upgrade.volcano.adapter.postgres.entity.BookingEntity;
import upgrade.volcano.domain.model.Booking;

import java.util.UUID;

@Component
public class EntityMapper {

    public BookingEntity map(final Booking booking) {
        BookingEntity entity = new BookingEntity();
        entity.setBookingId(booking.getId().toString());
        entity.setName(booking.getName());
        entity.setEmail(booking.getEmail());
        entity.setArrivalDate(booking.getArrivalDate());
        entity.setDepartureDate(booking.getDepartureDate());
        return entity;
    }

    public Booking map(final BookingEntity entity) {
        return
                Booking.builder().forId(UUID.fromString(entity.getBookingId()))
                        .forClient(entity.getName())
                        .forEmail(entity.getEmail())
                        .arrivingAt(entity.getArrivalDate())
                        .departingAt(entity.getDepartureDate())
                        .build();
    }


    public void updateEntity(final BookingEntity entity, final Booking booking) {
        // will not
        entity.setEmail(booking.getEmail());
        entity.setName(booking.getName());
        entity.setArrivalDate(booking.getArrivalDate());
        entity.setDepartureDate(booking.getDepartureDate());
    }

}
