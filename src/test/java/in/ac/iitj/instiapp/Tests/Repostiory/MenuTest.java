

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
            Date date = sdf.parse("2024/12/06");

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
        Assertions.assertThat(messRepository.messMenuExists(2024 , 6 , 7)).isTrue();
        Assertions.assertThat(messRepository.messMenuExists(2025 ,12, 7)).isFalse();
    }

    @Test
    @Order(2)
    public void testMenuOverrideExists() throws ParseException {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

        Date date1 = dateFormat.parse("2024/12/06");
        Date date2 = dateFormat.parse("2025/12/05");

        Assertions.assertThat(messRepository.menuOverrideExists(date1)).isTrue();
        Assertions.assertThat(messRepository.menuOverrideExists(date2)).isFalse();
    }

    @Test
    @Order(3)
    public void testGetMessMenu() {

        int year = 2024;
        int month = 6;


        List<MessMenu> result = messRepository.getMessMenu(year, month);


        assertNotNull(result, "Result should not be null");
        assertEquals(2, result.size(), "The number of menus returned should match the mock data");


        assertEquals(2024, result.get(0).getYear());
        assertEquals(6, result.get(0).getMonth());
    }

    @Test
    @Order(4)
    public void testGetMenuOverride() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

        Date date1 = dateFormat.parse("2024/12/06");
        MenuOverride result = messRepository.getOverrideMessMenu(date1);
        assertNotNull(result, "Result should not be null");
        assertEquals("shamiyana", result.getMenuItem().getDinner());
    }

    @Test
    @Order(5)
    public void testDeleteMessMenu() {

        boolean existsBefore = messRepository.messMenuExists(2024, 6, 6);
        assertTrue(existsBefore, "The record should exist before deletion");

        // Act: Call the delete method
        messRepository.deleteMessMenu(2024, 6, 6);

        // Assert: Verify that the record no longer exists after deletion
        boolean existsAfter = messRepository.messMenuExists(2024, 6, 6);
        assertFalse(existsAfter, "The record should be deleted from the table");

    }

    @Test
    @Order(6)
    public void testDeleteOverrideMessMenu() throws Exception {
        // Arrange: Parse the date used in the mock data setup
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse("2024-12-06");

        // Ensure the record exists before deletion
        boolean existsBefore = messRepository.menuOverrideExists(date);
        assertTrue(existsBefore, "The record should exist before deletion");

        // Act: Call the delete method
        messRepository.deleteOverrideMessMenu(date);

        // Assert: Verify that the record no longer exists after deletion
        boolean existsAfter = messRepository.menuOverrideExists(date);
        assertFalse(existsAfter, "The record should be deleted from the table");
    }


    @Test
    @Order(7)
    public void testUpdateMessMenu() {

        MenuItem newMenuItem = new MenuItem();
        newMenuItem.setBreakfast("idli");
        newMenuItem.setLunch("biryani");
        newMenuItem.setSnacks("samosa");
        newMenuItem.setDinner("pizza");


        boolean existsBefore = messRepository.messMenuExists(2024, 6, 7);  // Assuming 6th December 2024
        assertTrue(existsBefore, "The record should exist before updating");

        messRepository.updateMessMenu(2024, 6, 7, newMenuItem);

        List<MessMenu> updatedMenu = messRepository.getMessMenu(2024, 6);
        assertNotNull(updatedMenu, "The updated menu should not be null");
        assertEquals("idli", updatedMenu.get(0).getMenuItem().getBreakfast());
        assertEquals("biryani", updatedMenu.get(0).getMenuItem().getLunch());
        assertEquals("samosa", updatedMenu.get(0).getMenuItem().getSnacks());
        assertEquals("pizza", updatedMenu.get(0).getMenuItem().getDinner());
    }

    @Test
    public void testUpdateMessMenuThrowsExceptionWhenNotExists() {
        // Arrange: Set up mock MenuItem to update
        MenuItem newMenuItem = new MenuItem();
        newMenuItem.setBreakfast("idli");
        newMenuItem.setLunch("biryani");
        newMenuItem.setSnacks("samosa");
        newMenuItem.setDinner("pizza");

        // Ensure the MessMenu does not exist (by using a date that is not in the database)
        boolean existsBefore = messRepository.messMenuExists(2024, 12, 25);  // Assuming 25th December 2024 doesn't exist
        assertFalse(existsBefore, "The record should not exist before updating");

        // Act & Assert: Verify that the exception is thrown when trying to update a non-existing menu
        assertThrows(DataIntegrityViolationException.class, () -> {
            messRepository.updateMessMenu(2024, 12, 25, newMenuItem);
        });
    }

}
