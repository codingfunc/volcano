package upgrade.volcano.adapter.cache;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class BookingCache {

    private final Map<UUID, List<LocalDate>> bookingIdDatesCache = new ConcurrentHashMap<>();
    private final Map<LocalDate, UUID> datesIdCache = new ConcurrentHashMap<>();
    private LocalDate startDate;

    public void refresh(List<LocalDate> bookings){

    }


}
