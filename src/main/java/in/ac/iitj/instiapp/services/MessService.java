package in.ac.iitj.instiapp.services;

import in.ac.iitj.instiapp.database.entities.Scheduling.MessMenu.MenuItem;
import in.ac.iitj.instiapp.database.entities.Scheduling.MessMenu.MenuOverride;
import in.ac.iitj.instiapp.database.entities.Scheduling.MessMenu.MessMenu;
import in.ac.iitj.instiapp.payload.Scheduling.MessMenu.MenuOverrideDto;
import in.ac.iitj.instiapp.payload.Scheduling.MessMenu.MessMenuDto;

import java.util.Date;
import java.util.List;

public interface MessService {

/*----------------------------------------------MESS MENU-------------------------------------------------------------*/
    /**
     * @param menu
     * @Assumptions MessMenuDto object is correctly received from the controller layer
     */
    void saveMessMenu(MessMenuDto menu);

    /**
     * @param year
     * @param month
     * @return List of MessMEnuDto objects from the repository layer which is as it is passed to the controller layer
     */
    List<MessMenuDto> getMessMenu(int year, int month);

    /**
     * @param year
     * @param month
     * @param day
     * @return True if the menu for that daya month and year exist else false
     */
    boolean messMenuExists(int year, int month, int day);

    /**
     * @param year
     * @param month
     * @param day
     * @param menuItem
     */
    void updateMessMenu(int year, int month, int day, MenuItem menuItem);

    /**
     * @param year
     * @param month
     * @param day
     */
    void deleteMessMenu(int year, int month, int day);

/*---------------------------------------------MENU OVERRIDE----------------------------------------------------------*/

    /**
     * @param menuOverride
     * @Assumptions MenuOverrrideDto objects is correctly received form the controller layer
     */
    void saveOverrideMessMenu(MenuOverrideDto menuOverride);

    /**
     * @param date
     * @return Returns a MenuOverrideDto object if menu override for that date exists
     */
    MenuOverrideDto getOverrideMessMenu(Date date);

    /**
     * @param date
     * @return True if menu override for that day exist else false
     */
    boolean menuOverrideExists(Date date);

    /**
     * @param date
     */
    void deleteOverrideMessMenu(Date date);

    /**
     * @param menuItem
     * @param date
     */
    void updateOverrideMessMenu(MenuItem menuItem, Date date);
}