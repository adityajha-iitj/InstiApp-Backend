package in.ac.iitj.instiapp.database.entities.Scheduling.Buses;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bus_schedule")
public class BusSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;

    @Column( nullable = false, unique = true )
    String busNumber;

    @OneToMany(mappedBy = "busSchedule")
    Set<BusRun> runs;
}
