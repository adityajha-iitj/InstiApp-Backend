package in.ac.iitj.instiapp.database.entities.Scheduling.Buses;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BusOverride {

    @jakarta.persistence.Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;

    @ManyToOne
    private BusRun busRun;

    private Date overrideDate;

    private String description;
}
