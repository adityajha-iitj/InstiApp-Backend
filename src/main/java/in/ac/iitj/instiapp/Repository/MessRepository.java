package in.ac.iitj.instiapp.Repository;

import in.ac.iitj.instiapp.database.entities.Scheduling.MessMenu.MenuItem;
import in.ac.iitj.instiapp.database.entities.Scheduling.MessMenu.MenuOverride;
import in.ac.iitj.instiapp.database.entities.Scheduling.MessMenu.MessMenu;


import java.util.Date;
import java.util.List;


public interface MessRepository {
    void saveMessMenu(MessMenu menu);
    void saveOverrideMessMenu(MenuOverride menuOverride );
    List<MessMenu> getMessMenu(int year, int month);
    MenuOverride getOverrideMessMenu(Date date);
    boolean messMenuExists(int year, int month);
    void deleteMessMenu(int year, int month);
    void deleteOverrideMessMenu (Date date);
    boolean menuOverrideExists(Date date);
    void updateMessMenu(int year , int month  , int day , MenuItem menuItem);
    void updateOverrideMessMenu(MenuOverride menuOverride);

}
