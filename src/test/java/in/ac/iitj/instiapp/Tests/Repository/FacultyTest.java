package in.ac.iitj.instiapp.Tests.Repository;

import in.ac.iitj.instiapp.Repository.FacultyRepository;
import in.ac.iitj.instiapp.Repository.User.Organisation.OrganisationRepository;
import in.ac.iitj.instiapp.Repository.impl.UserRepositoryImpl;
import in.ac.iitj.instiapp.Tests.EntityTestData.*;
import in.ac.iitj.instiapp.database.entities.User.Faculty.Faculty;
import in.ac.iitj.instiapp.database.entities.User.Organisation.Organisation;
import in.ac.iitj.instiapp.database.entities.User.User;
import in.ac.iitj.instiapp.payload.User.Alumni.AlumniBaseDto;
import in.ac.iitj.instiapp.payload.User.Alumni.AlumniDetailedDto;
import in.ac.iitj.instiapp.payload.User.Faculty.FacultyDetailedDto;
import in.ac.iitj.instiapp.payload.User.Faculty.FacultyBaseDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import in.ac.iitj.instiapp.Tests.Utilities.InitialiseEntities;
import in.ac.iitj.instiapp.Tests.Utilities.Utils;

import java.util.List;
import java.util.Optional;


@DataJpaTest
@Import({InitialiseEntities.InitialiseFaculty.class})
public class FacultyTest {
    private final FacultyRepository facultyRepository;
    private final OrganisationRepository organisationRepository;
    @Autowired
    private UserRepositoryImpl userRepositoryImpl;

    @Autowired
    public FacultyTest(FacultyRepository facultyRepository, OrganisationRepository organisationRepository) {
        this.facultyRepository = facultyRepository;
        this.organisationRepository = organisationRepository;
    }

    @BeforeAll
    public static void setUp(@Autowired InitialiseEntities.InitialiseFaculty initialiseFaculty) {
        initialiseFaculty.initialise();
    }


    @Test
    @Order(1)
    public void testGetFaculty(){
        // as user9 is not Faculty user
        Assertions.assertThatThrownBy(() -> facultyRepository.getFaculty(UserData.USER9.userName))
                .isInstanceOf(EmptyResultDataAccessException.class);



        FacultyBaseDto facultyBaseDto = facultyRepository.getFaculty(UserData.USER11.userName);
        Utils.matchFacultyBaseDto(facultyBaseDto ,FacultyData.FACULTY1,OrganisationData.ORGANISATION1, UserData.USER11);
    }

    @Test
    @Order(2)
    public void testGetDetailedFaculty(){
        // as user9 is not Faculty user
        Assertions.assertThatThrownBy(() -> facultyRepository.getFaculty(UserData.USER9.userName))
                .isInstanceOf(EmptyResultDataAccessException.class);


        FacultyDetailedDto facultyDetailedDto = facultyRepository.getDetailedFaculty(UserData.USER11.userName);
        Assertions.assertThat(facultyDetailedDto.getUser().getUserName()).isEqualTo(UserData.USER11.userName);
        Assertions.assertThat(facultyDetailedDto.getOrganisation().getUser().getUserName()).isEqualTo(OrganisationData.ORGANISATION1);
        Assertions.assertThat(facultyDetailedDto.getDescription()).isEqualTo(FacultyData.FACULTY1.description);
        Assertions.assertThat(facultyDetailedDto.getWebsiteUrl()).isEqualTo(FacultyData.FACULTY1.websiteUrl);
    }

    @Test
    @Order(3)
    public void testGetFacultyByFilter() {
        Pageable pageable = PageRequest.of(0, 10);

        List<FacultyBaseDto> facultyBaseDtoList = facultyRepository.getFacultyByFilter(Optional.of(String.valueOf(OrganisationData.ORGANISATION1)), String.valueOf(Optional.empty()), String.valueOf(Optional.empty()), pageable);
        Utils.matchFacultyBaseDto(facultyBaseDtoList.get(0), FacultyData.FACULTY1, OrganisationData.ORGANISATION1, UserData.USER11);

        facultyBaseDtoList = facultyRepository.getFacultyByFilter(Optional.empty(), String.valueOf(Optional.of(FacultyData.FACULTY1.description)), String.valueOf(Optional.empty()), pageable);
        Utils.matchFacultyBaseDto(facultyBaseDtoList.get(0), FacultyData.FACULTY1, OrganisationData.ORGANISATION1, UserData.USER11);

        facultyBaseDtoList = facultyRepository.getFacultyByFilter(Optional.empty(), String.valueOf(Optional.empty()), String.valueOf(FacultyData.FACULTY1.websiteUrl), pageable);
        Utils.matchFacultyBaseDto(facultyBaseDtoList.get(0), FacultyData.FACULTY1, OrganisationData.ORGANISATION1, UserData.USER11);
    }

    @Test
    @Order(4)
    public void testFacultyExists() {
        Assertions.assertThat(facultyRepository.facultyExists(UserData.USER11.userName)).isNotNull().isNotEqualTo(-1L);
        Assertions.assertThat(facultyRepository.facultyExists(UserData.USER12.userName)).isNotNull().isNotEqualTo(-1L);
        Assertions.assertThat(facultyRepository.facultyExists(UserData.USER13.userName)).isNotNull().isNotEqualTo(-1L);
        Assertions.assertThat(facultyRepository.facultyExists(UserData.USER10.userName)).isEqualTo(-1L);
    }
    @Test

    @Order(5)
    @Rollback(value = true)
    public void testUpdateFaculty() {
        Faculty faculty = FacultyData.FACULTY2.toEntity();
        faculty.setUser(new User(userRepositoryImpl.exists(UserData.USER11.userName)));
        faculty.setOrganisation(new Organisation(organisationRepository.existOrganisation(UserData.USER1.userName)));
        facultyRepository.updateFaculty(faculty);

        FacultyBaseDto facultyBaseDto = facultyRepository.getFaculty(UserData.USER11.userName);
        Utils.matchFacultyBaseDto(
                facultyBaseDto,
                FacultyData.FACULTY2,
                OrganisationData.ORGANISATION2,
                UserData.USER11
        );
    }

    @Test
    @Order(6)
    public void testDeleteFaculty(){
        //TODO
    }



}
