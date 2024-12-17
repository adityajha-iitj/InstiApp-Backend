package in.ac.iitj.instiapp.Tests.Repostiory;


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


    @Autowired
    public UserTest(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    @BeforeAll
    public static void setUp(@Autowired InitialiseEntities.InitialiseUser initialiseUser) {
        initialiseUser.initialise();
    }

    @Test
    @Order(1)
    public void testExistByEmail() {
        Assertions.assertThat(userRepository.existsByEmail(USER1.email)).isTrue();
        Assertions.assertThat(userRepository.existsByEmail(USER4.email)).isFalse();
    }


}
