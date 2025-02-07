package in.ac.iitj.instiapp.Tests.Repository;

import in.ac.iitj.instiapp.Repository.*;
import in.ac.iitj.instiapp.Repository.impl.JWTRefreshTokenRepositoryImpl;
import in.ac.iitj.instiapp.Repository.impl.UserRepositoryImpl;
import in.ac.iitj.instiapp.Tests.Utilities.InitialiseEntities;
import in.ac.iitj.instiapp.database.entities.Auth.JWTRefreshToken;
import in.ac.iitj.instiapp.database.entities.User.User;
import in.ac.iitj.instiapp.database.entities.User.Usertype;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.Transactional;

import static in.ac.iitj.instiapp.Tests.EntityTestData.UserData.USER1;

@DataJpaTest
@Import({InitialiseEntities.InitialiseUser.class,JWTRefreshTokenRepositoryImpl.class })
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class JWTRefreshTokenTest {


    @Autowired
    JWTRefreshTokenRepository jwtRefreshTokenRepository;

    @Autowired
    TestEntityManager testEntityManager;

    @BeforeAll
    public static void setUp(@Autowired JWTRefreshTokenRepository jwtRefreshTokenRepository,
                             @Autowired InitialiseEntities.Initialise initialiseUser) {

        initialiseUser.initialise();


        JWTRefreshToken jwtRefreshToken = new JWTRefreshToken();
        jwtRefreshToken.setRefreshToken("TEST");
        jwtRefreshToken.setDeviceId("TEST");
        User u = new User();
        u.setUserName(USER1.userName);
        jwtRefreshToken.setUser(u);


        jwtRefreshTokenRepository.save(jwtRefreshToken);

    }


    @Test
    @Order(2)
    public void test() {
        //Assertions.assertThat(jwtRefreshTokenRepository.existsByUserNameAndDeviceId(USER1.userName, "TEST")).isTrue();
    }
}
