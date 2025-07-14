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
    private Long Id;

    @Column( nullable = false, unique = true , name = "bus_number")
    private String busNumber;

    @OneToMany(mappedBy = "busSchedule",fetch = FetchType.EAGER)
    private Set<BusRun> runs;

//    @OneToMany(mappedBy = "busSchedule", fetch = FetchType.LAZY)
//    Set<BusOverride> busOverrides;

    public BusSchedule(String busNumber) {
        this.busNumber = busNumber;
    }

}
