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

import static in.ac.iitj.instiapp.Tests.EntityTestData.UserData.USER1;
import static in.ac.iitj.instiapp.Tests.EntityTestData.UserData.USER4;

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
    public void testExistByEmail() {
        Assertions.assertThat(userRepository.existsByEmail(USER1.email)).isTrue();
        Assertions.assertThat(userRepository.existsByEmail(USER4.email)).isFalse();
    }

    @Test
    @Order(2)
    public void testExistByUsername() {
        Assertions.assertThat(userRepository.existsByUsername(USER1.email)).isTrue();
        Assertions.assertThat(userRepository.existsByUsername(USER4.email)).isFalse();
    }

    @Test
    @Order(3)
    public void testExistByPhoneNumber() {
        Assertions.assertThat(userRepository.existsByPhoneNumber(USER1.phoneNumber)).isTrue();
        Assertions.assertThat(userRepository.existsByPhoneNumber(USER4.phoneNumber)).isFalse();
    }

    @Test
    @Order(4)
    public void testUpdatePhoneNumber() {
        String newPhoneNumber = "111111111";
        String username = USER1.userName;

        userRepository.updatePhoneNumber(newPhoneNumber, username);
        Assertions.assertThat(userRepository.existsByPhoneNumber(newPhoneNumber)).isTrue();
        Assertions.assertThat(userRepository.existsByPhoneNumber(USER1.phoneNumber)).isFalse();
    }


}
