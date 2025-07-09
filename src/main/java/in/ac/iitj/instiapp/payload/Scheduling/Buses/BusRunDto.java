package in.ac.iitj.instiapp.payload.Scheduling.Buses;

import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.ScheduleType;
import lombok.Builder;
import lombok.Setter;
import lombok.Value;
import lombok.With;

import java.io.Serializable;
import java.sql.Time;

/**
 * DTO for {@link in.ac.iitj.instiapp.database.entities.Scheduling.Buses.BusRun}
 */
@Value
@Builder
@With
public class BusRunDto implements Serializable {
    String publicId;
    BusRouteDto route;
    Time startTime;
    ScheduleType scheduleType;
}