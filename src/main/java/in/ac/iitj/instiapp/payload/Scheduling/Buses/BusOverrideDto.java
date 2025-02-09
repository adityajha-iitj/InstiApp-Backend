package in.ac.iitj.instiapp.payload.Scheduling.Buses;

import lombok.*;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;


/**
 * DTO for {@link in.ac.iitj.instiapp.database.entities.Scheduling.Buses.BusOverride}
 */
@Value
@AllArgsConstructor
@Setter
@Getter
public class BusOverrideDto implements Serializable {
    String publicId;
    String busScheduleBusNumber;
    Time timeOfDeparture;
    String fromLocationName;
    String toLocationName;
    Date overrideDate;
    String description;
}