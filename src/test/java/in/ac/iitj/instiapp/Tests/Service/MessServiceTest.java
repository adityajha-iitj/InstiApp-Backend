package in.ac.iitj.instiapp.Tests.Service;


import in.ac.iitj.instiapp.Repository.MessRepository;
import in.ac.iitj.instiapp.Repository.impl.MessRepositoryImpl;
import in.ac.iitj.instiapp.payload.Scheduling.MessMenu.MenuOverrideDto;
import in.ac.iitj.instiapp.payload.Scheduling.MessMenu.MessMenuDto;
import in.ac.iitj.instiapp.services.MessService;
import in.ac.iitj.instiapp.services.impl.MessServiceImpl;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static in.ac.iitj.instiapp.Tests.EntityTestData.MenuData.*;
import static in.ac.iitj.instiapp.Tests.EntityTestData.OverrideMenudata.MESS_OVERRIDE1;
import static in.ac.iitj.instiapp.Tests.EntityTestData.OverrideMenudata.MESS_OVERRIDE2;

@DataJpaTest
@Import({MessServiceImpl.class, MessRepositoryImpl.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MessServiceTest {

    @Autowired
    private MessService messService;

    @Autowired  // Add this if you want to mock the repository
    private MessRepository messRepository;


    @BeforeAll
    public static void init(@Autowired MessService messService) {
        messService.saveMessMenu(MENU1.messMenuDto());
        messService.saveMessMenu(MENU2.messMenuDto());
        messService.saveOverrideMessMenu(MESS_OVERRIDE1.toDto());
    }

    @Test
    @Order(1)
    public void testGetMessMenu() {
        List<MessMenuDto> messMenuDtos = messService.getMessMenu(MENU1.year , MENU1.month);
        Assertions.assertEquals(2, messMenuDtos.size());
        Assertions.assertEquals(MENU1.year, messMenuDtos.get(0).getYear());
        Assertions.assertEquals(MENU1.month, messMenuDtos.get(0).getMonth());
        Assertions.assertEquals(MENU1.day, messMenuDtos.get(0).getDay());
        Assertions.assertEquals(MENU1.menuItemData.breakfast , messMenuDtos.get(0).getMenuItemBreakfast());
        Assertions.assertEquals(MENU1.menuItemData.lunch , messMenuDtos.get(0).getMenuItemLunch());

        Assertions.assertEquals(MENU2.year, messMenuDtos.get(1).getYear());
        Assertions.assertEquals(MENU2.month, messMenuDtos.get(1).getMonth());
        Assertions.assertEquals(MENU2.day, messMenuDtos.get(1).getDay());
        Assertions.assertEquals(MENU2.menuItemData.breakfast , messMenuDtos.get(1).getMenuItemBreakfast());
        Assertions.assertEquals(MENU2.menuItemData.lunch , messMenuDtos.get(1).getMenuItemLunch());

    }

    @Test
    @Order(2)
    public void testMessMenuExists(){
        Assertions.assertTrue(messService.messMenuExists(MENU1.year,MENU1.month,MENU1.day));
        Assertions.assertFalse(messService.messMenuExists(MENU3.year,MENU3.month,MENU3.day));
    }

    @Test
    @Order(3)
    public void testUpdateMessMenu(){
        messService.updateMessMenu(MENU1.year,MENU1.month,MENU1.day , MENU3.menuItemData.toEntity());
        List<MessMenuDto> messMenuDtos = messService.getMessMenu(MENU1.year,MENU1.month);
        Assertions.assertEquals(MENU3.menuItemData.breakfast , messMenuDtos.get(1).getMenuItemBreakfast());
        Assertions.assertEquals(MENU3.menuItemData.lunch , messMenuDtos.get(1).getMenuItemLunch());
        Assertions.assertEquals(MENU3.menuItemData.snacks, messMenuDtos.get(1).getMenuItemSnacks());
        Assertions.assertEquals(MENU3.menuItemData.dinner , messMenuDtos.get(1).getMenuItemDinner());
    }

    @Test
    @Order(4)
    public void testDeleteMessMenu(){
        messService.deleteMessMenu(MENU1.year,MENU1.month,MENU1.day);
        Assertions.assertFalse(messService.messMenuExists(MENU1.year,MENU1.month,MENU1.day));
    }

    @Test
    @Order(5)
    public void testGetMenuOverride(){
        MenuOverrideDto menuOverrideDto = messService.getOverrideMessMenu(MESS_OVERRIDE1.date);
        Assertions.assertEquals(MESS_OVERRIDE1.date, menuOverrideDto.getDate());
        Assertions.assertEquals(MESS_OVERRIDE1.menuItemData.breakfast , menuOverrideDto.getMenuItemBreakfast());
        Assertions.assertEquals(MESS_OVERRIDE1.menuItemData.lunch , menuOverrideDto.getMenuItemLunch());
        Assertions.assertEquals(MESS_OVERRIDE1.menuItemData.snacks , menuOverrideDto.getMenuItemSnacks());
        Assertions.assertEquals(MESS_OVERRIDE1.menuItemData.dinner , menuOverrideDto.getMenuItemDinner());
    }

    @Test
    @Order(6)
    public void testMenuOverrideExists(){
        Assertions.assertTrue(messService.menuOverrideExists(MESS_OVERRIDE1.date));
        Assertions.assertFalse(messService.menuOverrideExists(MESS_OVERRIDE2.date));
    }

    @Test
    @Order(7)
    public void testUpdateMenuOverride(){
        messService.updateOverrideMessMenu(MESS_OVERRIDE2.toEntity().getMenuItem(), MESS_OVERRIDE1.date);
        MenuOverrideDto menuOverrideDto = messService.getOverrideMessMenu(MESS_OVERRIDE1.date);
        Assertions.assertEquals(MESS_OVERRIDE2.menuItemData.breakfast , menuOverrideDto.getMenuItemBreakfast());
        Assertions.assertEquals(MESS_OVERRIDE2.menuItemData.lunch , menuOverrideDto.getMenuItemLunch());
        Assertions.assertEquals(MESS_OVERRIDE2.menuItemData.snacks , menuOverrideDto.getMenuItemSnacks());
        Assertions.assertEquals(MESS_OVERRIDE2.menuItemData.dinner , menuOverrideDto.getMenuItemDinner());
    }

    @Test
    @Order(8)
    public void testDeleteMenuOverride(){
        messService.deleteOverrideMessMenu(MESS_OVERRIDE1.date);
        Assertions.assertFalse(messService.menuOverrideExists(MESS_OVERRIDE1.date));
    }

}