package in.ac.iitj.instiapp.Tests.Repository;

import in.ac.iitj.instiapp.Repository.JWTRefreshTokenRepository;
import in.ac.iitj.instiapp.Repository.impl.JWTRefreshTokenRepositoryImpl;
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

@DataJpaTest
@Import({JWTRefreshTokenRepositoryImpl.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class JWTRefreshTokenTest {


    @Autowired
    JWTRefreshTokenRepository jwtRefreshTokenRepository;

    @Autowired
    TestEntityManager testEntityManager;

    @BeforeAll
    @Transactional
    public void setUp(@Autowired JWTRefreshTokenRepository jwtRefreshTokenRepository, @Autowired TestEntityManager testEntityManager) {


        JWTRefreshToken jwtRefreshToken = new JWTRefreshToken();
        jwtRefreshToken.setRefreshToken("TEST");
        jwtRefreshToken.setDeviceId("TEST");

        testEntityManager.persist(new User(null, "John Doe", "johndoe123", "johndoe@123", "+911234567890", new Usertype(null, "Student"), null, null, null));

        jwtRefreshTokenRepository.save(jwtRefreshToken);

    }


    @Test
    @Order(2)
    public void test() {
        Assertions.assertThat(jwtRefreshTokenRepository.existsByUserNameAndDeviceId("TEST", "TEST")).isTrue();
    }
}
