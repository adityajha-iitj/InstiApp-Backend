package in.ac.iitj.instiapp.Repository;

import in.ac.iitj.instiapp.database.entities.Scheduling.Calendar.Events;
import in.ac.iitj.instiapp.database.entities.User.User;
import in.ac.iitj.instiapp.payload.Scheduling.Calendar.EventsDto;
import org.springframework.data.domain.Pageable;

import java.sql.Time;
import java.util.Date;
import java.util.List;

public interface EventsRepository {
    void saveEvent(Events event);
    List<EventsDto> getEventsDtoByUser(User user);
    List<EventsDto> getEventsDtoByDate(Date date);
    List<EventsDto> getEventsDtoByTimeRange(Integer date, Time startTime, Time endTime);
    void updateEvent(Events event);
    void deleteEvent(Long id);
    boolean existsEventByTitle(String title);
    List<EventsDto> getPaginatedEventsDto(Pageable pageable);
    List<EventsDto> getRecurringEventsDto();
    void deleteEventsByUser(User user);
    Long countEventsByUser(User user);
}