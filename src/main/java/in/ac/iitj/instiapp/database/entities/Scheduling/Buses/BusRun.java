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

    @Column(nullable = false,unique = true)
    private String publicId;

    @ManyToOne
    private BusSchedule busSchedule;

    BusSnippet busSnippet;


    @Enumerated(EnumType.STRING)
    private ScheduleType scheduleType;


   public BusRun(String publicId, BusSnippet busSnippet,ScheduleType scheduleType) {
       this.publicId = publicId;
       this.busSnippet = busSnippet;
       this.scheduleType = scheduleType;
   }

}
