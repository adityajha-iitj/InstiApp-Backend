package in.ac.iitj.instiapp.payload.Scheduling.Calendar;

import in.ac.iitj.instiapp.database.entities.Scheduling.Calendar.CalendarFrequency;
import lombok.Value;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;
import java.util.Set;

/**
 * DTO for {@link in.ac.iitj.instiapp.database.entities.Scheduling.Calendar.Calendar}
 */
@Value
public class CalendarDto implements Serializable {
    String public_id;
    String userName;
    String userUserName;
    String userEmail;
    Set<CalendarEventsDto> events;

    /**
     * DTO for {@link in.ac.iitj.instiapp.database.entities.Scheduling.Calendar.Events}
     * This DTO is specifically for the Events on the User's Calendar (There is an independant EventsDto for other purposes
     */
    @Value
    public static class CalendarEventsDto implements Serializable {
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
}