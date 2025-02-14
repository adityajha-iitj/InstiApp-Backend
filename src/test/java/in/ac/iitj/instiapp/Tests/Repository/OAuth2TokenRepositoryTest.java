package in.ac.iitj.instiapp.Tests.Repository;

import in.ac.iitj.instiapp.Repository.OAuth2TokenRepository;
import in.ac.iitj.instiapp.Repository.impl.OAuthTokenRepositoryImpl;
import in.ac.iitj.instiapp.Tests.EntityTestData.AuthAndJwt.OAuth2TokensTestData;
import in.ac.iitj.instiapp.Tests.InitialiseEntities.Auth.InitialiseOAuth2Token;
import in.ac.iitj.instiapp.database.entities.Auth.OAuth2Tokens;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static in.ac.iitj.instiapp.Tests.EntityTestData.UserData.USER1;
import static in.ac.iitj.instiapp.Tests.EntityTestData.UserData.USER4;

@DataJpaTest
@Import({InitialiseOAuth2Token.class, OAuthTokenRepositoryImpl.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OAuth2TokenRepositoryTest {

    @Autowired
    private OAuth2TokenRepository oauth2TokenRepository;

    @BeforeEach
    @Transactional
    public void setUp(@Autowired InitialiseOAuth2Token initialiseOauth2Token) {
        initialiseOauth2Token.initialise();
    }

    @Test
    @Order(1)
    public void testGetByUsernameAndDeviceId() {
        // Given
        String username = USER1.userName;
        String deviceId = OAuth2TokensTestData.OAUTH2_TOKEN_1.deviceId;

        // When
        Optional<OAuth2Tokens> result = oauth2TokenRepository.getByUsernameAndDeviceId(username, deviceId);


        // Then
        Assertions.assertThat(result).isPresent();
        Assertions.assertThat(result.get().getDeviceId()).isEqualTo(deviceId);
        Assertions.assertThat(result.get().getUser().getUserName()).isEqualTo(USER1.userName);
        Assertions.assertThat(result.get().getRefreshToken()).isEqualTo(OAuth2TokensTestData.OAUTH2_TOKEN_1.refreshToken);
        Assertions.assertThat(result.get().getAccessToken()).isEqualTo(OAuth2TokensTestData.OAUTH2_TOKEN_1.accessToken);


        result = oauth2TokenRepository.getByUsernameAndDeviceId(USER4.userName, deviceId);
        Assertions.assertThat(result).isEmpty();
    }


    @Test
    @Order(2)
    public void testUpdateAccessToken() {
        // Given

        Optional<OAuth2Tokens> result = oauth2TokenRepository.getByUsernameAndDeviceId(USER1.userName, OAuth2TokensTestData.OAUTH2_TOKEN_1.deviceId);


        // Then
        Assertions.assertThat(result).isPresent();
        Assertions.assertThat(result.get().getDeviceId()).isEqualTo(OAuth2TokensTestData.OAUTH2_TOKEN_1.deviceId);
        Assertions.assertThat(result.get().getUser().getUserName()).isEqualTo(USER1.userName);
        Assertions.assertThat(result.get().getRefreshToken()).isEqualTo(OAuth2TokensTestData.OAUTH2_TOKEN_1.refreshToken);
        Assertions.assertThat(result.get().getAccessToken()).isEqualTo(OAuth2TokensTestData.OAUTH2_TOKEN_1.accessToken);



        oauth2TokenRepository.updateAccessToken(OAuth2TokensTestData.OAUTH2_TOKEN_1.deviceId, USER1.userName, OAuth2TokensTestData.OAUTH2_TOKEN_4.accessToken);

        result = oauth2TokenRepository.getByUsernameAndDeviceId(USER1.userName, OAuth2TokensTestData.OAUTH2_TOKEN_1.deviceId);
        Assertions.assertThat(result).isPresent();
        Assertions.assertThat(result.get().getDeviceId()).isEqualTo(OAuth2TokensTestData.OAUTH2_TOKEN_1.deviceId);
        Assertions.assertThat(result.get().getUser().getUserName()).isEqualTo(USER1.userName);
        Assertions.assertThat(result.get().getRefreshToken()).isEqualTo(OAuth2TokensTestData.OAUTH2_TOKEN_1.refreshToken);
        Assertions.assertThat(result.get().getAccessToken()).isEqualTo(OAuth2TokensTestData.OAUTH2_TOKEN_4.accessToken);
    }

    @Test
    @Order(3)
    public  void testDeleteByUsername() {
        // Given
        String username = USER1.userName;
        String deviceId = OAuth2TokensTestData.OAUTH2_TOKEN_1.deviceId;

        // When
        Optional<OAuth2Tokens> result = oauth2TokenRepository.getByUsernameAndDeviceId(username, deviceId);

        // Then
        Assertions.assertThat(result).isPresent();
        Assertions.assertThat(result.get().getDeviceId()).isEqualTo(deviceId);
        Assertions.assertThat(result.get().getUser().getUserName()).isEqualTo(USER1.userName);
        Assertions.assertThat(result.get().getRefreshToken()).isEqualTo(OAuth2TokensTestData.OAUTH2_TOKEN_1.refreshToken);
        Assertions.assertThat(result.get().getAccessToken()).isEqualTo(OAuth2TokensTestData.OAUTH2_TOKEN_1.accessToken);

        oauth2TokenRepository.deleteByUsername(username);

        result = oauth2TokenRepository.getByUsernameAndDeviceId(username, deviceId);
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    @Order(4)
    public void testDeleteByDeviceIdAndUserName(){
        String username = USER1.userName;
        String deviceId = OAuth2TokensTestData.OAUTH2_TOKEN_1.deviceId;

        // When
        Optional<OAuth2Tokens> result = oauth2TokenRepository.getByUsernameAndDeviceId(username, deviceId);

        // Then
        Assertions.assertThat(result).isPresent();
        Assertions.assertThat(result.get().getDeviceId()).isEqualTo(deviceId);
        Assertions.assertThat(result.get().getUser().getUserName()).isEqualTo(USER1.userName);
        Assertions.assertThat(result.get().getRefreshToken()).isEqualTo(OAuth2TokensTestData.OAUTH2_TOKEN_1.refreshToken);
        Assertions.assertThat(result.get().getAccessToken()).isEqualTo(OAuth2TokensTestData.OAUTH2_TOKEN_1.accessToken);

        oauth2TokenRepository.deleteByDeviceIdAndUserName(deviceId, username);
        result = oauth2TokenRepository.getByUsernameAndDeviceId(username, deviceId);
        Assertions.assertThat(result).isEmpty();
    }



}
