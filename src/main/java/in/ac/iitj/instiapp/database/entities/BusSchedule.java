package in.ac.iitj.instiapp.database.entities;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "busSchedule")
public class BusSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;

    @Column( nullable = false)
    Integer busNumber;

    @Column( nullable = false)
    Integer dayOfWeek;

    @ManyToOne
    @JoinColumn(name = "fromLocation", nullable = false)
    BusLocation fromLocation;

    @ManyToOne
    @JoinColumn(name = "toLocation", nullable = false)
    BusLocation toLocation;

    @Column( nullable = false)
    Time timeOfDeparture;

    @Column( nullable = false)
    Boolean BusStatus;

}
