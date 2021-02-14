package upgrade.volcano.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import upgrade.volcano.adapter.cache.DefaultCache;
import upgrade.volcano.adapter.postgres.BookingRepositoryImpl;
import upgrade.volcano.adapter.postgres.jpa.BookingJpaRepository;
import upgrade.volcano.adapter.validation.BookingValidatorImpl;
import upgrade.volcano.domain.*;
import upgrade.volcano.domain.model.ConstraintsConfig;

@Configuration
public class BookingConfiguration {

    @Value("${booking.duration.max}")
    private Integer bookingMaxDuration;

    @Value("${booking.advance.min}")
    private Integer bookingMinDaysInAdvance;

    @Value("${booking.advance.max}")
    private Integer bookingMaxDaysInAdvance;

    @Autowired
    private BookingJpaRepository bookingJpaRepository;

    @Bean
    public ConstraintsConfig constraintsConfig() {
        return new ConstraintsConfig(bookingMaxDuration, bookingMinDaysInAdvance, bookingMaxDaysInAdvance);
    }

    @Bean
    BookingCache bookingCache() {
        return new DefaultCache(bookingMaxDaysInAdvance);
    }

    @Bean
    public BookingValidator bookingValidator() {
        return new BookingValidatorImpl(constraintsConfig());
    }


    @Bean
    public BookingRepository bookingRepository() {
        return new BookingRepositoryImpl(bookingJpaRepository);
    }

    @Bean
    public BookingManager bookingManager() {
        return new BookingManagerImpl(constraintsConfig(), bookingRepository(), bookingValidator(), bookingCache());
    }
}
