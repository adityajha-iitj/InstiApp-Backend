package in.ac.iitj.instiapp.Tests.EntityTestData;

import in.ac.iitj.instiapp.database.entities.Scheduling.MessMenu.MenuItem;
import in.ac.iitj.instiapp.database.entities.Scheduling.MessMenu.MenuOverride;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public enum OverrideMenudata {

    MESS_OVERRIDE_DATA(parseDate("2024/06/06") , MenuItemData.MENU_ITEM_DATA2 );

    public final Date date;
    public final MenuItemData menuItemData;

    OverrideMenudata(Date date , MenuItemData menuItemData) {
        this.date = date;
        this.menuItemData = menuItemData;
    }

    // Static method to parse date strings into Date objects
    private static Date parseDate(String dateStr) {
        try {
            return new SimpleDateFormat("yyyy/MM/dd").parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException("Invalid date format: " + dateStr, e);
        }
    }

    public MenuOverride toEntity() {
        return new MenuOverride(this.date , this.menuItemData.toEntity());
    }
}
