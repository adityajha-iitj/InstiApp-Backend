package in.ac.iitj.instiapp.database.entities.User;

public enum PermissionsData {

    BUS_SCHEDULE("BusSchedule"),
    MENU_SCHEDULE("MenuSchedule"),
    CALENDAR("Calendar");

    public final String name;

    PermissionsData(String name) {this.name = name;}

    public Permissions toEntity() {
        Permissions permissions = new Permissions();
        permissions.setId(null);
        permissions.setPermissionsData(this);
        return permissions;
    }
}
