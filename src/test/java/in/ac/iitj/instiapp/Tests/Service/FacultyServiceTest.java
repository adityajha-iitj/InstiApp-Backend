package in.ac.iitj.instiapp.Tests.Service;

import in.ac.iitj.instiapp.Repository.FacultyRepository;
import in.ac.iitj.instiapp.Repository.User.Organisation.OrganisationRepository;
import in.ac.iitj.instiapp.Repository.UserRepository;
import in.ac.iitj.instiapp.database.entities.User.Faculty.Faculty;
import in.ac.iitj.instiapp.services.FacultyService;
import in.ac.iitj.instiapp.payload.User.Faculty.FacultyBaseDto;
import in.ac.iitj.instiapp.payload.User.Organisation.OrganisationBaseDto;
import in.ac.iitj.instiapp.payload.User.Organisation.OrganisationDetailedDto;
import in.ac.iitj.instiapp.payload.User.UserBaseDto;
import in.ac.iitj.instiapp.payload.User.UserDetailedDto;
import in.ac.iitj.instiapp.services.impl.FacultyServiceImpl;
import in.ac.iitj.instiapp.mappers.User.Faculty.FacultyDtoMapper;
import in.ac.iitj.instiapp.payload.User.Faculty.FacultyDetailedDto;


import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class FacultyServiceTest {

    @Mock
    private FacultyRepository facultyRepository;

    @Mock
    private OrganisationRepository organisationRepository;

    @Mock
    private FacultyDtoMapper facultyDtoMapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FacultyServiceImpl facultyService; // Use the actual implementation here

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks and inject them
    }

    @Test
    @Order(1)
    public void testGetFaculty() {
        // Mock input
        String facultyUserName = "faculty123";
        String orgUserName = "orgUser123";

        // Mock repository responses
        FacultyBaseDto mockFacultyBaseDto = new FacultyBaseDto(
                facultyUserName,
                orgUserName,
                "A faculty description",
                "https://faculty-website.com"
        );

        OrganisationBaseDto mockOrganisationBaseDto = new OrganisationBaseDto(
                orgUserName,
                "parentOrgUserName",
                "Organisation Type",
                "Organisation Description",
                "https://org-website.com"
        );

        UserBaseDto mockUserBaseDto = new UserBaseDto(facultyUserName);
        mockUserBaseDto.setName("Faculty Name");
        mockUserBaseDto.setEmail("faculty@example.com");
        mockUserBaseDto.setAvatarUrl("https://avatar-url.com");
        mockUserBaseDto.setUserTypeName("Faculty");

        // Define mock behavior
        when(facultyRepository.getFaculty(facultyUserName)).thenReturn(mockFacultyBaseDto);
        when(organisationRepository.getOrganisation(orgUserName)).thenReturn(mockOrganisationBaseDto);
        when(userRepository.getUserLimited(facultyUserName)).thenReturn(mockUserBaseDto);

        // Call the method under test
        FacultyBaseDto result = facultyService.getFaculty(facultyUserName);

        // Assertions
        assertNotNull(result, "Result should not be null");
        assertEquals("faculty123", result.getUser().getUserName(), "UserName should match");
        assertEquals("Faculty Name", result.getUser().getName(), "User name should match");
        assertEquals("orgUser123", result.getOrganisation().getUser().getUserName(), "Organisation UserName should match");
        assertEquals("Organisation Description", result.getOrganisation().getDescription(), "Organisation description should match");
        assertEquals("https://faculty-website.com", result.getWebsiteUrl(), "Website URL should match");

        // Verify repository interactions
        verify(facultyRepository, times(1)).getFaculty(facultyUserName);
        verify(organisationRepository, times(1)).getOrganisation(orgUserName);
        verify(userRepository, times(1)).getUserLimited(facultyUserName);
    }

    @Test
    @Order(2)
    public void testGetDetailedFaculty() {
        // Mock input
        String facultyUserName = "faculty123";
        String orgUserName = "orgUser123";

        // Mock repository responses
        FacultyDetailedDto mockFacultyDetailedDto = new FacultyDetailedDto(
                facultyUserName,
                orgUserName,
                "A detailed faculty description",
                "https://faculty-website.com"
        );

        OrganisationBaseDto mockOrganisationBaseDto = new OrganisationBaseDto(
                orgUserName,
                "parentOrgUserName",
                "Organisation Type",
                "Organisation Description",
                "https://org-website.com"
        );

        // Create a mock UserDetailedDto for the faculty
        UserDetailedDto mockUserDetailedDto = new UserDetailedDto(
                "Faculty Name",
                facultyUserName,
                "faculty@example.com",
                "https://avatar-url.com",
                "Faculty"
        );

        // Define mock behavior
        when(facultyRepository.getDetailedFaculty(facultyUserName)).thenReturn(mockFacultyDetailedDto);
        when(userRepository.getUserDetailed(facultyUserName, false)).thenReturn(mockUserDetailedDto);
        when(organisationRepository.getOrganisation(orgUserName)).thenReturn(mockOrganisationBaseDto);

        // Call the method under test
        FacultyDetailedDto result = facultyService.getDetailedFaculty(facultyUserName, false);

        // Assertions
        assertNotNull(result, "Result should not be null");
        assertEquals("faculty123", result.getUser().getUserName(), "UserName should match");
        assertEquals("Faculty Name", result.getUser().getName(), "User name should match");
        assertEquals("orgUser123", result.getOrganisation().getUser().getUserName(), "Organisation UserName should match");
        assertEquals("Organisation Description", result.getOrganisation().getDescription(), "Organisation description should match");
        assertEquals("https://org-website.com", result.getOrganisation().getWebsite(), "Organisation Website URL should match");

        // Verify repository interactions
        verify(facultyRepository, times(1)).getDetailedFaculty(facultyUserName);
        verify(userRepository, times(1)).getUserDetailed(facultyUserName, false);
        verify(organisationRepository, times(1)).getOrganisation(orgUserName);
    }

    @Test
    @Order(3)
    public void testUpdateFaculty() {
        // Mock input
        String facultyUserName = "faculty123";
        FacultyBaseDto mockFacultyBaseDto = new FacultyBaseDto(
                facultyUserName,
                "orgUser123",
                "Updated faculty description",
                "https://updated-faculty-website.com"
        );

        Faculty mockFaculty = new Faculty();  // Assuming you have a Faculty class for persistence

        // Define mock behavior
        when(facultyDtoMapper.toFaculty(mockFacultyBaseDto)).thenReturn(mockFaculty);

        // Call the method under test
        facultyService.updateFaculty(mockFacultyBaseDto);

        // Verify interactions
        verify(facultyDtoMapper, times(1)).toFaculty(mockFacultyBaseDto);
        verify(facultyRepository, times(1)).updateFaculty(mockFaculty);
    }

    @Test
    @Order(4)
    public void testGetFacultyByFilter() {
        // Mock input
        Optional<String> organisationName = Optional.of("IIT Jodhpur");
        Optional<String> description = Optional.of("Faculty description");
        Optional<String> websiteUrl = Optional.of("https://faculty-website.com");
        Pageable pageable = PageRequest.of(0, 10);  // Assuming Pageable is from Spring Data (PageRequest)

        FacultyBaseDto facultyBaseDto1 = new FacultyBaseDto("faculty123", "org123", "Faculty description 1", "https://faculty-website1.com");
        FacultyBaseDto facultyBaseDto2 = new FacultyBaseDto("faculty124", "org124", "Faculty description 2", "https://faculty-website2.com");
        List<FacultyBaseDto> mockFacultyList = Arrays.asList(facultyBaseDto1, facultyBaseDto2);

        // Define mock behavior
        when(facultyRepository.getFacultyByFilter(organisationName, description, websiteUrl, pageable)).thenReturn(mockFacultyList);

        // Call the method under test
        List<FacultyBaseDto> result = facultyService.getFacultyByFilter(organisationName, description, websiteUrl, pageable);

        // Assertions
        assertNotNull(result, "Result should not be null");
        assertEquals(2, result.size(), "The size of the result should match the mock data size");
        assertEquals("faculty123", result.get(0).getUser().getUserName(), "First faculty's username should match");
        assertEquals("faculty124", result.get(1).getUser().getUserName(), "Second faculty's username should match");

        // Verify repository interaction
        verify(facultyRepository, times(1)).getFacultyByFilter(organisationName, description, websiteUrl, pageable);
    }





}
