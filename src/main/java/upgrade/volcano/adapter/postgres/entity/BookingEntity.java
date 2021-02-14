package upgrade.volcano.adapter.postgres.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.stereotype.Indexed;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;


@Entity
@Table(name = "booking")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingEntity {

    /**
     * Database primary key
     */
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * The domain id.
     */
    @Column(name = "booking_id")
    private String bookingId;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "is_cancelled")
    private Boolean isCancelled;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_on")
    private Date createdOn;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_modified")
    private Date lastModified;

}

