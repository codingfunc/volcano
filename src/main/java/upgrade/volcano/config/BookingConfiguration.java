package upgrade.volcano.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import upgrade.volcano.adapter.cache.BookingCache;
import upgrade.volcano.adapter.postgres.BookingRepositoryImpl;
import upgrade.volcano.domain.BookingManager;
import upgrade.volcano.domain.BookingManagerImpl;
import upgrade.volcano.domain.BookingRepository;
import upgrade.volcano.domain.BookingValidator;

public class BookingConfiguration {

    @Autowired
    private BookingValidator bookingValidator;

    @Autowired
    private BookingCache bookingCache;

    @Bean
    public BookingRepository bookingRepository() {
        return new BookingRepositoryImpl(bookingCache);
    }
    @Bean
    public BookingManager bookingManager(){
        return new BookingManagerImpl(bookingRepository, bookingValidator);
    }
}
