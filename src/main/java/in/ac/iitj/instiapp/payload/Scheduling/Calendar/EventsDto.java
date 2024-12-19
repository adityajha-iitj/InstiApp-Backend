package in.ac.iitj.instiapp.payload.Scheduling.Calendar;

import in.ac.iitj.instiapp.database.entities.Scheduling.Calendar.CalendarFrequency;
import lombok.Value;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

/**
 * DTO for {@link in.ac.iitj.instiapp.database.entities.Scheduling.Calendar.Events}
 * DTO for accessing events generally, like in an All Events page etc..
 */
@Value
public class EventsDto implements Serializable {
    String calendarPublic_id;
    String calendarUserUserName;
    String Title;
    String Description;
    Integer Date;
    Time startTime;
    Time Duration;
    Boolean isAllDay;
    Boolean isRecurring;
    CalendarFrequency recurrenceFrequency;
    java.util.Date recurrenceUntil;
    Integer recurrenceCount;
    Integer recurrenceInterval;
    Boolean isHide;
}