package in.ac.iitj.instiapp.database.entities.Scheduling.Calendar;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "recurrence")
public class Recurrence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;

    @Enumerated(EnumType.STRING)
    @Column( nullable = true)
    CalendarFrequency Frequency;

    @Column( nullable = false)
    Date until;

    @Column(nullable = true)
    Integer count;

    @Column( nullable = true)
    Integer interval;

}
