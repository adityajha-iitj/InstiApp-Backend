package in.ac.iitj.instiapp.Tests.Repository;

import in.ac.iitj.instiapp.Repository.*;
import in.ac.iitj.instiapp.Repository.impl.JWTRefreshTokenRepositoryImpl;
import in.ac.iitj.instiapp.Repository.impl.UserRepositoryImpl;
import in.ac.iitj.instiapp.Tests.EntityTestData.AuthAndJwt.JwtRefreshTokenTestData;
import in.ac.iitj.instiapp.Tests.InitialiseEntities.User.InitialiseUser;
import in.ac.iitj.instiapp.Tests.Utilities.InitialiseEntities;
import in.ac.iitj.instiapp.database.entities.Auth.JWTRefreshToken;
import in.ac.iitj.instiapp.database.entities.User.User;
import in.ac.iitj.instiapp.database.entities.User.Usertype;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static in.ac.iitj.instiapp.Tests.EntityTestData.UserData.USER1;

@DataJpaTest
@Import({InitialiseUser.class,JWTRefreshTokenRepositoryImpl.class })
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class JWTRefreshTokenTest {


    @Autowired
    JWTRefreshTokenRepository jwtRefreshTokenRepository;

    @Autowired
    TestEntityManager testEntityManager;

    @BeforeAll
    public static void setUp(@Autowired InitialiseEntities.Initialise initialiseUser) {

        initialiseUser.initialise();

    }

    @Test
    @Order(1)
    @Transactional
    public void testGetRefreshTokenAndTokenExpireTimeByDeviceIdAndUserName() {
        String deviceId = JwtRefreshTokenTestData.JWT_REFRESH_TOKEN_1.deviceId;
        String userName = USER1.userName;

        // Insert the test data into the database
        JWTRefreshToken jwtRefreshToken = new JWTRefreshToken();
        jwtRefreshToken.setDeviceId(deviceId);
        jwtRefreshToken.setRefreshToken(JwtRefreshTokenTestData.JWT_REFRESH_TOKEN_1.refreshToken);
        jwtRefreshToken.setRefreshTokenExpires(JwtRefreshTokenTestData.JWT_REFRESH_TOKEN_1.refreshTokenExpires);
        User user = new User();
        user.setUserName(userName);
        jwtRefreshToken.setUser(user);

        jwtRefreshTokenRepository.save(jwtRefreshToken);

        // Call the method to test
        Map<String, Object> result = jwtRefreshTokenRepository.getRefreshTokenAndTokenExpireTimeByDeviceIdAndUserName(deviceId, userName);

        // Assert using AssertJ
        Assertions.assertThat(result)
                .isNotNull()
                .containsKey("refreshToken")
                .containsKey("expirationTime");

        Assertions.assertThat(result.get("refreshToken"))
                .isEqualTo(JwtRefreshTokenTestData.JWT_REFRESH_TOKEN_1.refreshToken);

        Assertions.assertThat(result.get("expirationTime"))
                .isEqualTo(JwtRefreshTokenTestData.JWT_REFRESH_TOKEN_1.refreshTokenExpires.toString());
    }


}
