package in.ac.iitj.instiapp.Tests.Service;

import in.ac.iitj.instiapp.Repository.FacultyRepository;
import in.ac.iitj.instiapp.Repository.User.Organisation.OrganisationRepository;
import in.ac.iitj.instiapp.Repository.UserRepository;
import in.ac.iitj.instiapp.services.FacultyService;
import in.ac.iitj.instiapp.payload.User.Faculty.FacultyBaseDto;
import in.ac.iitj.instiapp.payload.User.Organisation.OrganisationBaseDto;
import in.ac.iitj.instiapp.payload.User.UserBaseDto;
import in.ac.iitj.instiapp.services.impl.FacultyServiceImpl;
import in.ac.iitj.instiapp.mappers.User.Faculty.FacultyDtoMapper;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
}
