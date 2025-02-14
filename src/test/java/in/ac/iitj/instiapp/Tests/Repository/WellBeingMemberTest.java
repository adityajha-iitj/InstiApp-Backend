package in.ac.iitj.instiapp.Tests.Repository;


import in.ac.iitj.instiapp.Repository.UserRepository;
import in.ac.iitj.instiapp.Repository.WellBeingRepository;
import in.ac.iitj.instiapp.Repository.impl.UserRepositoryImpl;
import in.ac.iitj.instiapp.Repository.impl.WellBeingRepositoryImpl;
import in.ac.iitj.instiapp.Tests.EntityTestData.UserData;
import in.ac.iitj.instiapp.Tests.InitialiseEntities.User.InitialiseUser;
import in.ac.iitj.instiapp.database.entities.User.User;
import in.ac.iitj.instiapp.database.entities.User.Wellbeingmoderator.WellBeingMember;
import in.ac.iitj.instiapp.payload.User.WellBeingModerator.WellBeingMemberDtoFull;
import in.ac.iitj.instiapp.payload.User.WellBeingModerator.WellBeingMemberDtoLimited;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static in.ac.iitj.instiapp.Tests.EntityTestData.UserData.USER1;
import static in.ac.iitj.instiapp.Tests.EntityTestData.UserData.USER2;
import static in.ac.iitj.instiapp.Tests.EntityTestData.WellBeingMemberData.WELL_BEING_MEMBER_DATA1;
import static in.ac.iitj.instiapp.Tests.EntityTestData.WellBeingMemberData.WELL_BEING_MEMBER_DATA2;


@DataJpaTest
@Import({UserRepositoryImpl.class, WellBeingRepositoryImpl.class, InitialiseUser.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class WellBeingMemberTest {
    @Autowired
    WellBeingRepository wellBeingRepository;

    @Autowired
    private UserRepositoryImpl userRepository;

    @BeforeAll
    public static void setup(@Autowired InitialiseUser initialise,
                             @Autowired WellBeingRepository wellBeingRepository,
                             @Autowired UserRepository userRepository) {
        initialise.initialise();

        WellBeingMember member1 = WELL_BEING_MEMBER_DATA1.toEntity();
        member1.setUser(new User(userRepository.usernameExists(USER1.userName)));
        wellBeingRepository.save(member1);

        WellBeingMember member2 = WELL_BEING_MEMBER_DATA2.toEntity();
        member2.setUser(new User(userRepository.usernameExists(USER2.userName)));
        wellBeingRepository.save(member2);
    }

    @Test
    @Order(1)
    @Rollback(value = true)
    void testUpdateMember() {
        WellBeingMember updatedMember = WELL_BEING_MEMBER_DATA2.toEntity();
        updatedMember.setUser(new User(userRepository.usernameExists(USER2.userName)));
        wellBeingRepository.updateMember(updatedMember);

        WellBeingMemberDtoFull data = wellBeingRepository.findByUsernameFull(USER2.userName);
        Assertions.assertEquals(updatedMember.getQualification(), data.getQualification());
        Assertions.assertEquals(updatedMember.getAvailability(), data.getAvailability());
    }

    @Test
    @Order(2)
    void testFindByUsernameFull() {
        WellBeingMemberDtoFull data = wellBeingRepository.findByUsernameFull(UserData.USER2.userName);
        Assertions.assertEquals(WELL_BEING_MEMBER_DATA2.toEntity().getQualification(), data.getQualification());
        Assertions.assertEquals(WELL_BEING_MEMBER_DATA2.toEntity().getAvailability(), data.getAvailability());
    }

    @Test
    @Order(3)
    void testGetAllMembers() {
        List<WellBeingMemberDtoLimited> user = wellBeingRepository.getAllMembers(PageRequest.of(0, 10));
        Assertions.assertEquals(2, user.size());
        Assertions.assertEquals(WELL_BEING_MEMBER_DATA2.toEntity().getQualification(), user.get(1).getQualification());
        Assertions.assertEquals(WELL_BEING_MEMBER_DATA2.toEntity().getAvailability(), user.get(1).getAvailability());
    }

    @Test
    @Order(4)
    void testMemberExists() {
        Assertions.assertTrue(wellBeingRepository.memberExists(USER1.userName));
        Assertions.assertFalse(wellBeingRepository.memberExists(UserData.USER3.userName));
    }

//    @Test
//    @Order(5)
//    @Rollback(value = true)
//    void testDeleteMember() {
//        Assertions.assertThrows(EmptyResultDataAccessException.class, () ->
//                wellBeingRepository.deleteMember(USER1.userName)
//        );
//    }

}