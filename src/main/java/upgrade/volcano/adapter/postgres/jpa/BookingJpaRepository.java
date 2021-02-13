package upgrade.volcano.adapter.postgres.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import upgrade.volcano.adapter.postgres.entity.BookingEntity;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface BookingJpaRepository extends JpaRepository<BookingEntity, UUID> {

    Optional<BookingEntity> findOptionalByBookingId(String bookingId);

    Set<BookingEntity> findByIsCancelledFalseAndStartDateBetween(LocalDate startDate, LocalDate endDate);
}
