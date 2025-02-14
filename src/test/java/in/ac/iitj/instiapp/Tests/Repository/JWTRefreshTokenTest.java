package in.ac.iitj.instiapp.Tests.Repository;

import in.ac.iitj.instiapp.Repository.JWTRefreshTokenRepository;
import in.ac.iitj.instiapp.Repository.impl.JWTRefreshTokenRepositoryImpl;
import in.ac.iitj.instiapp.Tests.EntityTestData.AuthAndJwt.JwtRefreshTokenTestData;
import in.ac.iitj.instiapp.Tests.InitialiseEntities.Auth.InitialiseJWTRefreshToken;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Map;

import static in.ac.iitj.instiapp.Tests.EntityTestData.UserData.USER1;

@DataJpaTest
@Import({InitialiseJWTRefreshToken.class, JWTRefreshTokenRepositoryImpl.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class JWTRefreshTokenTest {


    @Autowired
    JWTRefreshTokenRepository jwtRefreshTokenRepository;


    @BeforeAll
    public static void setUp(@Autowired InitialiseJWTRefreshToken initialise) {

        initialise.initialise();

    }

    @Test
    @Order(1)
    @Transactional
    public void testGetRefreshTokenAndTokenExpireTimeByDeviceIdAndUserName() {
        // Call the method to test
        Map<String, Object> result = jwtRefreshTokenRepository.getRefreshTokenAndTokenExpireTimeByDeviceIdAndUserName(JwtRefreshTokenTestData.JWT_REFRESH_TOKEN_1.deviceId, USER1.userName);

        // Assert using AssertJ
        Assertions.assertThat(result).isNotNull().containsEntry("refreshToken", JwtRefreshTokenTestData.JWT_REFRESH_TOKEN_1.refreshToken);

        Assertions.assertThat(((Timestamp) result.get("expirationTime")).toInstant()).isEqualTo(JwtRefreshTokenTestData.JWT_REFRESH_TOKEN_1.refreshTokenExpires);
    }


    @Test
    @Order(2)
    @Transactional
    public void testDeleteByUsernameAndDeviceId() {

        Map<String, Object> result = jwtRefreshTokenRepository.getRefreshTokenAndTokenExpireTimeByDeviceIdAndUserName(JwtRefreshTokenTestData.JWT_REFRESH_TOKEN_1.deviceId, USER1.userName);

        // Assert using AssertJ
        Assertions.assertThat(result).isNotNull().containsEntry("refreshToken", JwtRefreshTokenTestData.JWT_REFRESH_TOKEN_1.refreshToken);

        Assertions.assertThat(((Timestamp) result.get("expirationTime")).toInstant()).isEqualTo(JwtRefreshTokenTestData.JWT_REFRESH_TOKEN_1.refreshTokenExpires);

        jwtRefreshTokenRepository.deleteByUserNameAndDeviceId(USER1.userName, JwtRefreshTokenTestData.JWT_REFRESH_TOKEN_1.deviceId);
        result = jwtRefreshTokenRepository.getRefreshTokenAndTokenExpireTimeByDeviceIdAndUserName(JwtRefreshTokenTestData.JWT_REFRESH_TOKEN_1.deviceId, USER1.userName);
        Assertions.assertThat(result).isEmpty();

    }


    @Test
    @Order(3)
    @Transactional
    public void testDeleteByUserName() {

        Map<String, Object> result = jwtRefreshTokenRepository.getRefreshTokenAndTokenExpireTimeByDeviceIdAndUserName(JwtRefreshTokenTestData.JWT_REFRESH_TOKEN_1.deviceId, USER1.userName);

        // Assert using AssertJ
        Assertions.assertThat(result).isNotNull().containsEntry("refreshToken", JwtRefreshTokenTestData.JWT_REFRESH_TOKEN_1.refreshToken);

        Assertions.assertThat(((Timestamp) result.get("expirationTime")).toInstant()).isEqualTo(JwtRefreshTokenTestData.JWT_REFRESH_TOKEN_1.refreshTokenExpires);

        jwtRefreshTokenRepository.deleteByUserName(USER1.userName);
        result = jwtRefreshTokenRepository.getRefreshTokenAndTokenExpireTimeByDeviceIdAndUserName(JwtRefreshTokenTestData.JWT_REFRESH_TOKEN_1.deviceId, USER1.userName);
        Assertions.assertThat(result).isEmpty();
    }


}
