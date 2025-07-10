package in.ac.iitj.instiapp.services.impl;

import in.ac.iitj.instiapp.Repository.BusRepository;
import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.*;
import in.ac.iitj.instiapp.mappers.Scheduling.Buses.BusOverrideDtoMapper;
import in.ac.iitj.instiapp.mappers.Scheduling.Buses.BusRouteDtoMapper;
import in.ac.iitj.instiapp.mappers.Scheduling.Buses.BusRunDtoMapper;
import in.ac.iitj.instiapp.mappers.Scheduling.Buses.RouteStopDtoMapper;
import in.ac.iitj.instiapp.payload.Scheduling.Buses.*;
import in.ac.iitj.instiapp.services.BusService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BusServiceImpl implements BusService {

    private final BusRepository busRepository;
    private final BusRouteDtoMapper busRouteDtoMapper;
    private final BusOverrideDtoMapper busOverrideDtoMapper;
    private final RouteStopDtoMapper routeStopDtoMapper;
    private final BusRunDtoMapper busRunDtoMapper;

    @Autowired
    public BusServiceImpl(BusRepository busRepository,
                          BusRouteDtoMapper busRouteDtoMapper,
                          BusOverrideDtoMapper busOverrideDtoMapper,
                          RouteStopDtoMapper routeStopDtoMapper,
                          BusRunDtoMapper busRunDtoMapper) {
        this.busRepository = busRepository;
        this.busRouteDtoMapper = busRouteDtoMapper;
        this.busOverrideDtoMapper = busOverrideDtoMapper;
        this.routeStopDtoMapper = routeStopDtoMapper;
        this.busRunDtoMapper = busRunDtoMapper;
    }

    // Bus Location

    @Override
    @Transactional
    public void saveBusLocation(String name) { busRepository.saveBusLocation(name); }

    @Override
    public List<String> getBusLocations(Pageable pageable) { return busRepository.getListOfBusLocations(pageable); }

    @Override
    public Long isBusLocationExist(String name) { return busRepository.isBusLocationExists(name); }

    @Override
    public void updateBusLocation(String oldName, String newName) { busRepository.updateBusLocation(oldName, newName); }

    @Override
    @Transactional
    public void deleteBusLocation(String name) { busRepository.deleteBusLocation(name); }

    // Bus Schedule

    @Override
    @Transactional
    public void saveBusSchedule(String busNumber) { busRepository.saveBusSchedule(busNumber); }

    @Override
    public BusScheduleDto getBusSchedule(String busNumber) { return busRepository.getBusSchedule(busNumber); }

    @Override
    public List<String> getBusNumbers(Pageable pageable) { return busRepository.getBusNumbers(pageable); }

    @Override
    public Long existsBusSchedule(String busNumber) { return busRepository.existsBusSchedule(busNumber); }

    @Override
    public void updateBusSchedule(String oldBusNumber, String newBusNumber) { busRepository.updateBusSchedule(oldBusNumber, newBusNumber); }

    @Override
    public void deleteBusSchedule(String busNumber) { busRepository.deleteBusSchedule(busNumber); }

    // BusRun with Route

    @Override
    public void saveBusRunWithRoute(BusRun busRun) { busRepository.saveBusRunWithRoute(busRun); }

    @Override
    public List<BusRun> getBusRunsForRoute(String busNumber, Long routeId) { return busRepository.getBusRunsForRoute(busNumber, routeId); }

    // BusRoute and RouteStop
    @Override
    @Transactional
    public void saveBusRoute(BusRouteDto busRouteDto) {
        BusRoute busRoute = new BusRoute(busRouteDto.getRouteName());
        busRepository.saveBusRoute(busRoute);

        Long routeId = busRepository.isBusRouteExists(busRouteDto.getRouteName());

        for(int i=0; i<busRouteDto.getStops().size(); i++){
            Long Id = busRepository.isBusLocationExists(busRouteDto.getStops().get(i).getLocationName());
            RouteStop routeStop = routeStopDtoMapper.toEntity(busRouteDto.getStops().get(i));
            routeStop.setLocation(new BusLocation(Id));
            routeStop.setRoute(new BusRoute(routeId));
            busRepository.saveRouteStop(routeStop);
        }
    }

    @Override
    @Transactional
    public BusRouteDto updateBusRoute(Long routeId, BusRouteDto busRouteDto) {
        try {
            BusRoute existingRoute = busRepository.getBusRouteByRouteId(routeId);
            BusRoute updatedRoute = busRouteDtoMapper.toEntity(busRouteDto);
            updatedRoute.setId(existingRoute.getId()); // Preserve the ID
            busRepository.saveBusRoute(updatedRoute); // Using merge through persist
            return busRouteDtoMapper.toDto(updatedRoute);
        } catch (Exception e) {
            return null;
        }
    }


    @Override
    public BusRouteDto getBusRouteByRouteId(Long routeId) {
        try {
            BusRoute route = busRepository.getBusRouteByRouteId(routeId);
            return busRouteDtoMapper.toDto(route);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<BusRouteDto> getAllBusRoutes() {
        List<BusRoute> routes = busRepository.getAllBusRoutes();
        return routes.stream()
                .map(busRouteDtoMapper::toDto)
                .collect(Collectors.toList());
    }


    @Override
    public void saveRouteStop(RouteStop stop) { busRepository.saveRouteStop(stop); }

    @Override
    public List<RouteStop> getRouteStopsByRouteId(Long routeId) { return busRepository.getRouteStopsByRouteId(routeId); }

    // BusOverride (if still needed)

    @Override
    public void saveBusOverride(String busNumber, BusOverride busOverride) { busRepository.saveBusOverride(busNumber, busOverride); }

    @Override
    public boolean existsBusOverrideByPublicId(String publicId) { return busRepository.existsBusOverrideByPublicId(publicId); }

    @Override
    public List<BusOverrideDto> getBusOverrideForYearAndMonth(int year, int month) { return busRepository.getBusOverrideForYearAndMonth(year, month); }

    @Override
    public void updateBusOverride(String publicId, BusOverride newBusOverride) { busRepository.updateBusOverride(publicId, newBusOverride); }

    @Override
    public void deleteBusOverride(List<String> busOverrideIds) { busRepository.deleteBusOverride(busOverrideIds); }

    @Override
    @Transactional
    public RouteStopDto addRouteStop(String routeName, RouteStopDto stopDto) {
        RouteStop stop = routeStopDtoMapper.toEntity(stopDto);
        busRepository.saveRouteStop(stop);
        return routeStopDtoMapper.toDto(stop);
    }

    @Override
    @Transactional
    public BusRunDto createBusRunWithRoute(BusRunDto busRunDto) {
        BusSchedule bus = busRepository.getBusScheduleByBusNumber(busRunDto.getBusNumber());
        if (bus == null) {
            throw new EntityNotFoundException("BusSchedule not found for busNumber: " + busRunDto.getBusNumber());
            // Or return a custom error response if you prefer
        }

        BusRoute route = busRepository.findBusRouteByRouteName(busRunDto.getRoute().getRouteName());
        if (route == null) {
            // Create and save new BusRoute
            route = busRouteDtoMapper.toEntity(busRunDto.getRoute());
            busRepository.saveBusRoute(route); // This assigns an ID

            // Save RouteStops, referencing the now-persisted route
            if (busRunDto.getRoute().getStops() != null) {
                for (RouteStopDto stopDto : busRunDto.getRoute().getStops()) {
                    RouteStop stop = routeStopDtoMapper.toEntity(stopDto);
                    stop.setRoute(route);

                    // Ensure BusLocation exists
                    Long locationId = busRepository.isBusLocationExists(stopDto.getLocationName());
                    BusLocation location;
                    if (locationId == -1L) {
                        // Create new location
                        busRepository.saveBusLocation(stopDto.getLocationName());
                        locationId = busRepository.isBusLocationExists(stopDto.getLocationName());
                    }
                    location = busRepository.getLocationById(locationId);

                    stop.setLocation(location);

                    System.out.println("Saving stop: " + stop + ", location: " + stop.getLocation());
                    if (stop.getLocation() == null) {
                        throw new IllegalStateException("Location is null for stop: " + stopDto.getLocationName());
                    }

                    busRepository.saveRouteStop(stop);
                }
            }
        }

        BusRun busRun = new BusRun();
        busRun.setBusSchedule(bus);
        busRun.setRoute(route);
        busRun.setStartTime(busRunDto.getStartTime());
        busRun.setScheduleType(busRunDto.getScheduleType());

        busRepository.saveBusRunWithRoute(busRun);
        return busRunDtoMapper.toDto(busRun);
    }
}