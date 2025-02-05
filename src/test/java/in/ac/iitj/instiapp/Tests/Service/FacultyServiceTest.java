package in.ac.iitj.instiapp.Tests.Service;

import in.ac.iitj.instiapp.Repository.FacultyRepository;
import in.ac.iitj.instiapp.Repository.User.Organisation.OrganisationRepository;
import in.ac.iitj.instiapp.Repository.UserRepository;
import in.ac.iitj.instiapp.database.entities.User.Faculty.Faculty;
import in.ac.iitj.instiapp.payload.User.Faculty.FacultyBaseDto;
import in.ac.iitj.instiapp.payload.User.Organisation.OrganisationBaseDto;
import in.ac.iitj.instiapp.payload.User.UserBaseDto;
import in.ac.iitj.instiapp.payload.User.UserDetailedDto;
import in.ac.iitj.instiapp.services.impl.FacultyServiceImpl;
import in.ac.iitj.instiapp.mappers.User.Faculty.FacultyDtoMapper;
import in.ac.iitj.instiapp.payload.User.Faculty.FacultyDetailedDto;


import static org.mockito.Mockito.*;

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
import static org.assertj.core.api.Assertions.*;

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
    private FacultyServiceImpl facultyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Order(1)
    public void testGetFaculty() {
        String facultyUserName = "faculty123";
        String orgUserName = "orgUser123";

        FacultyBaseDto mockFacultyBaseDto = new FacultyBaseDto(
                facultyUserName, orgUserName, "A faculty description", "https://faculty-website.com"
        );
        OrganisationBaseDto mockOrganisationBaseDto = new OrganisationBaseDto(
                orgUserName, "parentOrgUserName", "Organisation Type", "Organisation Description", "https://org-website.com"
        );
        UserBaseDto mockUserBaseDto = new UserBaseDto(facultyUserName);
        mockUserBaseDto.setName("Faculty Name");
        mockUserBaseDto.setEmail("faculty@example.com");
        mockUserBaseDto.setAvatarUrl("https://avatar-url.com");
        mockUserBaseDto.setUserTypeName("Faculty");

        when(facultyRepository.getFaculty(facultyUserName)).thenReturn(mockFacultyBaseDto);
        when(organisationRepository.getOrganisation(orgUserName)).thenReturn(mockOrganisationBaseDto);
        when(userRepository.getUserLimited(facultyUserName)).thenReturn(mockUserBaseDto);

        FacultyBaseDto result = facultyService.getFaculty(facultyUserName);

        assertThat(result).isNotNull();
        assertThat(result.getUser().getUserName()).isEqualTo("faculty123");
        assertThat(result.getUser().getName()).isEqualTo("Faculty Name");
        assertThat(result.getOrganisation().getUser().getUserName()).isEqualTo("orgUser123");
        assertThat(result.getOrganisation().getDescription()).isEqualTo("Organisation Description");
        assertThat(result.getWebsiteUrl()).isEqualTo("https://faculty-website.com");

        verify(facultyRepository, times(1)).getFaculty(facultyUserName);
        verify(organisationRepository, times(1)).getOrganisation(orgUserName);
        verify(userRepository, times(1)).getUserLimited(facultyUserName);
    }

    @Test
    @Order(2)
    public void testGetDetailedFaculty() {
        String facultyUserName = "faculty123";
        String orgUserName = "orgUser123";

        FacultyDetailedDto mockFacultyDetailedDto = new FacultyDetailedDto(
                facultyUserName, orgUserName, "A detailed faculty description", "https://faculty-website.com"
        );
        OrganisationBaseDto mockOrganisationBaseDto = new OrganisationBaseDto(
                orgUserName, "parentOrgUserName", "Organisation Type", "Organisation Description", "https://org-website.com"
        );
        UserDetailedDto mockUserDetailedDto = new UserDetailedDto(
                "Faculty Name", facultyUserName, "faculty@example.com", "https://avatar-url.com", "Faculty"
        );

        when(facultyRepository.getDetailedFaculty(facultyUserName)).thenReturn(mockFacultyDetailedDto);
        when(userRepository.getUserDetailed(facultyUserName, false)).thenReturn(mockUserDetailedDto);
        when(organisationRepository.getOrganisation(orgUserName)).thenReturn(mockOrganisationBaseDto);

        FacultyDetailedDto result = facultyService.getDetailedFaculty(facultyUserName, false);

        assertThat(result).isNotNull();
        assertThat(result.getUser().getUserName()).isEqualTo("faculty123");
        assertThat(result.getUser().getName()).isEqualTo("Faculty Name");
        assertThat(result.getOrganisation().getUser().getUserName()).isEqualTo("orgUser123");
        assertThat(result.getOrganisation().getDescription()).isEqualTo("Organisation Description");
        assertThat(result.getOrganisation().getWebsite()).isEqualTo("https://org-website.com");

        verify(facultyRepository, times(1)).getDetailedFaculty(facultyUserName);
        verify(userRepository, times(1)).getUserDetailed(facultyUserName, false);
        verify(organisationRepository, times(1)).getOrganisation(orgUserName);
    }

    @Test
    @Order(3)
    public void testUpdateFaculty() {
        FacultyDetailedDto facultyDetailedDto = new FacultyDetailedDto(
                "testFaculty", "testOrg", "Test Description", "http://test-website.com"
        );

        Faculty mockFaculty = new Faculty();
        when(facultyDtoMapper.toFaculty(facultyDetailedDto)).thenReturn(mockFaculty);
        doNothing().when(facultyRepository).updateFaculty(mockFaculty);

        facultyService.updateFaculty(facultyDetailedDto);

        verify(facultyDtoMapper, times(1)).toFaculty(facultyDetailedDto);
        verify(facultyRepository, times(1)).updateFaculty(mockFaculty);
    }

    @Test
    @Order(4)
    public void testGetFacultyByFilter() {
        Optional<String> organisationName = Optional.of("IIT Jodhpur");
        Optional<String> description = Optional.of("Faculty description");
        Optional<String> websiteUrl = Optional.of("https://faculty-website.com");
        Pageable pageable = PageRequest.of(0, 10);

        FacultyBaseDto facultyBaseDto1 = new FacultyBaseDto("faculty123", "org123", "Faculty description 1", "https://faculty-website1.com");
        FacultyBaseDto facultyBaseDto2 = new FacultyBaseDto("faculty124", "org124", "Faculty description 2", "https://faculty-website2.com");
        List<FacultyBaseDto> mockFacultyList = Arrays.asList(facultyBaseDto1, facultyBaseDto2);

        when(facultyRepository.getFacultyByFilter(organisationName, description, websiteUrl, pageable)).thenReturn(mockFacultyList);

        List<FacultyBaseDto> result = facultyService.getFacultyByFilter(organisationName, description, websiteUrl, pageable);

        assertThat(result).isNotNull().hasSize(2);
        assertThat(result.get(0).getUser().getUserName()).isEqualTo("faculty123");
        assertThat(result.get(1).getUser().getUserName()).isEqualTo("faculty124");

        verify(facultyRepository, times(1)).getFacultyByFilter(organisationName, description, websiteUrl, pageable);
    }
}