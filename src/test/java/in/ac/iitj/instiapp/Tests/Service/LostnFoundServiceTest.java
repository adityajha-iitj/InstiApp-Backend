package in.ac.iitj.instiapp.Tests.Service;

import in.ac.iitj.instiapp.Repository.LostnFoundRepository;
import in.ac.iitj.instiapp.database.entities.LostnFound.Locations;
import in.ac.iitj.instiapp.services.LostnFoundService;
import in.ac.iitj.instiapp.services.impl.LostnFoundServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LostnFoundServiceTest {

    @Mock
    private LostnFoundRepository lostnFoundRepository;

    private LostnFoundService lostnFoundService;

    @BeforeEach
    public void setUp() {
        lostnFoundService = new LostnFoundServiceImpl(lostnFoundRepository);
    }

    @Test
    public void testGetListOfLocationsName() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);

        // Prepare the expected result (a list of LostnFoundDto objects with sample locations)
        List<LostnFoundDto> expectedLocations = Arrays.asList(
                new LostnFoundDto(null, null, null, null, null, null, null, null, "LHC", null, null, null),  // LHC
                new LostnFoundDto(null, null, null, null, null, null, null, null, "PHC", null, null, null),  // PHC
                new LostnFoundDto(null, null, null, null, null, null, null, null, "SAC", null, null, null),  // SAC
                new LostnFoundDto(null, null, null, null, null, null, null, null, "CIC", null, null, null),  // CIC
                new LostnFoundDto(null, null, null, null, null, null, null, null, "JGC", null, null, null)   // JGC
        );

        // Mock the repository call to return the expected result
        when(lostnFoundRepository.getListOfLocationsName(pageable)).thenReturn(expectedLocations);

        // Act
        List<LostnFoundDto> actualLocations = lostnFoundService.getListOfLocationsName(pageable);

        // Assert
        assertEquals(expectedLocations, actualLocations);
        verify(lostnFoundRepository).getListOfLocationsName(pageable);
    }



    @Test
    public void testSaveLocation() {
        // Arrange
        Locations location = new Locations();
        location.setName("Test Location");

        // Act
        lostnFoundService.saveLocation(location);

        // Assert
        verify(lostnFoundRepository).saveLocation(location);
    }

    @Test
    public void testDeleteLocationByName() {
        // Arrange
        String locationName = "Test Location";

        // Act
        lostnFoundService.deleteLocationByName(locationName);

        // Assert
        verify(lostnFoundRepository).deleteLocationByName(locationName);
    }
}