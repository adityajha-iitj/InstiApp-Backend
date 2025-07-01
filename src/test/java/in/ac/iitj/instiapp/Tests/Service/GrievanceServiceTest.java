package in.ac.iitj.instiapp.Tests.Service;

import in.ac.iitj.instiapp.Repository.GrievanceRepository;
import in.ac.iitj.instiapp.Repository.UserRepository;
import in.ac.iitj.instiapp.Repository.MediaRepository;

import in.ac.iitj.instiapp.database.entities.Grievance;
import in.ac.iitj.instiapp.database.entities.Media.Mediatype;
import in.ac.iitj.instiapp.database.entities.User.Organisation.OrganisationPermission;
import in.ac.iitj.instiapp.payload.GrievanceDto;

import in.ac.iitj.instiapp.payload.Media.MediaBaseDto;
import in.ac.iitj.instiapp.payload.User.Organisation.OrganisationRoleDto;
import in.ac.iitj.instiapp.payload.User.UserBaseDto;
import in.ac.iitj.instiapp.services.GrievanceService;
import in.ac.iitj.instiapp.services.impl.GrievanceServiceImpl;
import in.ac.iitj.instiapp.mappers.GrievanceDtoMapper;


import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;


public class GrievanceServiceTest {

    @Mock
    private GrievanceRepository grievanceRepository;

    @Mock
    private MediaRepository mediaRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GrievanceDtoMapper grievanceDtoMapper;

    @InjectMocks
    private GrievanceServiceImpl grievanceService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    @Order(1)
    public void testGetGrievance() {
        // Arrange
        String publicId = "grievance123";
        String username = "user123";
        String orgUsername = "org123";
        String roleName = "Manager";
        OrganisationPermission permission = OrganisationPermission.INTERMEDIATE;
        String mediaPublicId = "media123";

        GrievanceDto grievanceDto = new GrievanceDto(
                publicId,
                "Sample Title",
                "Sample Description",
                username,
                orgUsername,
                roleName,
                permission,
                true,
                mediaPublicId
        );

        UserBaseDto userBaseDto = new UserBaseDto(username);
        userBaseDto.setName("John Doe"); // Set user name for validation

        MediaBaseDto mediaBaseDto = new MediaBaseDto(mediaPublicId);
        mediaBaseDto.setType(Mediatype.Image); // Assuming type is IMAGE
        mediaBaseDto.setPublicUrl("http://example.com/media123");

        // Mock repository calls
        Mockito.when(grievanceRepository.getGrievance(publicId)).thenReturn(grievanceDto);
        Mockito.when(userRepository.getUserLimited(username)).thenReturn(userBaseDto);
        Mockito.when(mediaRepository.findByPublicId(mediaPublicId)).thenReturn(mediaBaseDto);

        // Act
        GrievanceDto result = grievanceService.getGrievance(publicId);

        // Assert
        assertThat(result).isNotNull()
                .extracting(GrievanceDto::getPublicId, GrievanceDto::getTitle, GrievanceDto::getDescription, GrievanceDto::getResolved)
                .containsExactly(publicId, "Sample Title", "Sample Description", true);

        // Validate UserBaseDto fields
        assertThat(result.getUserFrom()).isNotNull()
                .extracting(UserBaseDto::getUserName, UserBaseDto::getName)
                .containsExactly(username, "John Doe");

        // Validate OrganisationRoleDto fields
        assertThat(result.getOrganisationRole()).isNotNull()
                .extracting(OrganisationRoleDto::getOrganisationUsername, OrganisationRoleDto::getRoleName, OrganisationRoleDto::getPermission)
                .containsExactly(orgUsername, roleName, permission);

        // Validate MediaBaseDto fields
        assertThat(result.getMedia()).isNotNull()
                .extracting(MediaBaseDto::getPublicId, MediaBaseDto::getType, MediaBaseDto::getPublicUrl)
                .containsExactly(mediaPublicId, Mediatype.Image, "http://example.com/media123");

        // Verify repository interactions
        Mockito.verify(grievanceRepository).getGrievance(publicId);
        Mockito.verify(userRepository).getUserLimited(username);
        Mockito.verify(mediaRepository).findByPublicId(mediaPublicId);
    }


//    @Test
//    @Order(2)
//    public void testUpdateGrievance() {
//        String testPublicId;
//        GrievanceDto testGrievanceDto;
//        Grievance testGrievance;
//
//        // Initialize test data
//        testPublicId = "test-grievance-123";
//        testGrievanceDto = new GrievanceDto(
//                testPublicId,
//                "Test Grievance",
//                "Test Description",
//                "testUser",
//                "testOrg",
//                "Manager",
//                OrganisationPermission.INTERMEDIATE,
//                false,
//                "media-123"
//        );
//
//        testGrievance = new Grievance();
//
//        // Arrange
//        when(grievanceRepository.getGrievance(testPublicId)).thenReturn(testGrievanceDto);
//        when(grievanceDtoMapper.toGrievance(testGrievanceDto)).thenReturn(testGrievance);
//        doNothing().when(grievanceRepository).updateGrievance(testPublicId, testGrievance);
//
//        // Act
//        grievanceService.updateGrievance(testPublicId);
//
//        // Assert using AssertJ and Mockito
//        verify(grievanceRepository).getGrievance(testPublicId);
//        verify(grievanceDtoMapper).toGrievance(testGrievanceDto);
//        verify(grievanceRepository).updateGrievance(testPublicId, testGrievance);
//    }


