package in.ac.iitj.instiapp.Tests.Repostiory;


import in.ac.iitj.instiapp.Repository.CalendarRepository;
import in.ac.iitj.instiapp.Repository.impl.CalendarRepositoryImpl;
import in.ac.iitj.instiapp.database.entities.Scheduling.Calendar.Calendar;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@Import({CalendarRepositoryImpl.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Rollback(value = true)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CalendarTest {


    @Autowired
    CalendarRepository calendarRepository;

    @BeforeAll
    public static void setUp(@Autowired CalendarRepository calendarRepository){
        calendarRepository.save(new Calendar("some_random_public_id_based_on_username"));
    }

    @Test
    @Order(1)
    public  void testCalendarExists(){
        Assertions.assertThat(calendarRepository.calendarExists("some_random_public_id_based_on_username")).isTrue();
        Assertions.assertThat(calendarRepository.calendarExists("some_random_public_id_based_on_username_not_in_database")).isFalse();
    }

}
