package upgrade.volcano.adapter.postgres.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import upgrade.volcano.adapter.postgres.entity.BookingEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookingJpaRepository extends JpaRepository<BookingEntity, UUID> {

    BookingEntity findByIdAndEmail(UUID id, String userEmail);
}
