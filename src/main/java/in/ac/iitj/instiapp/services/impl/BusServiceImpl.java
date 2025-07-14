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
import org.mapstruct.ap.shaded.freemarker.core.NonDateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.hibernate.Hibernate;
import java.util.ArrayList;
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

        Long busRouteId = busRepository.isBusRouteExists(busRunDto.getRoute().getRouteName());
        if (busRouteId != -1) {
            String busNumber = busRepository.findBusFromBusRoute(busRunDto.getRoute().getRouteName());
            if(busNumber != null && busNumber.equals(busRunDto.getBusNumber())) {
                throw new DataIntegrityViolationException("Route already exists");
            }
        }

        if (bus == null) {
            throw new EntityNotFoundException("BusSchedule not found for busNumber: " + busRunDto.getBusNumber());
            // Or return a custom error response if you prefer
        }

        BusRoute route = busRepository.findBusRouteByRouteName(busRunDto.getRoute().getRouteName());
        if (route == null) {
            // Create and save new BusRoute
            route = busRouteDtoMapper.toEntity(busRunDto.getRoute());

            busRepository.saveBusRoute(new BusRoute(route.getRouteName()));

            Long id = busRepository.isBusRouteExists(busRunDto.getRoute().getRouteName());
            route = busRepository.getBusRouteByRouteId(id);

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

        Long id = busRepository.isBusRouteExists(busRunDto.getRoute().getRouteName());
        route = busRepository.getBusRouteByRouteId(id);

        BusRun busRun = new BusRun();
        busRun.setBusSchedule(bus);
        busRun.setRoute(route);
        busRun.setStartTime(busRunDto.getStartTime());
        busRun.setScheduleType(busRunDto.getScheduleType());

        busRepository.saveBusRunWithRoute(busRun);
        return busRunDtoMapper.toDto(busRun);
    }

    @Override
    public List<BusRunDto> getBusRunByBusNumber(String busNumber){
        List<BusRun> busRuns = busRepository.getBusRunsByBusNumber(busNumber);
        List<BusRunDto> busRunDtos = new ArrayList<>();
        for (BusRun busRun : busRuns) {
            busRunDtoMapper.toDto(busRun);
            busRunDtos.add(busRunDtoMapper.toDto(busRun));
        }

        return busRunDtos;
    }

    @Override
    @Transactional
    public BusRunDto updateBusRunWithRouteName(BusRunDto busRunDto) {
        // Validate route
        Long busRunId = busRepository.isBusRouteExists(busRunDto.getRoute().getRouteName());
        if (busRunId == -1) {
            throw new DataIntegrityViolationException("Route does not exist");
        }

        // Validate bus
        BusSchedule bus = busRepository.getBusScheduleByBusNumber(busRunDto.getBusNumber());
        if (bus == null) {
            throw new EntityNotFoundException("Bus number does not exist");
        }

        // Fetch bus run (this is what makes it an update!)
        BusRun existingBusRun = busRepository.getBusRunByBusAndRoute(busRunDto);
        if (existingBusRun == null) {
            throw new EntityNotFoundException("BusRun with given ID does not exist");
        }

        // Fetch associated route
        BusRoute busRoute = busRepository.getBusRouteByRouteId(busRunId);

        // Update or add route stops
        if (busRunDto.getRoute().getStops() != null) {
            for (RouteStopDto stopDto : busRunDto.getRoute().getStops()) {

                // 1. Ensure the location exists
                Long locationId = busRepository.isBusLocationExists(stopDto.getLocationName());
                if (locationId == -1L) {
                    busRepository.saveBusLocation(stopDto.getLocationName());
                    locationId = busRepository.isBusLocationExists(stopDto.getLocationName());
                }

                BusLocation location = busRepository.getLocationById(locationId);
                if (location == null) {
                    throw new IllegalStateException("Location is null for stop: " + stopDto.getLocationName());
                }

                // 2. Check if the stop exists for this route and location
                RouteStop existingStop = busRepository.getRouteStopByRouteIdAndLocationId(busRoute.getId(), locationId);

                if (existingStop != null) {
                    // Update existing stop
                    existingStop.setStopOrder(stopDto.getStopOrder()); // or any other fields
                    existingStop.setLocation(location);
                    existingStop.setRoute(busRoute);
                    existingStop.setArrivalTime(stopDto.getArrivalTime());
                    existingStop.setDepartureTime(stopDto.getDepartureTime());
                    busRepository.saveRouteStop(existingStop);
                } else {
                    // Insert new stop
                    RouteStop stop = routeStopDtoMapper.toEntity(stopDto);
                    stop.setRoute(busRoute);
                    stop.setLocation(location);
                    busRepository.saveRouteStop(stop);
                }
            }
        }


        // Update existing bus run
        existingBusRun.setBusSchedule(bus);
        existingBusRun.setRoute(busRoute);
        existingBusRun.setStartTime(busRunDto.getStartTime());
        existingBusRun.setScheduleType(busRunDto.getScheduleType());

        // Save the updated bus run
        busRepository.saveBusRunWithRoute(existingBusRun);

        // Ensure stops are loaded before mapping to DTO
        Hibernate.initialize(existingBusRun.getRoute().getStops());

        return busRunDtoMapper.toDto(existingBusRun);
    }



}