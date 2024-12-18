package in.ac.iitj.instiapp.payload.Scheduling.Buses;

import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.ScheduleType;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.sql.Time;

/**
 * DTO for {@link in.ac.iitj.instiapp.database.entities.Scheduling.Buses.BusRun}
 */
@Value
public class BusRunDto implements Serializable {
    Time busSnippetTimeOfDeparture;
    String busSnippetFromLocationName;
    String busSnippetToLocationName;
    ScheduleType scheduleType;
}