package in.ac.iitj.instiapp.database.entities.Scheduling.Buses;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class BusOverride {

    @jakarta.persistence.Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;

    @ManyToOne
    private BusRun busRun;

    private Date overrideDate;

    private String description;
}
