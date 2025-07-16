package in.ac.iitj.instiapp.database.entities.Scheduling.Calendar;

import com.nimbusds.openid.connect.sdk.assurance.evidences.Organization;
import in.ac.iitj.instiapp.database.entities.User.Organisation.Organisation;
import in.ac.iitj.instiapp.database.entities.User.User;
import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import java.sql.Time;
import java.util.UUID;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "events")
public class Events {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;

    @Column(nullable = false, unique = true, updatable = false)
    private Long publicId;

    @ManyToOne
    @JoinColumn(name = "CalendarId", nullable = true)
    Calendar calendar;

    @ManyToOne
    @JoinColumn(name = "organisation_id", nullable = false)
    private Organisation organisation;

    @Column( nullable = false)
    String Title;

    @Column( nullable = false)
    String Description;

    @Column( nullable = false)
    Date Date;

    @Column( nullable = false)
    Time startTime;

    @Column( nullable = false)
    Time Duration;

    @Column( nullable = false)
    Boolean isAllDay;

    @Column( nullable = false)
    Boolean isRecurring;

    @ManyToOne
    @JoinColumn(name = "recurrence_rule", nullable = true)
    Recurrence recurrence;

    Boolean isHide;

    @PrePersist
    @PreUpdate
    private void ensurePublicId() {
        // if this is the very first save, Id will be null until after persist,
        // so we use a temporary UUID‐based long. After persist, Id != null,
        // so you could switch to Id if you prefer.
        if (publicId == null) {
            publicId = UUID.randomUUID()
                    .getMostSignificantBits() & Long.MAX_VALUE;
        }
    }

}
