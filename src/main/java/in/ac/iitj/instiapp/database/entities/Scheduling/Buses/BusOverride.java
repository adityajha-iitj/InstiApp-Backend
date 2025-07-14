package in.ac.iitj.instiapp.database.entities.Scheduling.Buses;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bus_override")
public class BusOverride {

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;

    @Column(nullable = false,unique = true)
    private String publicId;

    @ManyToOne
    private BusSchedule busSchedule;


    //BusSnippet busSnippet;


    private Date overrideDate;

    private String description;


    public BusOverride(String publicId, Date overrideDate, String description) {
        this.publicId = publicId;
        //this.busSnippet = busSnippet;
        this.overrideDate = overrideDate;
        this.description = description;
    }
}
