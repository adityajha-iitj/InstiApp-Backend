package in.ac.iitj.instiapp.Repository;

import in.ac.iitj.instiapp.database.entities.Scheduling.Calendar.Calendar;

public interface CalendarRepository {



   void save(Calendar calendar);



    /*Temporary method for testing*/
    Long calendarExists(String public_id);


}
