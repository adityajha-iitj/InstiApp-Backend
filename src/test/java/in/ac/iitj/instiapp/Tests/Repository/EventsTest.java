package in.ac.iitj.instiapp.Tests.Repository;

import in.ac.iitj.instiapp.Repository.EventsRepository;
import in.ac.iitj.instiapp.Repository.impl.EventsRepositoryImpl;
import in.ac.iitj.instiapp.database.entities.Scheduling.Calendar.Events;
import in.ac.iitj.instiapp.database.entities.User.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import java.sql.Time;
import java.time.LocalTime;
import java.util.List;

@DataJpaTest
@Import({EventsRepositoryImpl.class})
@Rollback(value = false)
public class EventsTest {

    @Autowired
    private EventsRepository eventsRepository;

    private static User user;

    @BeforeAll
    public static void setUp(@Autowired EventsRepository eventsRepository) {
        // Create and save a user for event association
        user = new User();
        user.setId(1L);
        user.setName("Test User");
        // Save user to repository (you might need to set up user saving logic)
        // eventsRepository.save(user);
    }

    @Test
    @Order(1)
    public void testSaveEvent() {
        Events event = new Events();
        event.setTitle("Test Event");
        event.setUser(user);
        event.setDate(20241216);
        event.setStartTime(Time.valueOf(LocalTime.of(10, 0)));
        event.setDuration(Time.valueOf(LocalTime.of(2, 0)));

        eventsRepository.saveEvent(event);

        Events fetchedEvent = eventsRepository.getEventById(event.getId());
        Assertions.assertThat(fetchedEvent).isNotNull();
        Assertions.assertThat(fetchedEvent.getTitle()).isEqualTo("Test Event");
    }

    @Test
    @Order(2)
    public void testSaveEventWithDuplicateTitle() {
        Events event = new Events();
        event.setTitle("Test Event");
        event.setUser(user);
        event.setDate(20241216);
        event.setStartTime(Time.valueOf(LocalTime.of(10, 0)));
        event.setDuration(Time.valueOf(LocalTime.of(2, 0)));

        Assertions.assertThatThrownBy(() -> eventsRepository.saveEvent(event))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("Event with title Test Event already exists");
    }

    @Test
    @Order(3)
    public void testGetEventById() {
        Events event = eventsRepository.getEventById(1L);
        Assertions.assertThat(event).isNotNull();
    }

    @Test
    @Order(4)
    public void testGetEventsByUser() {
        List<Events> events = eventsRepository.getEventsByUser(user);
        Assertions.assertThat(events).isNotEmpty();
    }

    @Test
    @Order(5)
    public void testGetEventsByDate() {
        List<Events> events = eventsRepository.getEventsByDate(20241216);
        Assertions.assertThat(events).isNotEmpty();
    }

    @Test
    @Order(6)
    public void testGetEventsByTimeRange() {
        Time startTime = Time.valueOf(LocalTime.of(9, 0));
        Time endTime = Time.valueOf(LocalTime.of(12, 0));
        List<Events> events = eventsRepository.getEventsByTimeRange(20241216, startTime, endTime);
        Assertions.assertThat(events).isNotEmpty();
    }

    @Test
    @Order(7)
    public void testUpdateEvent() {
        Events event = eventsRepository.getEventById(1L);
        event.setTitle("Updated Event Title");
        eventsRepository.updateEvent(event);

        Events updatedEvent = eventsRepository.getEventById(1L);
        Assertions.assertThat(updatedEvent.getTitle()).isEqualTo("Updated Event Title");
    }

    @Test
    @Order(8)
    public void testDeleteEvent() {
        eventsRepository.deleteEvent(1L);
        Assertions.assertThatThrownBy(() -> eventsRepository.getEventById(1L))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @Order(9)
    public void testExistsEventByTitle() {
        Assertions.assertThat(eventsRepository.existsEventByTitle("Test Event")).isTrue();
        Assertions.assertThat(eventsRepository.existsEventByTitle("Non-existing Event")).isFalse();
    }

    @Test
    @Order(10)
    public void testGetPaginatedEvents() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Events> events = eventsRepository.getPaginatedEvents(pageable);
        Assertions.assertThat(events).isNotEmpty();
    }

    @Test
    @Order(11)
    public void testGetRecurringEvents() {
        List<Events> recurringEvents = eventsRepository.getRecurringEvents();
        Assertions.assertThat(recurringEvents).isNotEmpty();
    }

    @Test
    @Order(12)
    public void testDeleteEventsByUser() {
        eventsRepository.deleteEventsByUser(user);
        List<Events> events = eventsRepository.getEventsByUser(user);
        Assertions.assertThat(events).isEmpty();
    }

    @Test
    @Order(13)
    public void testCountEventsByUser() {
        Long count = eventsRepository.countEventsByUser(user);
        Assertions.assertThat(count).isEqualTo(0);
    }
}
