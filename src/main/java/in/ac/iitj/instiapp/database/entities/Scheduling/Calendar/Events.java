package in.ac.iitj.instiapp.database.entities.Scheduling.Calendar;

import in.ac.iitj.instiapp.database.entities.User.User;
import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import java.sql.Time;


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

    @ManyToOne
    @JoinColumn(name = "CalendarId", nullable = false)
    Calendar calendar;

    @ManyToOne
    @JoinColumn(name = "Owner", nullable = false)
    User user;

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
}
