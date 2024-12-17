package in.ac.iitj.instiapp.Tests.EntityTestData;

import in.ac.iitj.instiapp.database.entities.Scheduling.MessMenu.MessMenu;

public enum MenuData {

    MENU_DATA1(2024, 6, 6, MenuItemData.MENU_ITEM_DATA1),
    MENU_DATA2(2024, 6, 4, MenuItemData.MENU_ITEM_DATA1);

    public final int year;
    public final int month;
    public final int day;
    public final MenuItemData menuItemData;

    MenuData(int year, int month, int day, MenuItemData menuItemData) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.menuItemData = menuItemData;
    }

    // Method to get the MenuItem entity for this date

    public MessMenu toEntity(){
        return  new MessMenu(this.year,this.month,this.day, this.menuItemData.toEntity());
    }


}
