package in.ac.iitj.instiapp.Repository;

import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.*;
import in.ac.iitj.instiapp.payload.Scheduling.Buses.BusOverrideDto;
import in.ac.iitj.instiapp.payload.Scheduling.Buses.BusScheduleDto;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface BusRepository {
    // Bus Location
    void saveBusLocation(String name);
    List<String> getListOfBusLocations(Pageable pageable);
    Long isBusLocationExists(String name);
    void updateBusLocation(String oldName, String newName);
    void deleteBusLocation(String name);
    BusLocation getLocationById(Long id);

    // Bus Schedule
    void saveBusSchedule(String busNumber);
    BusScheduleDto getBusSchedule(String busNumber);
    List<String> getBusNumbers(Pageable pageable);
    Long existsBusSchedule(String busNumber);
    void updateBusSchedule(String oldBusNumber, String newBusNumber);
    void deleteBusSchedule(String busNumber);
    BusSchedule getBusScheduleByBusNumber(String busNumber);

    // BusRun with Route
    void saveBusRunWithRoute(BusRun busRun);
    List<BusRun> getBusRunsForRoute(String busNumber, Long routeId);
    Long isBusRouteExists(String name);
    List<BusRun> getBusRunsByBusNumber(String busNumber);

    // BusRoute and RouteStop
    void saveBusRoute(BusRoute route);
    BusRoute getBusRouteByRouteId(Long routeId);
    List<BusRoute> getAllBusRoutes();
    void saveRouteStop(RouteStop stop);
    List<RouteStop> getRouteStopsByRouteId(Long routeId);
    BusRoute findBusRouteByRouteName(String routeName);

    // BusOverride (if still needed)
    void saveBusOverride(String busNumber, BusOverride busOverride);
    boolean existsBusOverrideByPublicId(String publicId);
    List<BusOverrideDto> getBusOverrideForYearAndMonth(int year, int month);
    void updateBusOverride(String publicId, BusOverride newBusOverride);
    void deleteBusOverride(List<String> busOverrideIds);
}