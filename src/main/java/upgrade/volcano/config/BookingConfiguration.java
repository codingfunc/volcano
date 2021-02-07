package upgrade.volcano.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import upgrade.volcano.adapter.cache.BookingCache;
import upgrade.volcano.adapter.postgres.BookingRepositoryImpl;
import upgrade.volcano.adapter.postgres.jpa.BookingJpaRepository;
import upgrade.volcano.adapter.validation.BookingValidatorImpl;
import upgrade.volcano.domain.BookingManager;
import upgrade.volcano.domain.BookingManagerImpl;
import upgrade.volcano.domain.BookingRepository;
import upgrade.volcano.domain.BookingValidator;

@Configuration
public class BookingConfiguration {

    @Value("${booking.duration.max}")
    private Integer bookingMaxDuration;

    @Value("${booking.advance.min}")
    private Long bookingMinDaysInAdvance;

    @Value("${booking.advance.max}")
    private Long bookingMaxDaysInAdvance;

    @Autowired
    private BookingCache bookingCache;

    @Autowired
    private BookingJpaRepository bookingJpaRepository;

    @Bean
    public BookingValidator bookingValidator() {
        return new BookingValidatorImpl(bookingMaxDuration, bookingMinDaysInAdvance, bookingMaxDaysInAdvance);
    }


    @Bean
    public BookingRepository bookingRepository() {
        return new BookingRepositoryImpl(bookingJpaRepository, bookingCache);
    }

    @Bean
    public BookingManager bookingManager() {
        return new BookingManagerImpl(bookingRepository(), bookingValidator());
    }
}
