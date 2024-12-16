package in.ac.iitj.instiapp.Tests.Service;

import in.ac.iitj.instiapp.database.entities.Scheduling.MessMenu.MenuItem;
import in.ac.iitj.instiapp.database.entities.Scheduling.MessMenu.MenuOverride;
import in.ac.iitj.instiapp.database.entities.Scheduling.MessMenu.MessMenu;
import in.ac.iitj.instiapp.services.MessService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MessServiceTest {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

    @Autowired
    private MessService messService;

    private Date testDate;
    private MenuItem testMenuItem;

    @BeforeEach
    public void setUp() throws ParseException {
        testDate = dateFormat.parse("2024/06/06");
        testMenuItem = new MenuItem("poha", "rajma", "Samosa", "chole bhature");
    }

    @Test
    @Order(1)
    public void testSaveAndGetMessMenu() {
        MessMenu menu = new MessMenu(2024, 6, 6, testMenuItem);
        messService.saveMessMenu(menu);

        List<MessMenu> retrievedMenus = messService.getMessMenu(2024, 6);
        assertFalse(retrievedMenus.isEmpty());
        assertEquals(menu.getMenuItem(), retrievedMenus.get(0).getMenuItem());
    }

    @Test
    @Order(2)
    public void testSaveAndGetOverrideMenu() throws ParseException {
        MenuOverride menuOverride = new MenuOverride(testDate,
                new MenuItem("dosa", "paneer", "pani puri", "shamiyana"));

        messService.saveOverrideMessMenu(menuOverride);

        MenuOverride retrievedOverride = messService.getOverrideMessMenu(testDate);
        assertNotNull(retrievedOverride);
        assertEquals("shamiyana", retrievedOverride.getMenuItem().getDinner());
    }

    @Test
    @Order(3)
    public void testUpdateMessMenu() {
        MessMenu menu = new MessMenu(2024, 6, 4, testMenuItem);
        messService.saveMessMenu(menu);

        MenuItem updatedMenuItem = new MenuItem("idli", "biryani", "samosa", "pizza");
        messService.updateMessMenu(2024, 6, 4, updatedMenuItem);

        List<MessMenu> updatedMenus = messService.getMessMenu(2024, 6);
        assertEquals(updatedMenuItem, updatedMenus.get(0).getMenuItem());
    }

    @Test
    @Order(4)
    public void testDeleteMessMenu() {
        MessMenu menu = new MessMenu(2024, 6, 4, testMenuItem);
        messService.saveMessMenu(menu);

        assertTrue(messService.messMenuExists(2024, 6, 4));

        messService.deleteMessMenu(2024, 6, 4);

        assertFalse(messService.messMenuExists(2024, 6, 4));
    }

    @Test
    @Order(5)
    public void testGetNonExistentOverrideMenu() throws ParseException {
        Date nonExistentDate = dateFormat.parse("2025/06/05");

        assertThrows(EmptyResultDataAccessException.class, () -> {
            messService.getOverrideMessMenu(nonExistentDate);
        });
    }
}