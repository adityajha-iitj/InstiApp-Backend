package in.ac.iitj.instiapp.services;

import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.BusLocation;
import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.BusRun;
import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.BusSchedule;
import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.ScheduleType;
import org.springframework.data.domain.Pageable;

import java.sql.Time;
import java.util.List;

public interface BusService {
    void createBusLocation(String name);
    void removeBusLocation(String name);
    BusLocation findBusLocation(String name);
    List<String> listBusLocations(Pageable pageable);
    boolean checkBusLocationExists(String name);

    void createBusSchedule(BusSchedule busSchedule);
    BusSchedule findBusSchedule(String busNumber);
    List<BusSchedule> listBusSchedules(Pageable pageable);

    void createBusRun(BusRun busRun);
    void updateBusScheduleRun(String busNumber, ScheduleType scheduleType, Time timeOfDeparture, BusRun busRun);

    boolean checkBusScheduleExists(String busNumber);
    void removeBusSchedule(String busNumber);
    void updateBusScheduleNumber(String oldBusNumber, String newBusNumber);
}