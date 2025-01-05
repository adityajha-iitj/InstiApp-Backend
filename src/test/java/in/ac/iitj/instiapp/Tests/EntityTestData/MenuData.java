//package in.ac.iitj.instiapp.Tests.EntityTestData;
//
//import in.ac.iitj.instiapp.database.entities.Scheduling.MessMenu.MessMenu;
//import in.ac.iitj.instiapp.mappers.MessDtoMapper;
//import in.ac.iitj.instiapp.payload.Scheduling.MessMenu.MessMenuDto;
//
//public enum MenuData {
//
//    MENU1(2024, 6, 6, MenuItemData.MENU_ITEM1),
//    MENU2(2024, 6, 4, MenuItemData.MENU_ITEM2),
//    MENU3(2024,8,2,MenuItemData.MENU_ITEM3),
//    MENU4(2024,9,1,MenuItemData.MENU_ITEM4);
//
//    public final int year;
//    public final int month;
//    public final int day;
//    public final MenuItemData menuItemData;
//
//    MenuData(int year, int month, int day, MenuItemData menuItemData) {
//        this.year = year;
//        this.month = month;
//        this.day = day;
//        this.menuItemData = menuItemData;
//    }
//
//    // Method to get the MenuItem entity for this date
//
//    public MessMenu toEntity(){
//        return  new MessMenu(this.year,this.month,this.day, this.menuItemData.toEntity());
//    }
//
//    public MessMenuDto messMenuDto(){
//        return MessDtoMapper.INSTANCE.messMenuToDto(this.toEntity());
//    }
//
//
//}