    @Test
    @Order(3)
    public void testGetGrievancesByFilter() {
        // Arrange
        String title = "Sample Title";
        String description = "Sample Description";
        String organisationName = "Organisation1";
        Boolean resolved = true;
        Pageable pageable = PageRequest.of(0, 10); // Example Pageable for pagination

        // Creating sample GrievanceDto objects
        GrievanceDto grievanceDto1 = new GrievanceDto(
                "grievance123",
                "Sample Title",
                "Sample Description",
                "user123",
                "org123",
                "Manager",
                OrganisationPermission.INTERMEDIATE,
                resolved,
                "media123"
        );

        GrievanceDto grievanceDto2 = new GrievanceDto(
                "grievance124",
                "Sample Title",
                "Different Description",
                "user124",
                "org124",
                "Employee",
                OrganisationPermission.READ,
                resolved,
                "media124"
        );

        // List of grievanceDtos returned by repository
        List<GrievanceDto> grievanceDtos = Arrays.asList(grievanceDto1, grievanceDto2);

        // Mock repository call to return filtered grievances
        Mockito.when(grievanceRepository.getGrievancesByFilter(
                Optional.of(title),
                Optional.of(description),
                Optional.of(organisationName),
                Optional.of(resolved),
                pageable
        )).thenReturn(grievanceDtos);

        // Act
        List<GrievanceDto> result = grievanceService.getGrievancesByFilter(
                Optional.of(title),
                Optional.of(description),
                Optional.of(organisationName),
                Optional.of(resolved),
                pageable
        );

        // Assert using AssertJ
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2); // We expect 2 grievances as returned by mock

        // Validate GrievanceDto fields
        GrievanceDto firstGrievance = result.get(0);
        assertThat(firstGrievance.getPublicId()).isEqualTo("grievance123");
        assertThat(firstGrievance.getTitle()).isEqualTo(title);
        assertThat(firstGrievance.getDescription()).isEqualTo(description);
        assertThat(firstGrievance.getResolved()).isTrue();

        GrievanceDto secondGrievance = result.get(1);
        assertThat(secondGrievance.getPublicId()).isEqualTo("grievance124");
        assertThat(secondGrievance.getTitle()).isEqualTo(title);
        assertThat(secondGrievance.getDescription()).isNotEqualTo(description); // Description should differ
        assertThat(secondGrievance.getResolved()).isTrue();

        // Verify that repository was called with the expected parameters
        Mockito.verify(grievanceRepository).getGrievancesByFilter(
                Optional.of(title),
                Optional.of(description),
                Optional.of(organisationName),
                Optional.of(resolved),
                pageable
        );
    }


    @Test
    @Order(4)
    public void testDeleteGrievance() {
        //grievanceService.deleteGrievance();
    }










}
