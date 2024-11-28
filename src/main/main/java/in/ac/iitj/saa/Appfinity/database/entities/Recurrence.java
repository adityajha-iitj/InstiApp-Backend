package in.ac.iitj.saa.Appfinity.database.entities;

import jakarta.persistence.*;
import lombok.*;


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
    @Column( nullable = false)
    CalendarFrequency Frequency;

    @Column(nullable = false)
    Integer count;

    @Column( nullable = false)
    Integer until;

    @Column( nullable = false)
    Integer interval;

    @Enumerated(EnumType.STRING)
    @Column( nullable = false)
    WeekDay BYDAY;
}
