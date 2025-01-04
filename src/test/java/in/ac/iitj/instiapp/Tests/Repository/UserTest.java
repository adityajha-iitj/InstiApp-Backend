package in.ac.iitj.instiapp.Tests.Repository;


import in.ac.iitj.instiapp.Repository.UserRepository;
import in.ac.iitj.instiapp.Repository.impl.UserRepositoryImpl;
import in.ac.iitj.instiapp.Tests.Utilities.InitialiseEntities;
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

import static in.ac.iitj.instiapp.Tests.EntityTestData.UserData.USER1;
import static in.ac.iitj.instiapp.Tests.EntityTestData.UserData.USER4;
import static in.ac.iitj.instiapp.Tests.EntityTestData.UserTypeData.*;

@DataJpaTest
@Import({UserRepositoryImpl.class, InitialiseEntities.InitialiseUser.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserTest {


    private final UserRepository userRepository;

    private final InitialiseEntities.Initialise initialise;

    @Autowired
    public UserTest(UserRepository userRepository, InitialiseEntities.Initialise initialise) {
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

        Assertions.assertThat(userRepository.exists(USER_TYPE1.name)).isNotNull().isNotEqualTo(-1L);
        Assertions.assertThat(userRepository.exists(USER_TYPE4.name)).isEqualTo(-1L);

        userRepository.save(USER_TYPE4.toEntity());

        Assertions.assertThat(userRepository.exists(USER_TYPE4.name)).isNotNull().isNotEqualTo(-1L);


        Assertions.assertThatThrownBy(() -> userRepository.save(USER_TYPE1.toEntity()))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @Order(2)
    public void testGetAllUserTypes(){
        Assertions.assertThat(userRepository.getAllUserTypes(PageRequest.of(0,10)))
                .containsExactlyInAnyOrder(USER_TYPE1.name, USER_TYPE2.name,USER_TYPE3.name);
    }

    @Test
    @Order(3)
    public void testExistsUsertype(){
        Assertions.assertThat(userRepository.exists(USER_TYPE1.name)).isNotNull().isNotEqualTo(-1L);
        Assertions.assertThat(userRepository.exists(USER_TYPE2.name)).isNotNull().isNotEqualTo(-1L);
        Assertions.assertThat(userRepository.exists(USER_TYPE3.name)).isNotNull().isNotEqualTo(-1L);
        Assertions.assertThat(userRepository.exists(USER_TYPE4.name)).isEqualTo(-1L);
    }

    @Test
    @Order(4)
    @Rollback(value = true)
    public void testUpdateUserType(){
        Assertions.assertThatThrownBy(() -> userRepository.update(USER_TYPE4.name,USER_TYPE1.name))
                .isInstanceOf(EmptyResultDataAccessException.class);
        Assertions.assertThatThrownBy(() -> userRepository.update(USER_TYPE1.name,USER_TYPE2.name))
                .isInstanceOf(DataIntegrityViolationException.class);


        Assertions.assertThat(userRepository.exists(USER_TYPE1.name)).isNotNull().isNotEqualTo(-1L);
        Assertions.assertThat(userRepository.exists(USER_TYPE4.name)).isEqualTo(-1L);


        userRepository.update(USER_TYPE1.name, USER_TYPE4.name);

        Assertions.assertThat(userRepository.exists(USER_TYPE4.name)).isNotNull().isNotEqualTo(-1L);
        Assertions.assertThat(userRepository.exists(USER_TYPE1.name)).isEqualTo(-1L);
    }

    // TODO
@Test
@Order(5)
    public void testDeleteUserType(){
        Assertions.assertThat(userRepository.exists(USER_TYPE1.name)).isNotNull().isNotEqualTo(-1L);
        userRepository.delete(USER_TYPE1.name);
        Assertions.assertThat(userRepository.exists(USER_TYPE1.name)).isEqualTo(-1L);
    }




}
