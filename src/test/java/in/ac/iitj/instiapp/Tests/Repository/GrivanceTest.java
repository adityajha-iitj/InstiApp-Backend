package in.ac.iitj.instiapp.Tests.Repository;

import in.ac.iitj.instiapp.Repository.GrievanceRepository;
import in.ac.iitj.instiapp.Repository.User.Organisation.OrganisationRepository;
import in.ac.iitj.instiapp.Repository.impl.GrievanceRepositoryImpl;
import in.ac.iitj.instiapp.Tests.EntityTestData.*;
import in.ac.iitj.instiapp.Tests.Utilities.InitialiseEntities;
import in.ac.iitj.instiapp.database.entities.Grievance;
import in.ac.iitj.instiapp.database.entities.User.Organisation.Organisation;
import in.ac.iitj.instiapp.database.entities.User.Organisation.OrganisationRole;
import in.ac.iitj.instiapp.database.entities.User.User;
import in.ac.iitj.instiapp.payload.GrievanceDto;
import in.ac.iitj.instiapp.Repository.MediaRepository;
import in.ac.iitj.instiapp.Repository.UserRepository;
import in.ac.iitj.instiapp.Repository.User.Organisation.OrganisationRoleRepository;
import in.ac.iitj.instiapp.payload.User.Organisation.OrganisationRoleDto;
import org.assertj.core.api.Assertions;
import org.hibernate.annotations.NaturalId;
import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import in.ac.iitj.instiapp.Tests.Utilities.Utils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;


import java.util.List;
import java.util.Optional;

import static jdk.dynalink.linker.support.Guards.isNotNull;

@DataJpaTest
@Import({InitialiseEntities.InitialiseGrievance.class, GrievanceRepositoryImpl.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class GrivanceTest {

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private MediaRepository mediaRepository;
    @Autowired
    private GrievanceRepository grievanceRepository;
    @Autowired
    private final OrganisationRepository organisationRepository;

    @Autowired
    private final OrganisationRoleRepository organisationRoleRepository;


    @Autowired
    public GrivanceTest(MediaRepository mediaRepository, UserRepository userRepository, OrganisationRoleRepository organisationRoleRepository, GrievanceRepository grievanceRepository, OrganisationRepository organisationRepository) {
        this.mediaRepository = mediaRepository;
        this.organisationRoleRepository = organisationRoleRepository;
        this.userRepository = userRepository;
        this.grievanceRepository = grievanceRepository;
        this.organisationRepository = organisationRepository;
    }

    @BeforeAll
    public static void setUp(@Autowired InitialiseEntities.InitialiseGrievance initialiseGrievance) {
        initialiseGrievance.initialise();

    }

    @Test
    @Order(1)
    public void testGetGrievance(){
        GrievanceDto grievanceDto = grievanceRepository.getGrievance(GrievanceData.GRIEVANCE1.publicId);
        Utils.matchGrievanceDto(grievanceDto,GrievanceData.GRIEVANCE1,OrganisationRoleData.ORGANISATION_ROLE1);
    }

    @Test
    @Order(2)
    public void testGetGrievancesByFilter() {
        Pageable pageable = PageRequest.of(0, 10);

        // Test case 1: Filter by title
        List<GrievanceDto> grievanceDtoList = grievanceRepository.getGrievancesByFilter(Optional.of("Regarding the Water coolers"), Optional.empty(), Optional.empty(),Optional.empty(), pageable);
        Utils.matchGrievanceDto(grievanceDtoList.get(0), GrievanceData.GRIEVANCE1,OrganisationRoleData.ORGANISATION_ROLE1);

        // Test case 2: Filter by description
        grievanceDtoList = grievanceRepository.getGrievancesByFilter(Optional.empty(), Optional.of("The water coolers are not properly functioning"), Optional.empty(),Optional.empty(), pageable);
        Utils.matchGrievanceDto(grievanceDtoList.get(0), GrievanceData.GRIEVANCE1,OrganisationRoleData.ORGANISATION_ROLE1);

        // Test case 3: Filter by resolved
        grievanceDtoList = grievanceRepository.getGrievancesByFilter(Optional.empty(), Optional.empty(), Optional.empty(),Optional.of(false), pageable);
        Utils.matchGrievanceDto(grievanceDtoList.get(0), GrievanceData.GRIEVANCE1,OrganisationRoleData.ORGANISATION_ROLE1);

        // Test case 4: Filter by all fields
        grievanceDtoList = grievanceRepository.getGrievancesByFilter(Optional.of("Regarding the Water coolers"), Optional.of("The water coolers are not properly functioning"), Optional.empty(),Optional.of(false), pageable);
        Utils.matchGrievanceDto(grievanceDtoList.get(0), GrievanceData.GRIEVANCE1,OrganisationRoleData.ORGANISATION_ROLE1);
    }

    @Test
    @Order(3)
    public void testGrievanceExists(){
        Assertions.assertThat(grievanceRepository.existGrievance(GrievanceData.GRIEVANCE1.publicId)).isNotNull().isNotEqualTo(-1L);
        Assertions.assertThat(grievanceRepository.existGrievance(GrievanceData.GRIEVANCE2.publicId)).isNotNull().isNotEqualTo(-1L);
        Assertions.assertThat(grievanceRepository.existGrievance(GrievanceData.GRIEVANCE3.publicId)).isNotNull().isNotEqualTo(-1L);
    }

    @Test
    @Order(4)
    @Rollback(value = true)
    public void testUpdateGrievance() {
        Grievance grievance = GrievanceData.GRIEVANCE1.toEntity();
        grievance.setTitle(GrievanceData.GRIEVANCE2.Title);
        grievance.setDescription(GrievanceData.GRIEVANCE2.Description);
        grievance.setResolved(GrievanceData.GRIEVANCE2.resolved);
        grievance.setOrganisationRole(new OrganisationRole(organisationRoleRepository.existOrganisationRole(UserData.USER2.userName, OrganisationRoleData.ORGANISATION_ROLE2.roleName)));

        grievanceRepository.updateGrievance(GrievanceData.GRIEVANCE1.publicId,grievance);

        GrievanceDto updatedGrievance = grievanceRepository.getGrievance(GrievanceData.GRIEVANCE1.publicId);


        Utils.matchGrievanceDto(
                updatedGrievance,
                GrievanceData.GRIEVANCE2,
                OrganisationRoleData.ORGANISATION_ROLE2
        );
    }
}



