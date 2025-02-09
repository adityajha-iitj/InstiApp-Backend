package in.ac.iitj.instiapp.Tests.Service;


import in.ac.iitj.instiapp.Repository.BusRepository;
import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.ScheduleType;
import in.ac.iitj.instiapp.payload.Scheduling.Buses.BusOverrideDto;
import in.ac.iitj.instiapp.payload.Scheduling.Buses.BusRunDto;
import in.ac.iitj.instiapp.payload.Scheduling.Buses.BusScheduleDto;
import in.ac.iitj.instiapp.services.impl.BusServiceImpl;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.springframework.core.annotation.Order;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.sql.Time;
import java.util.*;

public class BusServiceTest {

    @Mock
    public BusRepository busRepository;


    @InjectMocks
    private BusServiceImpl busService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Order(1)
    public void testGetBusLocations() {
        List<String> busLocations = new ArrayList<>();
        busLocations.add("IIT Jodhpur");
        busLocations.add("MBM");
        busLocations.add("Paota");

        Pageable pageable = PageRequest.of(0,10);

        when(busRepository.getListOfBusLocations(pageable)).thenReturn(busLocations);

        List<String> result = busService.getBusLocations(pageable);

        assertThat(result)
                .isNotNull()
                .hasSize(3)
                .containsExactly("IIT Jodhpur", "MBM", "Paota");

        verify(busRepository, times(1)).getListOfBusLocations(pageable);
    }

    @Test
    @Order(2)
    public void testIsBusLocationExist(){
        String l1 = "IIT Jodhpur";
        String l2 = "MBM";
        String l3 = "Paota";

        when(busRepository.isBusLocationExists(l1)).thenReturn(1L);
        when(busRepository.isBusLocationExists(l2)).thenReturn(2L);
        when(busRepository.isBusLocationExists(l3)).thenReturn(-1L);

        Long result1 = busService.isBusLocationExist(l1);
        Long result2 = busService.isBusLocationExist(l2);
        Long result3 = busService.isBusLocationExist(l3);

        assertThat(result1)
                .isNotNull()
                .isEqualTo(1L);

        assertThat(result2)
        .isNotNull()
                .isEqualTo(2L);

        assertThat(result3)
        .isNotNull()
                .isEqualTo(-1L);

        verify(busRepository, times(1)).isBusLocationExists(l1);
        verify(busRepository, times(1)).isBusLocationExists(l2);
        verify(busRepository, times(1)).isBusLocationExists(l3);
    }

    @Test
    @Order(3)
    void testGetBusSchedule() {
        // Given
        String busNumber = "B1";

        BusRunDto busRunDto = new BusRunDto(
                "run1",
                new Time(10, 30, 0),
                "Station A",
                "Station B",
                ScheduleType.WEEKDAY
        );

        BusOverrideDto busOverrideDto = new BusOverrideDto(
                "override1",
                busNumber,
                new Time(11, 00, 0),
                "Station C",
                "Station D",
                new Date(),
                "Maintenance work"
        );

        Set<BusRunDto> busRuns = new HashSet<>(Collections.singletonList(busRunDto));
        Set<BusOverrideDto> busOverrides = new HashSet<>(Collections.singletonList(busOverrideDto));

        BusScheduleDto expectedBusSchedule = new BusScheduleDto(busNumber, busRuns, Optional.of(busOverrides));

        when(busRepository.getBusSchedule(busNumber)).thenReturn(expectedBusSchedule);

        // When
        BusScheduleDto actualBusSchedule = busService.getBusSchedule(busNumber);

        // Then
        assertThat(actualBusSchedule).isNotNull();
        assertThat(actualBusSchedule.getBusNumber()).isEqualTo(busNumber);
        assertThat(actualBusSchedule.getRuns()).hasSize(1);
        assertThat(actualBusSchedule.getRuns().iterator().next().getPublicId()).isEqualTo("run1");
        assertThat(actualBusSchedule.getBusOverrides()).hasSize(1);
        assertThat(actualBusSchedule.getBusOverrides().iterator().next().getPublicId()).isEqualTo("override1");
    }

    @Test
    @Order(4)
    public void testGetBusNumbers(){
        List<String> busNumbers = new ArrayList<>();
        busNumbers.add("B1");
        busNumbers.add("B2");
        busNumbers.add("B3");

        Pageable pageable = PageRequest.of(0,10);

        when(busRepository.getBusNumbers(pageable)).thenReturn(busNumbers);

        List<String> result = busService.getBusNumbers(pageable);

        assertThat(result)
                .isNotNull()
                .hasSize(3)
                .containsExactly("B1", "B2", "B3");

        verify(busRepository,times(1)).getBusNumbers(pageable);
    }

