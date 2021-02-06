package upgrade.volcano.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import upgrade.volcano.domain.BookingManager;
import upgrade.volcano.domain.BookingManagerImpl;
import upgrade.volcano.domain.BookingRepository;

public class BookingConfiguration {

    @Autowired
    private BookingRepository bookingRepository;

    @Bean
    public BookingManager bookingManager(){
        return new BookingManagerImpl(bookingRepository);
    }
}
