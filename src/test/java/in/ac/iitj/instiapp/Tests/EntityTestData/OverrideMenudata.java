package in.ac.iitj.instiapp.Tests.EntityTestData;

import in.ac.iitj.instiapp.Tests.Utilities.Conversions;
import in.ac.iitj.instiapp.database.entities.Scheduling.MessMenu.MenuOverride;

import java.time.LocalDate;
import java.util.Date;

import static in.ac.iitj.instiapp.Tests.EntityTestData.MenuData.MENU1;
import static in.ac.iitj.instiapp.Tests.EntityTestData.MenuData.MENU2;

public enum OverrideMenudata {

    MESS_OVERRIDE1(Conversions.convertLocalDateToDate(
            LocalDate.of(MENU1.year, MENU1.month, 8)
    ), MenuItemData.MENU_ITEM3),
    MESS_OVERRIDE2(Conversions.convertLocalDateToDate(
            LocalDate.of(MENU2.year, MENU2.month, 6)
    ), MenuItemData.MENU_ITEM4);

    public final Date date;
    public final MenuItemData menuItemData;

    OverrideMenudata(Date date , MenuItemData menuItemData) {
        this.date = date;
        this.menuItemData = menuItemData;
    }

    public MenuOverride toEntity() {
        return new MenuOverride(this.date , this.menuItemData.toEntity());
    }
}
