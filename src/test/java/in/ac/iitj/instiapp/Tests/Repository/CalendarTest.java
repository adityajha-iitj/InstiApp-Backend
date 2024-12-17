package in.ac.iitj.instiapp.Tests.Repository;


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

import static in.ac.iitj.instiapp.Tests.EntityTestData.CalendarData.CALENDAR1;
import static in.ac.iitj.instiapp.Tests.EntityTestData.CalendarData.CALENDAR2;

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
        calendarRepository.save(CALENDAR1.toEntity());
    }

    @Test
    @Order(1)
    public  void testCalendarExists(){
        Assertions.assertThat(calendarRepository.calendarExists(CALENDAR1.publicId)).isTrue();
        Assertions.assertThat(calendarRepository.calendarExists(CALENDAR2.publicId)).isFalse();
    }

}
