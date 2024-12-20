package in.ac.iitj.instiapp.Tests.Repository;



import in.ac.iitj.instiapp.Repository.UserRepository;
import in.ac.iitj.instiapp.Repository.WellBeingRepository;
import in.ac.iitj.instiapp.Repository.impl.WellBeingRepositoryImpl;
import in.ac.iitj.instiapp.Tests.EntityTestData.UserData;
import in.ac.iitj.instiapp.Tests.Utilities.InitialiseEntities;

import static in.ac.iitj.instiapp.Tests.EntityTestData.WellBeingMemberData.*;

import in.ac.iitj.instiapp.payload.User.WellBeingModerator.WellBeingMemberDtoFull;
import in.ac.iitj.instiapp.payload.User.WellBeingModerator.WellBeingMemberDtoLimited;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;


@DataJpaTest
@Import({WellBeingRepositoryImpl.class , InitialiseEntities.InitialiseUser.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class WellBeingMemberTest {

    @Autowired
    WellBeingRepository wellBeingRepository;
    InitialiseEntities.InitialiseUser initialise;

    @BeforeAll
    public static void setup(@Autowired InitialiseEntities.InitialiseUser initialise , @Autowired WellBeingRepository wellBeingRepository , @Autowired UserRepository userRepository) {
        initialise.initialise();
        wellBeingRepository.save(WELL_BEING_MEMBER_DATA1.toEntity() , userRepository.getUserId(UserData.USER1.userName));
        wellBeingRepository.save(WELL_BEING_MEMBER_DATA2.toEntity() , userRepository.getUserId(UserData.USER2.userName));
    }

    @Test
    @Order(1)
    void testUpdateQualification() {
        wellBeingRepository.updateQualification(UserData.USER1.userName, WELL_BEING_MEMBER_DATA2.toEntity().getQualification());
        WellBeingMemberDtoLimited data = wellBeingRepository.findByUsernameLimited(UserData.USER1.userName);
        Assertions.assertEquals(WELL_BEING_MEMBER_DATA2.toEntity().getQualification(), data.getQualification());
    }

    @Test
    @Order(2)
    void testUpdateAvailability(){
        wellBeingRepository.updateAvailability(UserData.USER2.userName, WELL_BEING_MEMBER_DATA1.toEntity().getAvailability());
        WellBeingMemberDtoLimited data = wellBeingRepository.findByUsernameLimited(UserData.USER2.userName);
        Assertions.assertEquals( WELL_BEING_MEMBER_DATA1.toEntity().getAvailability(), data.getAvailability());
    }

    @Test
    @Order(3)
    void testFindByUsernameFull(){
        WellBeingMemberDtoFull data = wellBeingRepository.findByUsernameFull(UserData.USER2.userName);
        /* availability was changed and set to user1 availability */
        Assertions.assertEquals(WELL_BEING_MEMBER_DATA2.toEntity().getQualification(), data.getQualification());
        Assertions.assertEquals( WELL_BEING_MEMBER_DATA2.toEntity().getAvailability() , data.getAvailability());
        Assertions.assertEquals(UserData.USER2.phoneNumber, data.getUserPhoneNumber());
        Assertions.assertEquals(UserData.USER2.name, data.getUserName());
        Assertions.assertEquals(UserData.USER2.email, data.getUserEmail());
        Assertions.assertEquals(UserData.USER2.userName, data.getUserUserName());
    }

    @Test
    @Order(4)
    void testGetAllMembers(){
        List<WellBeingMemberDtoLimited> user = wellBeingRepository.getAllMembers();
        Assertions.assertEquals(2, user.size());
        Assertions.assertEquals(WELL_BEING_MEMBER_DATA2.toEntity().getQualification(), user.get(1).getQualification());
        /* availability was changed and set to user1 availability */
        Assertions.assertEquals(WELL_BEING_MEMBER_DATA2.toEntity().getAvailability(), user.get(1).getAvailability());
        Assertions.assertEquals(UserData.USER1.userName, user.get(0).getUserUserName());
        Assertions.assertEquals(UserData.USER2.email, user.get(1).getUserEmail());
    }

    @Test
    @Order(5)
    void testMemberExists(){
        Assertions.assertTrue(wellBeingRepository.memberExists(UserData.USER1.userName));
        Assertions.assertFalse(wellBeingRepository.memberExists(UserData.USER3.userName));
    }




}
