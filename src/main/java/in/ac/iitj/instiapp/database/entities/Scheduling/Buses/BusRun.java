package in.ac.iitj.instiapp.database.entities.Scheduling.Buses;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bus_run")
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

   @OneToMany(mappedBy = "busRun",fetch = FetchType.LAZY)
   Set<BusOverride> busOverrideList;

   public BusRun(BusSchedule busSchedule, Time timeOfDeparture, BusLocation fromLocation, BusLocation toLocation,ScheduleType scheduleType) {
       this.busSchedule = busSchedule;
       this.timeOfDeparture = timeOfDeparture;
       this.fromLocation = fromLocation;
       this.toLocation = toLocation;
       this.scheduleType = scheduleType;
   }
}
