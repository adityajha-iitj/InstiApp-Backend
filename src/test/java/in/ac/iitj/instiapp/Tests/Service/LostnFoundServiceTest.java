package in.ac.iitj.instiapp.Tests.Service;

import in.ac.iitj.instiapp.Repository.LostnFoundRepository;
import in.ac.iitj.instiapp.Tests.EntityTestData.LocationData;
import in.ac.iitj.instiapp.Tests.EntityTestData.LostnFoundData;
import in.ac.iitj.instiapp.Tests.EntityTestData.MediaData;
import in.ac.iitj.instiapp.Tests.EntityTestData.UserData;
import in.ac.iitj.instiapp.database.entities.LostnFound.Locations;
import in.ac.iitj.instiapp.database.entities.LostnFound.LostnFound;
import in.ac.iitj.instiapp.payload.LostnFound.LostnFoundDto;
import in.ac.iitj.instiapp.services.impl.LostnFoundServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class LostnFoundServiceTest {

    @Mock
    private LostnFoundRepository lostnFoundRepository;

    @InjectMocks
    private LostnFoundServiceImpl lostnFoundService;  // Changed from interface to implementation

    private Pageable pageable;

    @BeforeEach
    void setup() {
        pageable = PageRequest.of(0, 10);
    }

    @Test
    void testGetListOfLocationsName() {
        // Arrange
        List<String> expectedLocations = Arrays.asList(LocationData.LOCATION1.name, LocationData.LOCATION2.name, LocationData.LOCATION3.name);
        when(lostnFoundRepository.getListOfLocationsName(pageable)).thenReturn(expectedLocations);

        // Act
        List<String> actualLocations = lostnFoundService.getListOfLocationsName(pageable);

        // Assert
        assertEquals(expectedLocations, actualLocations);
        verify(lostnFoundRepository).getListOfLocationsName(pageable);
    }

    @Test
    void testSaveLocation() {
        // Arrange
        Locations location = new Locations();
        location.setName(LocationData.LOCATION1.name);

        // Act
        lostnFoundService.saveLocation(location);

        // Assert
        verify(lostnFoundRepository).saveLocation(location);
    }

    @Test
    void testDeleteLocationByName() {
        // Arrange
        String locationName = LocationData.LOCATION1.name;

        // Act
        lostnFoundService.deleteLocationByName(locationName);

        // Assert
        verify(lostnFoundRepository).deleteLocationByName(locationName);
    }

    @Test
    void testUpdateLocation() {
        // Arrange
        String oldLocationName = LocationData.LOCATION1.name;
        Locations newLocation = new Locations();
        newLocation.setName(LocationData.LOCATION2.name);

        // Act
        lostnFoundService.updateLocation(oldLocationName, LocationData.LOCATION2.name);

        // Assert
        verify(lostnFoundRepository).updateLocation(oldLocationName, newLocation);
    }

//    @Test
//    void testSaveLostAndFound() {
//        // Arrange
//        LostnFoundDto dto = createSampleLostnFoundDto();
//
//        // Act
//        lostnFoundService.saveLostAndFound(dto);
//
//        // Assert
//        verify(lostnFoundRepository).saveLostnFoundDetails(any(LostnFound.class));
//    }

//    @Test
//    void testUpdateLostAndFound() {
//        // Arrange
//        LostnFoundDto dto = createSampleLostnFoundDto();
//
//        // Act
//        lostnFoundService.updateLostAndFound(dto);
//
//        // Assert
//        verify(lostnFoundRepository).updateLostnFound(any(LostnFound.class), eq(dto.getPublicId()));
//    }

    @Test
    void testDeleteLostAndFound() {
        // Arrange
        String publicId = LostnFoundData.LOST_N_FOUND1.publicId;

        // Act
        lostnFoundService.deleteLostAndFound(publicId);

        // Assert
        verify(lostnFoundRepository).deleteLostnFound(publicId);
    }

//    @Test
//    void testGetLostAndFoundByFilter() {
//        // Arrange
//        Optional<Boolean> status = Optional.of(LostnFoundData.LOST_N_FOUND1.status);
//        Optional<String> owner = Optional.of(UserData.USER1.userName);
//        Optional<String> finder = Optional.of(UserData.USER2.userName);
//        Optional<String> landmark = Optional.of(LocationData.LOCATION1.name);
//
//        List<LostnFoundDto> expectedResults = Arrays.asList(
//                createSampleLostnFoundDto(),
//                createSampleLostnFoundDto()
//        );
//
//        when(lostnFoundRepository.getLostnFoundByFilter(status, owner, finder, landmark, pageable))
//                .thenReturn(expectedResults);
//
//        // Act
//        List<LostnFoundDto> actualResults = lostnFoundService.getLostAndFoundByFilter(
//                status, owner, finder, landmark, pageable
//        );
//
//        // Assert
//        assertEquals(expectedResults, actualResults);
//        verify(lostnFoundRepository).getLostnFoundByFilter(status, owner, finder, landmark, pageable);
//    }

//    @Test
//    void testGetLostAndFoundByFilterWithEmptyOptionals() {
//        // Arrange
//        Optional<Boolean> status = Optional.empty();
//        Optional<String> owner = Optional.empty();
//        Optional<String> finder = Optional.empty();
//        Optional<String> landmark = Optional.empty();
//
//        List<LostnFoundDto> expectedResults = Arrays.asList(
//                createSampleLostnFoundDto(),
//                createSampleLostnFoundDto()
//        );
//
//        when(lostnFoundRepository.getLostnFoundByFilter(status, owner, finder, landmark, pageable))
//                .thenReturn(expectedResults);
//
//        // Act
//        List<LostnFoundDto> actualResults = lostnFoundService.getLostAndFoundByFilter(
//                status, owner, finder, landmark, pageable
//        );
//
//        // Assert
//        assertEquals(expectedResults, actualResults);
//        verify(lostnFoundRepository).getLostnFoundByFilter(status, owner, finder, landmark, pageable);
//    }
//
//    private LostnFoundDto createSampleLostnFoundDto() {
//        return new LostnFoundDto(
//                LostnFoundData.LOST_N_FOUND1.publicId,  // publicId
//                UserData.USER1.userName,           // finder_username
//                UserData.USER2.userName,           // owner_username
//                LocationData.LOCATION1.name,        // landmark_name
//                LostnFoundData.LOST_N_FOUND1.extraInfo,    // extra_info
//                LostnFoundData.LOST_N_FOUND1.status,            // status
//                MediaData.MEDIA1.publicId              // media_publicId (assuming no media for sample)
//        );
//    }
}