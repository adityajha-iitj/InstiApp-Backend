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
@Table(name = "bus_run",
uniqueConstraints = {
@UniqueConstraint(columnNames = {"bus_schedule_id", "route_id"})
    }
)
public class BusRun {

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "bus_schedule_id")
    private BusSchedule busSchedule;

    @ManyToOne
    @JoinColumn(name = "route_id")
    private BusRoute route;

    @Column(nullable = false)
    private Time startTime;

    @Enumerated(EnumType.STRING)
    private ScheduleType scheduleType;

    public BusRun(BusSchedule busSchedule, BusRoute route, Time startTime, ScheduleType scheduleType) {
        this.busSchedule = busSchedule;
        this.route = route;
        this.startTime = startTime;
        this.scheduleType = scheduleType;
    }
}
