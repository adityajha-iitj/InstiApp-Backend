package in.ac.iitj.instiapp.Tests.Repository;

import in.ac.iitj.instiapp.Repository.GrievanceRepository;
import in.ac.iitj.instiapp.Repository.User.Organisation.OrganisationRepository;
import in.ac.iitj.instiapp.Tests.Utilities.InitialiseEntities;
import in.ac.iitj.instiapp.database.entities.Grievance;
import in.ac.iitj.instiapp.payload.GrievanceDto;
import in.ac.iitj.instiapp.Repository.MediaRepository;
import in.ac.iitj.instiapp.Repository.UserRepository;
import in.ac.iitj.instiapp.Repository.User.Organisation.OrganisationRoleRepository;
import org.assertj.core.api.Assertions;
import org.hibernate.annotations.NaturalId;
import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import in.ac.iitj.instiapp.Tests.EntityTestData.GrievanceData;
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
@Import({InitialiseEntities})
public class GrivanceTest {

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private MediaRepository mediaRepository;
    @Autowired
    private GrievanceRepository grievanceRepository;

    @Autowired
    private final OrganisationRoleRepository organisationRoleRepository;


    @Autowired
    public GrivanceTest(MediaRepository mediaRepository, UserRepository userRepository, OrganisationRoleRepository organisationRoleRepository) {
        this.mediaRepository = mediaRepository;
        this.organisationRoleRepository = organisationRoleRepository;
        this.userRepository = userRepository;
    }

    @BeforeAll
    public static void setUp(@Autowired InitialiseEntities.InitialiseUser initialiseUser) {
        initialiseUser.initialise();
    }

    @Test
    @Order(1)
    public void testGetGrievance(String publicId){
        GrievanceDto grievanceDto = grievanceRepository.getGrievance(GrievanceData.GRIEVANCE1.publicId);
        Utils.matchGrievanceDto(grievanceDto,GrievanceData.GRIEVANCE1);
    }

    @Test
    @Order(2)
    public void testGetGrievancesByFilter() {
        Pageable pageable = PageRequest.of(0, 10);

        // Test case 1: Filter by title
        List<GrievanceDto> grievanceDtoList = grievanceRepository.getGrievancesByFilter(Optional.of("Regarding the Water coolers"), Optional.empty(), Optional.empty(),Optional.empty(), pageable);
        Utils.matchGrievanceDto(grievanceDtoList.get(0), GrievanceData.GRIEVANCE1);
        Utils.matchGrievanceDto(grievanceDtoList.get(1), GrievanceData.GRIEVANCE2);

        // Test case 2: Filter by description
        grievanceDtoList = grievanceRepository.getGrievancesByFilter(Optional.empty(), Optional.of("The Air Conditioner in my room O4 W314 is not working properly"), Optional.empty(),Optional.empty(), pageable);
        Utils.matchGrievanceDto(grievanceDtoList.get(0), GrievanceData.GRIEVANCE1);

        // Test case 3: Filter by resolved
        grievanceDtoList = grievanceRepository.getGrievancesByFilter(Optional.empty(), Optional.empty(), Optional.empty(),Optional.of(false), pageable);
        Utils.matchGrievanceDto(grievanceDtoList.get(0), GrievanceData.GRIEVANCE1);

        // Test case 4: Filter by all fields
        grievanceDtoList = grievanceRepository.getGrievancesByFilter(Optional.of("Regarding the AC in my room"), Optional.of("The Air Conditioner in my room O4 W314 is not working properly"), Optional.empty(),Optional.of(false), pageable);
        Utils.matchGrievanceDto(grievanceDtoList.get(0), GrievanceData.GRIEVANCE1);
    }

    @Test
    @Order(3)
    public void testGrievanceExists(){
        Assertions.assertThat(grievanceRepository.existGrievance(GrievanceData.GRIEVANCE1.publicId)).isNotNull().isNotEqualTo(-1L);
        Assertions.assertThat(grievanceRepository.existGrievance(GrievanceData.GRIEVANCE2.publicId)).isNotNull().isNotEqualTo(-1L);
        Assertions.assertThat(grievanceRepository.existGrievance(GrievanceData.GRIEVANCE3.publicId)).isNotNull().isNotEqualTo(-1L);
        Assertions.assertThat(grievanceRepository.existGrievance(GrievanceData.GRIEVANCE4.publicId)).isNotNull().isNotEqualTo(-1L);
    }

    @Test
    @Order(4)
    @Rollback(value = true)
    public void testUpdateGrievance() {
        // Step 1: Set up the grievance entity using GrievanceData
        Grievance grievance = GrievanceData.GRIEVANCE1.toEntity();
        grievance.setTitle("Updated Title");
        grievance.setDescription("Updated Description");
        grievance.setResolved(true);

        // Step 2: Perform the update operation
        grievanceRepository.updateGrievance(GrievanceData.GRIEVANCE1.publicId,grievance);

        // Step 3: Fetch the updated grievance and perform assertions
        GrievanceDto updatedGrievanceDto = grievanceRepository.getGrievance(GrievanceData.GRIEVANCE1.publicId);

        // Step 4: Assert that the grievance was updated correctly
        Assertions.assertThat(updatedGrievanceDto.getTitle()).isEqualTo("Updated Title");
        Assertions.assertThat(updatedGrievanceDto.getDescription()).isEqualTo("Updated Description");
        Assertions.assertThat(updatedGrievanceDto.getResolved()).isTrue();
    }



}



