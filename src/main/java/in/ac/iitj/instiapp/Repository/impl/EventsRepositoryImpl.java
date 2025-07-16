//package in.ac.iitj.instiapp.Repository.impl;
//
//import in.ac.iitj.instiapp.Repository.EventsRepository;
//import in.ac.iitj.instiapp.database.entities.Scheduling.Calendar.Events;
//import in.ac.iitj.instiapp.database.entities.User.User;
//import in.ac.iitj.instiapp.payload.Scheduling.Calendar.EventsDto;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.NoResultException;
//import jakarta.transaction.Transactional;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.dao.DataIntegrityViolationException;
//import org.springframework.dao.EmptyResultDataAccessException;
//import org.springframework.data.domain.Pageable;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Repository;
//
//import java.sql.Time;
//import java.util.List;
//import java.util.Date;
//
//@Repository
//public class EventsRepositoryImpl implements EventsRepository {
//
//    private static final Logger log = LoggerFactory.getLogger(EventsRepositoryImpl.class);
//
//    private final JdbcTemplate jdbcTemplate;
//    private final EntityManager entityManager;
//
//    public EventsRepositoryImpl(JdbcTemplate jdbcTemplate, EntityManager entityManager) {
//        this.jdbcTemplate = jdbcTemplate;
//        this.entityManager = entityManager;
//    }
//
//    @Override
//    @Transactional
//    public void saveEvent(Events event) {
//        // Check if an event with the same title already exists
//        if (existsEventByTitle(event.getTitle())) {
//            throw new DataIntegrityViolationException("Event with title " + event.getTitle() + " already exists");
//        }
//        entityManager.persist(event);
//    }
//
//    @Override
//    public List<EventsDto> getEventsDtoByUser(User user) {
//        try {
//            return entityManager.createQuery(
//                            "SELECT new in.ac.iitj.instiapp.payload.Scheduling.Calendar.EventsDto( " +
//                                    "    t.calendar.public_id, " +          // Calendar Public ID
//                                    "    t.calendar.user.userName, " +    // Calendar User's Username
//                                    "    t.Title, " +                     // Event Title
//                                    "    t.Description, " +               // Event Description
//                                    "    t.Date, " +                      // Event Date
//                                    "    t.startTime, " +                 // Event Start Time
//                                    "    t.Duration, " +                  // Event Duration
//                                    "    t.isAllDay, " +                  // Is All-Day Event
//                                    "    t.isRecurring, " +               // Is Recurring Event
//                                    "    t.recurrence.Frequency, " +       // Recurrence Frequency
//                                    "    t.recurrence.until, " +           // Recurrence Until
//                                    "    t.recurrence.count, " +           // Recurrence Count
//                                    "    t.recurrence.interval, " +        // Recurrence Interval
//                                    "    t.isHide " +                     // Is Hidden
//                                    ") FROM Events t WHERE t.calendar.user = :user",
//                            EventsDto.class
//                    )
//                    .setParameter("user", user)
//                    .getResultList();
//        } catch (NoResultException e) {
//            //log.error("No events found for user {}", user.getId);
//            return List.of();
//        }
//    }
//
//    @Override
//    public List<EventsDto> getEventsDtoByDate(Date date) {
//        try {
//            return entityManager.createQuery(
//                            "SELECT new in.ac.iitj.instiapp.payload.Scheduling.Calendar.EventsDto( " +
//                                    "    t.calendar.public_id, " +          // Calendar Public ID
//                                    "    t.calendar.user.userName, " +    // Calendar User's Username
//                                    "    t.Title, " +                     // Event Title
//                                    "    t.Description, " +               // Event Description
//                                    "    t.Date, " +                      // Event Date
//                                    "    t.startTime, " +                 // Event Start Time
//                                    "    t.Duration, " +                  // Event Duration
//                                    "    t.isAllDay, " +                  // Is All-Day Event
//                                    "    t.isRecurring, " +               // Is Recurring Event
//                                    "    t.recurrence.Frequency, " +       // Recurrence Frequency
//                                    "    t.recurrence.until, " +           // Recurrence Until
//                                    "    t.recurrence.count, " +           // Recurrence Count
//                                    "    t.recurrence.interval, " +        // Recurrence Interval
//                                    "    t.isHide " +                     // Is Hidden
//                                    ") FROM Events t WHERE t.Date = :date",  // Filter by Date
//                            EventsDto.class
//                    )
//                    .setParameter("date", date)
//                    .getResultList();
//        } catch (NoResultException e) {
//            log.error("No events found for date {}", date);
//            return List.of();
//        }
//    }
//
//
//    @Override
//    public List<EventsDto> getEventsDtoByTimeRange(Integer date, Time startTime, Time endTime) {
//        try {
//            return entityManager.createQuery(
//                            "SELECT new in.ac.iitj.instiapp.payload.Scheduling.Calendar.EventsDto( " +
//                                    "    e.calendar.public_id, " +          // Calendar Public ID
//                                    "    e.calendar.user.userName, " +    // Calendar User's Username
//                                    "    e.Title, " +                     // Event Title
//                                    "    e.Description, " +               // Event Description
//                                    "    e.Date, " +                      // Event Date
//                                    "    e.startTime, " +                 // Event Start Time
//                                    "    e.Duration, " +                  // Event Duration
//                                    "    e.isAllDay, " +                  // Is All-Day Event
//                                    "    e.isRecurring, " +               // Is Recurring Event
//                                    "    e.recurrence.Frequency, " +       // Recurrence Frequency
//                                    "    e.recurrence.until, " +           // Recurrence Until
//                                    "    e.recurrence.count, " +           // Recurrence Count
//                                    "    e.recurrence.interval, " +        // Recurrence Interval
//                                    "    e.isHide " +                     // Is Hidden
//                                    ") FROM Events e WHERE e.Date = :date AND " +
//                                    "((e.startTime BETWEEN :startTime AND :endTime) OR " +
//                                    "(e.startTime + e.Duration BETWEEN :startTime AND :endTime))",
//                            EventsDto.class)
//                    .setParameter("date", date)
//                    .setParameter("startTime", startTime)
//                    .setParameter("endTime", endTime)
//                    .getResultList();
//        } catch (NoResultException e) {
//            log.error("No events found for date {} and time range", date);
//            return List.of();
//        }
//    }
//
//
//    @Override
//    @Transactional
//    public void updateEvent(Events event) {
//        // Ensure the event exists before updating
//        if (entityManager.find(Events.class, event.getId()) == null) {
//            throw new EmptyResultDataAccessException("Event with id " + event.getId() + " not found", 1);
//        }
//        entityManager.merge(event);
//    }
//
//    @Override
//    @Transactional
//    public void deleteEvent(Long id) {
//        Events event = entityManager.find(Events.class, id);
//        if (event == null) {
//            throw new EmptyResultDataAccessException("Event with id " + id + " not found", 1);
//        }
//        entityManager.remove(event);
//    }
//
//    @Override
//    public boolean existsEventByTitle(String title) {
//        try {
//            Long count = entityManager.createQuery(
//                "SELECT COUNT(e) FROM Events e WHERE e.Title = :title", Long.class)
//                .setParameter("title", title)
//                .getSingleResult();
//            return count > 0;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    @Override
//    public List<EventsDto> getPaginatedEventsDto(Pageable pageable) {
//        try {
//            return entityManager.createQuery(
//                            "SELECT new in.ac.iitj.instiapp.payload.Scheduling.Calendar.EventsDto( " +
//                                    "    e.calendar.public_id, " +          // Calendar Public ID
//                                    "    e.calendar.user.userName, " +    // Calendar User's Username
//                                    "    e.Title, " +                     // Event Title
//                                    "    e.Description, " +               // Event Description
//                                    "    e.Date, " +                      // Event Date
//                                    "    e.startTime, " +                 // Event Start Time
//                                    "    e.Duration, " +                  // Event Duration
//                                    "    e.isAllDay, " +                  // Is All-Day Event
//                                    "    e.isRecurring, " +               // Is Recurring Event
//                                    "    e.recurrence.Frequency, " +       // Recurrence Frequency
//                                    "    e.recurrence.until, " +           // Recurrence Until
//                                    "    e.recurrence.count, " +           // Recurrence Count
//                                    "    e.recurrence.interval, " +        // Recurrence Interval
//                                    "    e.isHide " +                     // Is Hidden
//                                    ") FROM Events e", EventsDto.class)
//                    .setFirstResult((int) pageable.getOffset())  // Paginate by offset
//                    .setMaxResults(pageable.getPageSize())      // Set page size
//                    .getResultList();
//        } catch (NoResultException e) {
//            log.error("No paginated events found");
//            return List.of();
//        }
//    }
//
//
//    @Override
//    public List<EventsDto> getRecurringEventsDto() {
//        try {
//            return entityManager.createQuery(
//                            "SELECT new in.ac.iitj.instiapp.payload.Scheduling.Calendar.EventsDto( " +
//                                    "    e.calendar.public_id, " +          // Calendar Public ID
//                                    "    e.calendar.user.userName, " +    // Calendar User's Username
//                                    "    e.Title, " +                     // Event Title
//                                    "    e.Description, " +               // Event Description
//                                    "    e.Date, " +                      // Event Date
//                                    "    e.startTime, " +                 // Event Start Time
//                                    "    e.Duration, " +                  // Event Duration
//                                    "    e.isAllDay, " +                  // Is All-Day Event
//                                    "    e.isRecurring, " +               // Is Recurring Event
//                                    "    e.recurrence.Frequency, " +       // Recurrence Frequency
//                                    "    e.recurrence.until, " +           // Recurrence Until
//                                    "    e.recurrence.count, " +           // Recurrence Count
//                                    "    e.recurrence.interval, " +        // Recurrence Interval
//                                    "    e.isHide " +                     // Is Hidden
//                                    ") FROM Events e WHERE e.isRecurring = true",
//                            EventsDto.class)
//                    .getResultList();
//        } catch (NoResultException e) {
//            log.error("No recurring events found");
//            return List.of();
//        }
//    }
//
//
//    @Override
//    @Transactional
//    public void deleteEventsByUser(User user) {
//        int deletedEvents = entityManager.createQuery(
//            "DELETE FROM Events e WHERE e.user = :user")
//            .setParameter("user", user)
//            .executeUpdate();
//        //log.info("Deleted {} events for user {}", deletedEvents, user.getId());
//    }
//
//    @Override
//    public Long countEventsByUser(User user) {
//        return entityManager.createQuery(
//            "SELECT COUNT(e) FROM Events e WHERE e.user = :user", Long.class)
//            .setParameter("user", user)
//            .getSingleResult();
//    }
//}