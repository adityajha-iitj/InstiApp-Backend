package in.ac.iitj.instiapp.Repository;

import in.ac.iitj.instiapp.database.entities.Scheduling.Calendar.Events;
import in.ac.iitj.instiapp.database.entities.User.User;
import in.ac.iitj.instiapp.payload.Scheduling.Calendar.EventsDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Time;
import java.util.Date;
import java.util.List;

public interface EventsRepository extends JpaRepository<Events, Long> {


//    void saveEvent(Events event);
//    List<EventsDto> getEventsDtoByUser(User user);
//    List<EventsDto> getEventsDtoByDate(Date date);
//    List<EventsDto> getEventsDtoByTimeRange(Integer date, Time startTime, Time endTime);
//    void updateEvent(Events event);
//    void deleteEvent(Long id);
//    boolean existsEventByTitle(String title);
//    List<EventsDto> getPaginatedEventsDto(Pageable pageable);
//    List<EventsDto> getRecurringEventsDto();
//    void deleteEventsByUser(User user);
//    Long countEventsByUser(User user);

    @Query("SELECT e FROM Events e WHERE e.publicId = :publicId")
    public Events findByEventByPublicId(Long publicId);

    @Query("SELECT m.publicUrl FROM Events e JOIN e.media m WHERE e.publicId = :publicId")
    public List<String> getEventMediaUrl(Long publicId);
}