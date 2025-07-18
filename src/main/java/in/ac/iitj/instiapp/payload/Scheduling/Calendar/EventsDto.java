package in.ac.iitj.instiapp.payload.Scheduling.Calendar;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;
import java.util.List;

/**
 * DTO for {@link in.ac.iitj.instiapp.database.entities.Scheduling.Calendar.Events}
 */
@Data
@Getter
@Setter
public class EventsDto implements Serializable {
    Long publicId;
    String ownerUsername;
    String title;
    String description;
    java.util.Date date;
    Time startTime;
    Time duration;
    Boolean isAllDay;
    Boolean isRecurring;
    Boolean isHide;
    List<String> eventsMediauRL;

}