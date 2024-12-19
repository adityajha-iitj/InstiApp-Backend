package in.ac.iitj.instiapp.payload.Scheduling.Buses;

import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.ScheduleType;
import lombok.Setter;
import lombok.Value;

import java.io.Serializable;
import java.sql.Time;

/**
 * DTO for {@link in.ac.iitj.instiapp.database.entities.Scheduling.Buses.BusRun}
 */
@Value
@Setter
public class BusRunDto implements Serializable {
    String publicId;
    Time timeOfDeparture;
    String fromLocationName;
    String toLocationName;
    ScheduleType scheduleType;
}