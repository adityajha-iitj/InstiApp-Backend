package in.ac.iitj.instiapp.Repository;

import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.BusLocation;
import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.BusRun;
import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.BusSchedule;
import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.ScheduleType;
import in.ac.iitj.instiapp.database.entities.Scheduling.MessMenu.MenuOverride;
import in.ac.iitj.instiapp.database.entities.Scheduling.MessMenu.MessMenu;
import org.springframework.data.domain.Pageable;

import java.sql.Time;
import java.util.Date;
import java.util.List;

public interface BusRepository  {


    void saveBusLocation(String name);
    void deleteBusLocation(String name); // FIXME
    BusLocation getBusLocation(String name);
    List<String> getListOfBusLocations(Pageable pageable);
    boolean isBusLocationExists(String name);


    void saveBusSchedule(BusSchedule busSchedule);
    BusSchedule getBusSchedule(String busNumber);

    void saveBusRun(BusRun busRun);
    List<BusSchedule> getBusSchedules(Pageable pageable);
    void updateBusScheduleRun(String busNumber, ScheduleType scheduleType,Time timeOfDeparture, BusRun busRun);
    boolean existsBusSchedule(String busNumber);
    void deleteBusSchedule(String busNumber);
    void updateBusSchedule(String oldBusNumber,String newBusNumber);


    interface MessRepository {
        void saveMessMenu (MessMenu messMenu);
        List<MessMenu> getMessMenu (int year , int month);
        void overrideMessMenu (MenuOverride menuOverride);
        MenuOverride getMenuOverride (Date date);
        void updateMessMenu (MessMenu messMenu);
        void updateOverrideMessMenu (MenuOverride menuOverride);
    }
}