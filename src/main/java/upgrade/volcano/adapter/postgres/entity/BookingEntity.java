package upgrade.volcano.adapter.postgres.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "booking")

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingEntity {

    @Id
    private UUID id;

    @Column(name="user_name")
    private String userName;

    @Column(name="user_email")
    private String email;

    @Column(name="start_date")
    private LocalDate startDate;

    @Column(name="end_date")
    private LocalDate endDate;

    @Column(name="is_cancelled")
    private Boolean isCancelled;

    @Column(name="created_on")
    private LocalDateTime createdOn;

    @Column(name="last_modified")
    private LocalDateTime lastModified;

}

