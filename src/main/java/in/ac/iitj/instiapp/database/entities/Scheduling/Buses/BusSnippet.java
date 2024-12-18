package in.ac.iitj.instiapp.database.entities.Scheduling.Buses;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;

import java.sql.Time;

@Embeddable
public class BusSnippet {

    @Column(nullable = false)
    private Time timeOfDeparture;

    @ManyToOne
    private BusLocation fromLocation;

    @ManyToOne
    private BusLocation toLocation;
}
