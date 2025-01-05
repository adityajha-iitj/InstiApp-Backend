//package in.ac.iitj.instiapp.Tests.Repository;
//
//import in.ac.iitj.instiapp.Repository.EventsRepository;
//import in.ac.iitj.instiapp.Repository.impl.EventsRepositoryImpl;
//import in.ac.iitj.instiapp.database.entities.Scheduling.Calendar.Events;
//import in.ac.iitj.instiapp.database.entities.User.User;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Order;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.dao.DataIntegrityViolationException;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.test.annotation.Rollback;
//import in.ac.iitj.instiapp.payload.Scheduling.Calendar.EventsDto;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//
//
//
//import java.sql.Time;
//import java.time.LocalTime;
//import java.util.List;
//
//@DataJpaTest
//@Import({EventsRepositoryImpl.class})
//@Rollback(value = false)
//public class EventsTest {
//
//    @Autowired
//    private EventsRepository eventsRepository;
//
//    private static User user;
//
//    @BeforeAll
//    public static void setUp(@Autowired EventsRepository eventsRepository) {
//        // Create and save a user for event association
//        user = new User();
//        user.setId(1L);
//        user.setName("Test User");
//        // Save user to repository (you might need to set up user saving logic)
//        // eventsRepository.save(user);
//    }
//
//
//
//    @Test
//    @Order(2)
//    public void testSaveEventWithDuplicateTitle() {
//        Events event = new Events();
//        event.setTitle("Test Event");
//        event.setUser(user);
//        event.setDate(LocalDate.parse("20241216", DateTimeFormatter.BASIC_ISO_DATE));
//        event.setStartTime(Time.valueOf(LocalTime.of(10, 0)));
//        event.setDuration(Time.valueOf(LocalTime.of(2, 0)));
//
//        Assertions.assertThatThrownBy(() -> eventsRepository.saveEvent(event))
//                .isInstanceOf(DataIntegrityViolationException.class)
//                .hasMessageContaining("Event with title Test Event already exists");
//    }
//
//
//    @Test
//    @Order(4)
//    public void testGetEventsByUser() {
//        List<EventsDto> events = eventsRepository.getEventsDtoByUser(user);
//        Assertions.assertThat(events).isNotEmpty();
//    }
//
//    @Test
//    @Order(5)
//    public void testGetEventsByDate() {
//        List<Events> events = eventsRepository.getEventsDtoByDate(12:1:2004);
//        Assertions.assertThat(events).isNotEmpty();
//    }
//
//    @Test
//    @Order(6)
//    public void testGetEventsByTimeRange() {
//        Time startTime = Time.valueOf(LocalTime.of(9, 0));
//        Time endTime = Time.valueOf(LocalTime.of(12, 0));
//        List<EventsDto> events = eventsRepository.getEventsDtoByTimeRange(20241216, startTime, endTime);
//        Assertions.assertThat(events).isNotEmpty();
//    }
//
//    @Test
//    @Order(9)
//    public void testExistsEventByTitle() {
//        Assertions.assertThat(eventsRepository.existsEventByTitle("Test Event")).isTrue();
//        Assertions.assertThat(eventsRepository.existsEventByTitle("Non-existing Event")).isFalse();
//    }
//
//    @Test
//    @Order(10)
//    public void testGetPaginatedEvents() {
//        Pageable pageable = PageRequest.of(0, 10);
//        List<EventsDto> events = eventsRepository.getPaginatedEventsDto(pageable);
//        Assertions.assertThat(events).isNotEmpty();
//    }
//
//    @Test
//    @Order(11)
//    public void testGetRecurringEvents() {
//        List<EventsDto> recurringEvents = eventsRepository.getRecurringEventsDto();
//        Assertions.assertThat(recurringEvents).isNotEmpty();
//    }
//
//    @Test
//    @Order(12)
//    public void testDeleteEventsByUser() {
//        eventsRepository.deleteEventsByUser(user);
//        List<EventsDto> events = eventsRepository.getEventsDtoByUser(user);
//        Assertions.assertThat(events).isEmpty();
//    }
//
//    @Test
//    @Order(13)
//    public void testCountEventsByUser() {
//        Long count = eventsRepository.countEventsByUser(user);
//        Assertions.assertThat(count).isEqualTo(0);
//    }
//}
