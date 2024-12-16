package in.ac.iitj.instiapp.Tests.Repostiory;

import in.ac.iitj.instiapp.Repository.GrievanceRepository;
import in.ac.iitj.instiapp.Repository.impl.GrievanceRepositoryImpl;
import in.ac.iitj.instiapp.Repository.impl.MessRepositoryImpl;
import in.ac.iitj.instiapp.database.entities.Grievance;
import in.ac.iitj.instiapp.database.entities.User.Organisation.OrganisationRole;
import in.ac.iitj.instiapp.database.entities.User.User;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@DataJpaTest
@Import({GrievanceRepositoryImpl.class})
@Rollback(value = false)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class GrivanceTest {

    @Autowired
    private GrievanceRepository grievanceRepository;
    @Autowired
    private EntityManager EntityManager;

    @BeforeAll

    public static void setup(@Autowired GrievanceRepository grievanceRepository , @Autowired EntityManager entityManager) {

        User user = new User();
        user.setUserName("test");
        entityManager.persist(user);

        OrganisationRole role = new OrganisationRole();
        role.setRoleName("admin");
        entityManager.persist(role);

        Grievance grievance = new Grievance();
        grievance.setTitle("testing 1");
        grievance.setDescription("description for testing 1");
        grievance.setOrganisationRole(role);
        grievance.setUserFrom(user);
        grievance.setResolved(false);
        grievanceRepository.addGrievance(grievance);

        Grievance grievance2 = new Grievance();
        grievance2.setTitle("testing 2");
        grievance2.setDescription("description for testing 2");
        grievance2.setOrganisationRole(role);
        grievance2.setUserFrom(user);
        grievance2.setResolved(true);
        grievanceRepository.addGrievance(grievance2);
    }
    @Test
    @Order(1)
    public void testCheckGrievance(){
        boolean bool = grievanceRepository.checkGrievance("testing 1" , "test");
        boolean bool2 = grievanceRepository.checkGrievance("testing 2" , "test1");
        Assertions.assertTrue(bool);
        Assertions.assertFalse(bool2);
    }

    @Test
    @Order(2)
    public void testGetGrievances(){
        List<Grievance> result = grievanceRepository.getGrievances("test");

        Assertions.assertEquals("testing 1", result.get(0).getTitle());
        Assertions.assertEquals("description for testing 1", result.get(0).getDescription());
        Assertions.assertEquals("testing 2", result.get(1).getTitle());
    }



    @Test
    @Order(3)
    public void testDeleteGrievance(){

        String username = "test";
        String grievanceTitle = "testing 1";

        grievanceRepository.deleteGrievance(username, grievanceTitle);

        Grievance deletedGrievance = null;
        try{
            deletedGrievance = grievanceRepository.getGrievance(username, grievanceTitle);
        }catch (RuntimeException e){}

        Assertions.assertNull(deletedGrievance, "Grievance should be deleted and not found");
    }

    @Test
    @Order(4)
    public void testUpdateGrievance(){
        String username = "test";
        String grievanceTitle = "testing 1";
        Boolean newResolved = true;

        try{
            grievanceRepository.updateGrievance(username, grievanceTitle, newResolved);
            Grievance updatedGrievance = grievanceRepository.getGrievance(username, grievanceTitle);

            Assertions.assertTrue(updatedGrievance.getResolved(), "Grievance should be marked as resolved");
        }
        catch (RuntimeException e){
            Assertions.fail("Error occurred while updating the grievance: " + e.getMessage());
        }
    }


    @Test
    @Order(5)
    public void testGetGrievance(){
        String username = "test";
        String grievanceTitle = "testing 1";

        Grievance grievance = grievanceRepository.getGrievance(username , grievanceTitle);

        Assertions.assertNotNull(grievance, "Grievance is null");
        Assertions.assertEquals(grievance.getTitle(), grievanceTitle);
    }



}
