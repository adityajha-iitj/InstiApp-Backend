package in.ac.iitj.instiapp.database.entities.Scheduling.Buses;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BusRun {

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne
    private BusSchedule busSchedule;

    @Column(nullable = false)
    private Time timeOfDeparture;

    @ManyToOne
    private BusLocation fromLocation;

    @ManyToOne
    private BusLocation toLocation;

    @Enumerated(EnumType.STRING)
    private ScheduleType scheduleType;

   @OneToMany(mappedBy = "busRun")
   List<BusOverride> busOverrideList;
}
