package in.ac.iitj.instiapp.database.entities.Scheduling.Buses;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BusSnippet {

    @Column(nullable = false)
    private Time timeOfDeparture;

    @ManyToOne
    private BusLocation fromLocation;

    @ManyToOne
    private BusLocation toLocation;
}
