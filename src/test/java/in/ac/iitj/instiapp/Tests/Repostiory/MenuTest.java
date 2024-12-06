

package in.ac.iitj.instiapp.Tests.Repostiory;

import in.ac.iitj.instiapp.Repository.BusRepository;
import in.ac.iitj.instiapp.Repository.MessRepository;
import in.ac.iitj.instiapp.Repository.impl.BusRepositoryImpl;
import in.ac.iitj.instiapp.Repository.impl.MessRepositoryImpl;
import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.BusRun;
import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.BusSchedule;
import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.ScheduleType;

import in.ac.iitj.instiapp.database.entities.Scheduling.MessMenu.MenuItem;
import in.ac.iitj.instiapp.database.entities.Scheduling.MessMenu.MenuOverride;
import in.ac.iitj.instiapp.database.entities.Scheduling.MessMenu.MessMenu;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@Import({MessRepositoryImpl.class})
@Rollback(value = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MenuTest {

    @Autowired
    MessRepository messRepository;

    @BeforeAll
    public static void setUp( @Autowired MessRepository messRepository ) {

        MessMenu mess = new MessMenu();
        mess.setYear(2024);    // Set the year
        mess.setMonth(6);     // Set the month
        mess.setDay(6);        // Set the day (if required)

        // Create a MenuItem if needed (adjust according to your MenuItem class)
        MenuItem menuItem = new MenuItem();
        menuItem.setBreakfast("poha");
        menuItem.setLunch("rajma");
        menuItem.setSnacks("Samosa");
        menuItem.setDinner("chole bhature");
        // Set properties for menuItem if necessary
        mess.setMenuItem(menuItem);

        MessMenu Menu2 = new MessMenu();
        Menu2.setYear(2024);
        Menu2.setMonth(6);
        Menu2.setDay(7);

        MenuItem menuItem1 = new MenuItem();
        menuItem1.setBreakfast("poha");
        menuItem1.setLunch("rajma");
        menuItem1.setSnacks("Samosa");
        menuItem1.setDinner("chole bhature");

        Menu2.setMenuItem(menuItem1);


        // Save the mess entity to the repository (ensure it's saved for the test)
        messRepository.saveMessMenu(mess);
        messRepository.saveMessMenu(Menu2);

        MenuOverride menuOverride = new MenuOverride();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        try {
            // Parse the string into a Date object
            Date date = sdf.parse("2024-12-06");

            // Now set the Date object into the menuOverride
            menuOverride.setDate(date);
        } catch (Exception e) {
            e.printStackTrace();  // Handle any parsing exceptions
        }

        MenuItem menuItem2= new MenuItem();
        menuItem2.setBreakfast("dosa");
        menuItem2.setLunch("paneer");
        menuItem2.setSnacks("pani puri");
        menuItem2.setDinner("shamiyana");
        menuOverride.setMenuItem(menuItem2);

        messRepository.saveOverrideMessMenu(menuOverride);
    }

    @Test
    @Order(1)
    public void testMessMenuExists(){
        Assertions.assertThat(messRepository.messMenuExists(2024 , 12)).isTrue();
        Assertions.assertThat(messRepository.messMenuExists(2025 ,12)).isFalse();
    }




}
