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
    ;


    public final  String publicId;




    CalendarData(String publicId) {
        this.publicId = publicId;
    }


    public Calendar toEntity(){
        return new Calendar(publicId);
    }
}
