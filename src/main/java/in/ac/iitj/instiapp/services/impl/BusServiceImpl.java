package in.ac.iitj.instiapp.services.impl;

import in.ac.iitj.instiapp.Repository.BusRepository;
import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.*;
import in.ac.iitj.instiapp.mappers.Scheduling.Buses.BusOverrideDtoMapper;
import in.ac.iitj.instiapp.mappers.Scheduling.Buses.BusRouteDtoMapper;
import in.ac.iitj.instiapp.mappers.Scheduling.Buses.BusRunDtoMapper;
import in.ac.iitj.instiapp.mappers.Scheduling.Buses.RouteStopDtoMapper;
import in.ac.iitj.instiapp.payload.Scheduling.Buses.*;
import in.ac.iitj.instiapp.services.BusService;
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
    public List<BusRun> getBusRunsForRoute(String busNumber, String routeId) { return busRepository.getBusRunsForRoute(busNumber, routeId); }

    // BusRoute and RouteStop
    @Override
    @Transactional
    public BusRouteDto saveBusRoute(BusRouteDto busRouteDto) {
        BusRoute busRoute = busRouteDtoMapper.toEntity(busRouteDto);
        busRepository.saveBusRoute(busRoute);
        return busRouteDtoMapper.toDto(busRoute);
    }

    @Override
    @Transactional
    public BusRouteDto updateBusRoute(String routeId, BusRouteDto busRouteDto) {
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
    public BusRouteDto getBusRouteByRouteId(String routeId) {
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
    public List<RouteStop> getRouteStopsByRouteId(String routeId) { return busRepository.getRouteStopsByRouteId(routeId); }

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
    public RouteStopDto addRouteStop(String routeId, RouteStopDto stopDto) {
        RouteStop stop = routeStopDtoMapper.toEntity(stopDto);
        busRepository.saveRouteStop(stop);
        return routeStopDtoMapper.toDto(stop);
    }

    @Override
    @Transactional
    public BusRunDto createBusRunWithRoute(BusRunDto busRunDto) {
        BusRun busRun = busRunDtoMapper.toEntity(busRunDto);
        busRepository.saveBusRunWithRoute(busRun);
        return busRunDtoMapper.toDto(busRun);
    }
}