package in.ac.iitj.instiapp.Tests.EntityTestData;

import in.ac.iitj.instiapp.database.entities.Scheduling.Calendar.Calendar;

public enum CalendarData {

    CALENDAR1("USER1"),
    CALENDAR2("USER2"),
    CALENDAR3("USER3"),
    CALENDAR4("USER4"),// not in database
    CALENDAR5("USER5"),
    CALENDAR6("USER6"),
    CALENDAR7("USER7"),
    CALENDAR8("USER8"),
    CALENDAR9("USER9"),
    CALENDAR10("USER10"),
    CALENDAR11("USER11"),
    CALENDAR12("USER12"),
    CALENDAR13("USER13"),
    CALENDAR14("USER14"),
    CALENDAR15("USER15"),
    CALENDAR16("USER16"),
    ;


    public final  String publicId;




    CalendarData(String publicId) {
        this.publicId = publicId;
    }


    public Calendar toEntity(){
        return new Calendar(publicId);
    }
}
