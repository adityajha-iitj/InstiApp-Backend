package in.ac.iitj.instiapp.Repository;

import in.ac.iitj.instiapp.database.entities.Scheduling.MessMenu.*;
import in.ac.iitj.instiapp.database.entities.Scheduling.MessMenu.MenuItem;
import in.ac.iitj.instiapp.payload.Scheduling.MessMenu.MenuOverrideDto;
import in.ac.iitj.instiapp.payload.Scheduling.MessMenu.MessMenuDto;


import java.util.Date;
import java.util.List;



public interface MessRepository {

/*----------------------------------------------MESS MENU-------------------------------------------------------------*/

    /**
     * @param menu
     * @throws org.springframework.dao.DataIntegrityViolationException when already the mess menu of that year day and month exists
     */
    void saveMessMenu(MessMenu menu);

    /**
     * @param year
     * @param month
     * @return List of mess menu items
     */
    List<MessMenuDto> getMessMenu(int year, int month);

    /**
     * @param year
     * @param month
     * @param day
     * @return True if the mess menu for that yaer month and day exists else False
     */
    boolean messMenuExists(int year, int month , int day);

    /**
     * @param year
     * @param month
     * @param day
     * @param menuItem
     * @throws org.springframework.dao.DataIntegrityViolationException if mess menu for that year month and day doesnt exists
     */
    void updateMessMenu(int year , int month  , int day , MenuItem menuItem);

    /**
     * @param year
     * @param month
     * @param day
     */
    void deleteMessMenu(int year, int month, int day);

/*-----------------------------------------MENU OVERRIDE--------------------------------------------------------------*/

    /**
     * @param menuOverride
     * @throws org.springframework.dao.DataIntegrityViolationException mess override menu for that date already exists
     */
    void saveOverrideMessMenu(MenuOverride menuOverride );

    /**
     * @param date
     * @return
     */
    MenuOverrideDto getOverrideMessMenu(Date date);

    /**
     * @param date
     * @return True id mess menu for that date exists else return False
     */
    boolean menuOverrideExists(Date date);

    /**
     * @param menuItem
     * @param date
     * @throws org.springframework.dao.DataIntegrityViolationException if mess menu for that day doesn't exist
     */
    void updateOverrideMessMenu(MenuItem menuItem , Date date);

    /**
     * @param date
     */
    void deleteOverrideMessMenu (Date date);
}
