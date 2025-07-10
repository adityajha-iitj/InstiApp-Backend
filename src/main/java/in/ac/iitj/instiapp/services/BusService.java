package in.ac.iitj.instiapp.services;

import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.*;
import in.ac.iitj.instiapp.payload.Scheduling.Buses.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BusService {

/*----------------------------------------------BUS LOCATION----------------------------------------------------------*/
    /**
     * @param name of Location to e inserted
     */
    void saveBusLocation(String name);

    /**
     * @param pageable  firstResult Max Value-INT_MAX,Min Value-0 PageSize-Max value-TODO
     * @return Returns a list od all the bus locations (Pagination implementation)
     */
    List<String> getBusLocations(Pageable pageable);

    /**
     * @param name
     * @return Returns the id of the bus locations else returns -1
     */
    Long isBusLocationExist(String name);

    /**
     * @param oldName
     * @param newName
     * need to implement the cascading effect
     * AutoUpdates the bus run and bus override as the id remains same
     */
    void updateBusLocation(String oldName, String newName);

    /**
     * @param name
     * Delete the bus run and bus override run of that locations IMPLEMENTED IN REPOSITORY
     */
    void deleteBusLocation(String name);

/*--------------------------------------------------BUS SCHEDULE------------------------------------------------------*/
    /**
     * @param busNumber
     */
    void saveBusSchedule(String busNumber);

    /**
     * @param busNumber
     * @return BusScheduleDto form the bus run table
     */
    BusScheduleDto getBusSchedule(String busNumber);

    /**
     * @param pageable
     * @return List of Name of current busses
     */
    List<String> getBusNumbers(Pageable pageable);

    /**
     * @param busNumber
     * @return Returns id if the bus number exists else return -1
     */
    Long existsBusSchedule(String busNumber);

    /**
     * @param oldBusNumber
     * @param newBusNumber
     * Autoupdates the busSchedule in bus override and bus run
     */
    void updateBusSchedule(String oldBusNumber,String newBusNumber);

    /**
     * @param busNumber
     * Deletes BusRun and BusOverrride of that bus number IMPLEMENTED IN REPOSITORY
     */
    void deleteBusSchedule(String busNumber);
/*---------------------------------------------------BUS RUN----------------------------------------------------------*/


    /**
     * @param busRun
     * @Assumtions Assuming the Bus Run object contains the correct name of location and bus run to get refrence from
     */
    void saveBusRunWithRoute(BusRun busRun);

    /**
     * @param busNumber
     * @param routeId
     * @return
     */
    List<BusRun> getBusRunsForRoute(String busNumber, Long routeId);

/*--------------------------------------------BUS OVERRIDE------------------------------------------------------------*/
    /**
     * @param busNumber
     * @param busOverride
     */
    void saveBusOverride(String busNumber, BusOverride busOverride);

    /**
     * @param publicId
     * @return
     */
    boolean existsBusOverrideByPublicId(String publicId);

    /**
     * @param year
     * @param month
     * @return
     */
    List<BusOverrideDto> getBusOverrideForYearAndMonth(int year, int month);

    /**
     * @param publicId
     * @param newBusOverride
     */
    void updateBusOverride(String publicId,BusOverride newBusOverride);

    /**
     * @param busOverrideIds
     */
    void deleteBusOverride(List<String> busOverrideIds);

    // ------------------- BusRoute and RouteStop Operations -------------------
    void saveBusRoute(BusRouteDto busRouteDto);

    BusRouteDto getBusRouteByRouteId(Long routeId);

    List<BusRouteDto> getAllBusRoutes();

    void saveRouteStop(RouteStop stop);

    List<RouteStop> getRouteStopsByRouteId(Long routeId);

    BusRouteDto updateBusRoute(Long routeId, BusRouteDto busRouteDto);

    RouteStopDto addRouteStop(String routeName, RouteStopDto routeStopDto);

    BusRunDto createBusRunWithRoute(BusRunDto busRunDto);
}