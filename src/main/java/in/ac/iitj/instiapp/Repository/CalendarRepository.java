package in.ac.iitj.instiapp.Repository;

import in.ac.iitj.instiapp.database.entities.Scheduling.Calendar.Calendar;

public interface CalendarRepository {



    Long save(Calendar calendar);



    /*Temporary method for testing*/
    Boolean calendarExists(String public_id);


}
