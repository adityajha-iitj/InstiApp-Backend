package in.ac.iitj.instiapp.Tests.Repository;


import in.ac.iitj.instiapp.Repository.UserRepository;
import in.ac.iitj.instiapp.Repository.impl.UserRepositoryImpl;
import in.ac.iitj.instiapp.Tests.Utilities.InitialiseEntities;
import in.ac.iitj.instiapp.Tests.Utilities.Utils;
import in.ac.iitj.instiapp.payload.User.UserBaseDto;
import in.ac.iitj.instiapp.payload.User.UserDetailedDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static in.ac.iitj.instiapp.Tests.EntityTestData.CalendarData.CALENDAR1;
import static in.ac.iitj.instiapp.Tests.EntityTestData.UserData.*;
import static in.ac.iitj.instiapp.Tests.EntityTestData.UserTypeData.*;

@DataJpaTest
@Import({UserRepositoryImpl.class, InitialiseEntities.InitialiseUser.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserTest {


    private final UserRepository userRepository;

    private final InitialiseEntities.Initialise initialise;



    @Autowired
    public UserTest(UserRepository userRepository, InitialiseEntities.InitialiseUser initialise) {
        this.userRepository = userRepository;
        this.initialise = initialise;

    }

    @BeforeAll
    public static void setUp(@Autowired InitialiseEntities.Initialise initialise) {
        initialise.initialise();
    }

    @Test
    @Order(1)
    @Rollback(value = true)
    public void testSaveUserType(){

        Assertions.assertThat(userRepository.userTypeExists(USER_TYPE1.name)).isNotNull().isNotEqualTo(-1L);
        Assertions.assertThat(userRepository.userTypeExists(USER_TYPE4.name)).isEqualTo(-1L);

        userRepository.save(USER_TYPE4.toEntity());

        Assertions.assertThat(userRepository.userTypeExists(USER_TYPE4.name)).isNotNull().isNotEqualTo(-1L);


        Assertions.assertThatThrownBy(() -> userRepository.save(USER_TYPE1.toEntity()))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @Order(2)
    public void testGetAllUserTypes(){
        Assertions.assertThat(userRepository.getAllUserTypes(PageRequest.of(0,10)))
                .containsExactlyInAnyOrder(USER_TYPE1.name, USER_TYPE2.name,USER_TYPE3.name, USER_TYPE5.name,USER_TYPE6.name,USER_TYPE7.name);
    }

    @Test
    @Order(3)
    public void testExistsUsertype(){
        Assertions.assertThat(userRepository.userTypeExists(USER_TYPE1.name)).isNotNull().isNotEqualTo(-1L);
        Assertions.assertThat(userRepository.userTypeExists(USER_TYPE2.name)).isNotNull().isNotEqualTo(-1L);
        Assertions.assertThat(userRepository.userTypeExists(USER_TYPE3.name)).isNotNull().isNotEqualTo(-1L);
        Assertions.assertThat(userRepository.userTypeExists(USER_TYPE4.name)).isEqualTo(-1L);
    }

    @Test
    @Order(4)
    @Rollback(value = true)
    public void testUpdateUserType(){
        Assertions.assertThatThrownBy(() -> userRepository.update(USER_TYPE4.name,USER_TYPE1.name))
                .isInstanceOf(EmptyResultDataAccessException.class);
        Assertions.assertThatThrownBy(() -> userRepository.update(USER_TYPE1.name,USER_TYPE2.name))
                .isInstanceOf(DataIntegrityViolationException.class);


        Assertions.assertThat(userRepository.userTypeExists(USER_TYPE1.name)).isNotNull().isNotEqualTo(-1L);
        Assertions.assertThat(userRepository.userTypeExists(USER_TYPE4.name)).isEqualTo(-1L);


        userRepository.update(USER_TYPE1.name, USER_TYPE4.name);

        Assertions.assertThat(userRepository.userTypeExists(USER_TYPE4.name)).isNotNull().isNotEqualTo(-1L);
        Assertions.assertThat(userRepository.userTypeExists(USER_TYPE1.name)).isEqualTo(-1L);
    }

    // TODO
@Test
@Order(5)
    public void testDeleteUserType(){
        Assertions.assertThat(userRepository.userTypeExists(USER_TYPE1.name)).isNotNull().isNotEqualTo(-1L);
        userRepository.delete(USER_TYPE1.name);
        Assertions.assertThat(userRepository.userTypeExists(USER_TYPE1.name)).isEqualTo(-1L);
    }


    @Test
    @Order(6)
    public void testGetUserLimited(){
        Assertions.assertThatThrownBy(() ->{
            userRepository.getUserLimited(USER4.userName);
        }).isInstanceOf(EmptyResultDataAccessException.class);

        UserBaseDto userBaseDto = userRepository.getUserLimited(USER1.userName);


        Utils.matchUserBaseDto(userBaseDto, USER1, USER_TYPE1);




    }

    @Test
    @Order(7)
    public void testGetUserDetailed(){
        Assertions.assertThatThrownBy(() ->{
            userRepository.getUserDetailed(USER4.userName,true);
        }).isInstanceOf(EmptyResultDataAccessException.class);


        UserDetailedDto userDetailedDto = userRepository.getUserDetailed(USER1.userName,true);
        Assertions.assertThat(userDetailedDto.getUserName()).isEqualTo(USER1.userName);
        Assertions.assertThat(userDetailedDto.getName()).isEqualTo(USER1.name);
        Assertions.assertThat(userDetailedDto.getEmail()).isEqualTo(USER1.email);
        Assertions.assertThat(userDetailedDto.getAvatarUrl()).isEqualTo(USER1.avatarUrl);
        Assertions.assertThat(userDetailedDto.getUserTypeName()).isEqualTo(USER_TYPE1.name);
        Assertions.assertThat(userDetailedDto.getCalendarPublicId()).isEqualTo(CALENDAR1.publicId);
        Assertions.assertThat(userDetailedDto.getPhoneNumber()).isEqualTo(USER1.phoneNumber);

        UserDetailedDto userDetailedDto1 = userRepository.getUserDetailed(USER1.userName,false);
        Assertions.assertThat(userDetailedDto1.getUserName()).isEqualTo(USER1.userName);
        Assertions.assertThat(userDetailedDto1.getName()).isEqualTo(USER1.name);
        Assertions.assertThat(userDetailedDto1.getEmail()).isEqualTo(USER1.email);
        Assertions.assertThat(userDetailedDto1.getAvatarUrl()).isEqualTo(USER1.avatarUrl);
        Assertions.assertThat(userDetailedDto1.getUserTypeName()).isEqualTo(USER_TYPE1.name);
        Assertions.assertThat(userDetailedDto1.getCalendarPublicId()).isNull();
        Assertions.assertThat(userDetailedDto1.getPhoneNumber()).isNull();
    }

    @Test
    @Order(8)
    public void testGetListUserLimitedByUserType(){
        List<UserBaseDto> userBaseDtoList = userRepository.getListUserLimitedByUsertype(USER_TYPE1.name, PageRequest.of(0,10));

        Assertions.assertThat(userBaseDtoList.size()).isEqualTo(1);


        Utils.matchUserBaseDto(userBaseDtoList.get(0),USER1,USER_TYPE1);
    }

@Test
@Order(9)
    public void testGetOrganisationPermission(){

    }



    @Test
    @Order(10)
    //TODO
    public void testGetOrganisationRoleDTOsByUsername(){

    }

    @Test
    @Order(11)
    public void testUserNameExist(){
        Assertions.assertThat(userRepository.usernameExists(USER1.userName)).isNotNull().isNotEqualTo(-1L);
        Assertions.assertThat(userRepository.usernameExists(USER2.userName)).isNotNull().isNotEqualTo(-1L);
        Assertions.assertThat(userRepository.usernameExists(USER3.userName)).isNotNull().isNotEqualTo(-1L);
        Assertions.assertThat(userRepository.usernameExists(USER4.userName)).isEqualTo(-1L);
    }

    @Test
    @Order(12)
    @Rollback(value = true)
    public void testUpdateOauth2Info(){

        Utils.matchUserBaseDto(userRepository.getUserLimited(USER1.userName),USER1,USER_TYPE1);


        userRepository.updateOauth2Info(USER2.name, USER2.avatarUrl, USER1.userName);


        UserBaseDto userBaseDto = userRepository.getUserLimited(USER1.userName);
        Assertions.assertThat(userBaseDto.getUserTypeName()).isEqualTo(USER_TYPE1.name);
        Assertions.assertThat(userBaseDto.getUserName()).isEqualTo(USER1.userName);
        Assertions.assertThat(userBaseDto.getAvatarUrl()).isEqualTo(USER2.avatarUrl);
        Assertions.assertThat(userBaseDto.getEmail()).isEqualTo(USER1.email);
        Assertions.assertThat(userBaseDto.getName()).isEqualTo(USER2.name);
    }


    @Test
    @Order(13)
    @Rollback(value = true)
    public void testSetUserType(){
        Assertions.assertThatThrownBy(() ->{
            userRepository.setUserType(USER4.userName, USER_TYPE2.name);
        }).isInstanceOf(EmptyResultDataAccessException.class);

        Assertions.assertThatThrownBy(() ->{
            userRepository.setUserType(USER1.userName, USER_TYPE4.name);
        }).isInstanceOf(EmptyResultDataAccessException.class);

        Assertions.assertThat(userRepository.getListUserLimitedByUsertype(USER_TYPE1.name, PageRequest.of(0,10)).get(0).getUserName())
                .isEqualTo(USER1.userName);


        userRepository.setUserType(USER1.userName, USER_TYPE2.name);

        Assertions.assertThat(userRepository.getUserLimited(USER1.userName).getUserTypeName())
                .isEqualTo(USER_TYPE2.name);
    }


    @Test
    @Order(14)
    public void testUpdatePhoneNumber(){

        Assertions.assertThatThrownBy(() ->{
            userRepository.updatePhoneNumber(USER4.userName, USER2.phoneNumber);
        }).isInstanceOf(EmptyResultDataAccessException.class);


        Assertions.assertThat(userRepository.getUserDetailed(USER1.userName,true).getPhoneNumber()).isEqualTo(USER1.phoneNumber);

        userRepository.updatePhoneNumber(USER1.userName,USER4.phoneNumber);

        Assertions.assertThat(userRepository.getUserDetailed(USER1.userName,true).getPhoneNumber()).isEqualTo(USER4.phoneNumber);
    }














}