    @Test
    @Order(5)
    public void testExistsBusSchedule(){
        String busNumber1 = "B1";
        String busNumber2 = "Suiiiiii";

        when(busRepository.existsBusSchedule(busNumber1)).thenReturn(1L);
        when(busRepository.existsBusSchedule(busNumber2)).thenReturn(-1L);

        Long result1 = busService.existsBusSchedule(busNumber1);
        Long result2 = busService.existsBusSchedule(busNumber2);

        assertThat(result1)
                .isNotNull()
                .isEqualTo(1L);
        assertThat(result2)
                .isNotNull()
                .isEqualTo(-1L);

        verify(busRepository, times(1)).existsBusSchedule(busNumber1);
        verify(busRepository, times(1)).existsBusSchedule(busNumber2);


    }

    @Test
    @Order(6)
    public void testExistsBusRunByPublicId() {
        String publicId1 = "PUB123";
        String publicId2 = "UNKNOWN";

        when(busRepository.existsBusRunByPublicId(publicId1)).thenReturn(true);
        when(busRepository.existsBusRunByPublicId(publicId2)).thenReturn(false);

        Boolean result1 = busService.existsBusRunByPublicId(publicId1);
        Boolean result2 = busService.existsBusRunByPublicId(publicId2);

        assertThat(result1)
                .isNotNull()
                .isTrue();
        assertThat(result2)
                .isNotNull()
                .isFalse();

        verify(busRepository, times(1)).existsBusRunByPublicId(publicId1);
        verify(busRepository, times(1)).existsBusRunByPublicId(publicId2);
    }

    @Test
    @Order(7)
    public void testExistsBusOverrideByPublicId() {
        String publicId1 = "OVERRIDE_001";
        String publicId2 = "UNKNOWN_ID";

        when(busRepository.existsBusOverrideByPublicId(publicId1)).thenReturn(true);
        when(busRepository.existsBusOverrideByPublicId(publicId2)).thenReturn(false);

        boolean result1 = busService.existsBusOverrideByPublicId(publicId1);
        boolean result2 = busService.existsBusOverrideByPublicId(publicId2);

        assertThat(result1)
                .isNotNull()
                .isTrue();
        assertThat(result2)
                .isNotNull()
                .isFalse();

        verify(busRepository, times(1)).existsBusOverrideByPublicId(publicId1);
        verify(busRepository, times(1)).existsBusOverrideByPublicId(publicId2);
    }

    @Test
    @Order(8)
    public void testGetBusOverrideForYearAndMonth() {
        int year = 2025;
        int month = 2;

        // Creating BusOverrideDto objects with values for each column
        BusOverrideDto busOverride1 = new BusOverrideDto(
                "PUB001",
                "B1",
                Time.valueOf("10:30:00"),
                "Hostel 1",
                "Library",
                new Date(2025 - 1900, 1, 10), // Date(year-1900, month-1, day)
                "Exam special bus"
        );

        BusOverrideDto busOverride2 = new BusOverrideDto(
                "PUB002",
                "B2",
                Time.valueOf("14:00:00"),
                "Main Gate",
                "Mess",
                new Date(2025 - 1900, 1, 15),
                "Lunch time special"
        );

        List<BusOverrideDto> mockOverrides = Arrays.asList(busOverride1, busOverride2);

        when(busRepository.getBusOverrideForYearAndMonth(year, month)).thenReturn(mockOverrides);

        List<BusOverrideDto> result = busService.getBusOverrideForYearAndMonth(year, month);

        // Assertions using AssertJ
        assertThat(result)
                .isNotNull()
                .hasSize(2)
                .containsExactlyInAnyOrder(busOverride1, busOverride2);

        assertThat(result.get(0))
                .extracting(BusOverrideDto::getPublicId, BusOverrideDto::getBusScheduleBusNumber,
                        BusOverrideDto::getTimeOfDeparture, BusOverrideDto::getFromLocationName,
                        BusOverrideDto::getToLocationName, BusOverrideDto::getOverrideDate,
                        BusOverrideDto::getDescription)
                .containsExactly("PUB001", "B1", Time.valueOf("10:30:00"),
                        "Hostel 1", "Library", new Date(2025 - 1900, 1, 10),
                        "Exam special bus");

        assertThat(result.get(1))
                .extracting(BusOverrideDto::getPublicId, BusOverrideDto::getBusScheduleBusNumber,
                        BusOverrideDto::getTimeOfDeparture, BusOverrideDto::getFromLocationName,
                        BusOverrideDto::getToLocationName, BusOverrideDto::getOverrideDate,
                        BusOverrideDto::getDescription)
                .containsExactly("PUB002", "B2", Time.valueOf("14:00:00"),
                        "Main Gate", "Mess", new Date(2025 - 1900, 1, 15),
                        "Lunch time special");

        verify(busRepository, times(1)).getBusOverrideForYearAndMonth(year, month);
    }




}