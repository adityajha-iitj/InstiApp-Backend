package in.ac.iitj.instiapp.Tests.EntityTestData;

import in.ac.iitj.instiapp.database.entities.Scheduling.Calendar.Calendar;

public enum CalendarData {

    CALENDAR1("USER1"),
    CALENDAR2("USER2"),
    CALENDAR3("USER3"),
    ;


    public final  String publicId;




    CalendarData(String publicId) {
        this.publicId = publicId;
    }


    public Calendar toEntity(){
        return new Calendar(publicId);
    }
}
