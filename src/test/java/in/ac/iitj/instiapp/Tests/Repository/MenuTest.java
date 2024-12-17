package in.ac.iitj.instiapp.Tests.Repository;

import in.ac.iitj.instiapp.Repository.MessRepository;
import in.ac.iitj.instiapp.Repository.impl.MessRepositoryImpl;
import in.ac.iitj.instiapp.Tests.EntityTestData.MenuItemData;
import in.ac.iitj.instiapp.database.entities.Scheduling.MessMenu.MenuItem;
import in.ac.iitj.instiapp.database.entities.Scheduling.MessMenu.MenuOverride;
import in.ac.iitj.instiapp.database.entities.Scheduling.MessMenu.MessMenu;
import jakarta.persistence.NoResultException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static in.ac.iitj.instiapp.Tests.EntityTestData.MenuData.*;
import static in.ac.iitj.instiapp.Tests.EntityTestData.OverrideMenudata.MESS_OVERRIDE_DATA;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({MessRepositoryImpl.class})
@Rollback(value = false)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //  Cannot be removed as the repository uses postgres specific schema for queries
public class MenuTest {


    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    @Autowired
    MessRepository messRepository;

    @BeforeAll
    public static void setUp(@Autowired MessRepository messRepository) throws ParseException {

        MessMenu menu1 = MENU_DATA1.toEntity();
        MessMenu menu2 = MENU_DATA2.toEntity();

        messRepository.saveMessMenu(menu1);
        messRepository.saveMessMenu(menu2);

        MenuOverride menuoverride1 = MESS_OVERRIDE_DATA.toEntity();

        messRepository.saveOverrideMessMenu(menuoverride1);
    }

    @Test
    @Order(1)
    public void testMessMenuExists() {
        Assertions.assertThat(messRepository.messMenuExists(2024, 6, 6)).isTrue();
        Assertions.assertThat(messRepository.messMenuExists(2025, 6, 2)).isFalse();
    }

    @Test
    @Order(2)
    public void testMenuOverrideExists() throws ParseException {


        Date date1 = dateFormat.parse("2024/06/06");
        Date date2 = dateFormat.parse("2025/06/05");

        Assertions.assertThat(messRepository.menuOverrideExists(date1)).isTrue();
        Assertions.assertThat(messRepository.menuOverrideExists(date2)).isFalse();
    }

    @Test
    @Order(3)
    public void testGetMessMenu() {

        int year = 2024;
        int month = 6;

        List<MessMenu> result = messRepository.getMessMenu(year, month);

        assertEquals(2, result.size(), "The number of menus returned should match the mock data");

        assertAll(
                () -> assertEquals(2024, result.get(0).getYear()),
                () -> assertEquals(6, result.get(0).getMonth())
        );


    }

    @Test
    @Order(4)
    public void testGetMenuOverride() throws ParseException {

        Date date1 = dateFormat.parse("2024/06/06");
        MenuOverride result = messRepository.getOverrideMessMenu(date1);
        assertNotNull(result, "Result should not be null");
        assertEquals("shamiyana", result.getMenuItem().getDinner());


        Assertions.assertThatThrownBy(() -> messRepository.getOverrideMessMenu(dateFormat.parse("2024/05/05")))
                .isInstanceOf(NoResultException.class);
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
    public void testUpdateMenuOverride() throws ParseException {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

        Date date1 = dateFormat.parse("2024/06/06");
        boolean existsBefore = messRepository.menuOverrideExists(date1);
        assertTrue(existsBefore, "The record should exist before updating");
        MenuItem menuItem = new MenuItem("idli", "biryani", "samosa", "pizza");
        messRepository.updateOverrideMessMenu(menuItem, date1);
        MenuOverride newmenu = messRepository.getOverrideMessMenu(date1);
        assertEquals(menuItem, newmenu.getMenuItem());
    }

    @Test
    @Order(7)
    public void testDeleteOverrideMessMenu() throws Exception {

        Date date = dateFormat.parse("2024/06/06");
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
    @Order(8)
    public void testUpdateMessMenu() {

        MenuItem newMenuItem = new MenuItem("idli", "biryani", "samosa", "pizza");


        boolean existsBefore = messRepository.messMenuExists(2024, 6, 4);  // Assuming 6th December 2024
        assertTrue(existsBefore, "The record should exist before updating");

        messRepository.updateMessMenu(2024, 6, 4, newMenuItem);

        List<MessMenu> updatedMenu = messRepository.getMessMenu(2024, 6);
        assertNotNull(updatedMenu, "The updated menu should not be null");
        assertEquals("idli", updatedMenu.get(0).getMenuItem().getBreakfast());
        assertEquals("biryani", updatedMenu.get(0).getMenuItem().getLunch());
        assertEquals("samosa", updatedMenu.get(0).getMenuItem().getSnacks());
        assertEquals("pizza", updatedMenu.get(0).getMenuItem().getDinner());
    }


}
