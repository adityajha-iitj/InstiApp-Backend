package in.ac.iitj.instiapp.Repository;

import in.ac.iitj.instiapp.database.entities.Scheduling.Calendar.Events;
import in.ac.iitj.instiapp.database.entities.User.User;
import org.springframework.data.domain.Pageable;

import java.sql.Time;
import java.util.List;

public interface EventsRepository {
    void saveEvent(Events event);
    Events getEventById(Long id);
    List<Events> getEventsByUser(User user);
    List<Events> getEventsByDate(Integer date);
    List<Events> getEventsByTimeRange(Integer date, Time startTime, Time endTime);
    void updateEvent(Events event);
    void deleteEvent(Long id);
    boolean existsEventByTitle(String title);
    List<Events> getPaginatedEvents(Pageable pageable);
    List<Events> getRecurringEvents();
    void deleteEventsByUser(User user);
    Long countEventsByUser(User user);
}