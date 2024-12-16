package in.ac.iitj.instiapp.Tests.Service;

import in.ac.iitj.instiapp.Repository.BusRepository;
import in.ac.iitj.instiapp.services.BusService;
import in.ac.iitj.instiapp.services.impl.BusServiceImpl;
import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.BusLocation;
import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.BusRun;
import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.BusSchedule;
import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.ScheduleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BusServiceTest {

    @Mock
    private BusRepository busRepository;

    private BusService busService;

    @BeforeEach
    public void setUp() {
        busService = new BusServiceImpl(busRepository);
    }

    @Test
    public void testCreateBusLocation() {
        String locationName = "Test Location";
        busService.createBusLocation(locationName);
        verify(busRepository).saveBusLocation(locationName);
    }

    @Test
    public void testRemoveBusLocation() {
        String locationName = "Test Location";
        busService.removeBusLocation(locationName);
        verify(busRepository).deleteBusLocation(locationName);
    }

    @Test
    public void testFindBusLocation() {
        String locationName = "Test Location";
        BusLocation expectedLocation = new BusLocation(locationName);
        when(busRepository.getBusLocation(locationName)).thenReturn(expectedLocation);

        BusLocation result = busService.findBusLocation(locationName);
        assertEquals(expectedLocation, result);
    }

    @Test
    public void testListBusLocations() {
        Pageable pageable = PageRequest.of(0, 10);
        List<String> expectedLocations = Arrays.asList("Location1", "Location2");
        when(busRepository.getListOfBusLocations(pageable)).thenReturn(expectedLocations);

        List<String> result = busService.listBusLocations(pageable);
        assertEquals(expectedLocations, result);
    }

    @Test
    public void testCheckBusLocationExists() {
        String locationName = "Test Location";
        when(busRepository.isBusLocationExists(locationName)).thenReturn(true);

        boolean result = busService.checkBusLocationExists(locationName);
        assertTrue(result);
    }

    @Test
    public void testCreateBusSchedule() {
        BusSchedule busSchedule = new BusSchedule("B1");
        busService.createBusSchedule(busSchedule);
        verify(busRepository).saveBusSchedule(busSchedule);
    }

    @Test
    public void testFindBusSchedule() {
        String busNumber = "B1";
        BusSchedule expectedSchedule = new BusSchedule(busNumber);
        when(busRepository.getBusSchedule(busNumber)).thenReturn(expectedSchedule);

        BusSchedule result = busService.findBusSchedule(busNumber);
        assertEquals(expectedSchedule, result);
    }

    @Test
    public void testCreateBusRun() {
        BusSchedule busSchedule = new BusSchedule("B1");
        BusRun busRun = new BusRun();
        busRun.setBusSchedule(busSchedule);

        busService.createBusRun(busRun);
        verify(busRepository).saveBusRun(busRun);
    }

    @Test
    public void testUpdateBusScheduleRun() {
        String busNumber = "B1";
        ScheduleType scheduleType = ScheduleType.WEEKDAY;
        Time timeOfDeparture = Time.valueOf(LocalTime.of(10, 30));
        BusRun busRun = new BusRun();

        busService.updateBusScheduleRun(busNumber, scheduleType, timeOfDeparture, busRun);
        verify(busRepository).updateBusScheduleRun(busNumber, scheduleType, timeOfDeparture, busRun);
    }

    @Test
    public void testRemoveBusSchedule() {
        String busNumber = "B1";
        busService.removeBusSchedule(busNumber);
        verify(busRepository).deleteBusSchedule(busNumber);
    }

    @Test
    public void testUpdateBusScheduleNumber() {
        String oldBusNumber = "B1";
        String newBusNumber = "B2";

        busService.updateBusScheduleNumber(oldBusNumber, newBusNumber);
        verify(busRepository).updateBusSchedule(oldBusNumber, newBusNumber);
    }
}