package in.ac.iitj.instiapp.Tests.Repository;

import in.ac.iitj.instiapp.Repository.OAuth2TokenRepository;
import in.ac.iitj.instiapp.Tests.EntityTestData.AuthAndJwt.OAuth2TokensTestData;
import in.ac.iitj.instiapp.Tests.InitialiseEntities.User.InitialiseUser;
import in.ac.iitj.instiapp.Tests.Utilities.InitialiseEntities;
import in.ac.iitj.instiapp.Repository.impl.OAuthTokenRepositoryImpl;


import in.ac.iitj.instiapp.database.entities.Auth.OAuth2Tokens;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.core.annotation.Order;

import java.util.Optional;
import static in.ac.iitj.instiapp.Tests.EntityTestData.UserData.USER1;

@DataJpaTest
@Import({InitialiseUser.class, OAuthTokenRepositoryImpl.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OAuth2TokenRepositoryTest {

    @Autowired
    private OAuth2TokenRepository oauth2TokenRepository;

    @BeforeEach
    @Transactional
    public void setUp(@Autowired InitialiseEntities.Initialise initialiseUser) {
        initialiseUser.initialise();
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
        Assertions.assertThat(result)
                .isPresent()  // Ensures the result is present
                .get()  // Get the result from Optional
                .isInstanceOf(OAuth2Tokens.class)  // Ensures it's an instance of OAuth2Tokens
                .extracting(OAuth2Tokens::getDeviceId)  // Extract the deviceId
                .isEqualTo(deviceId);  // Ensure deviceId matches the expected value

        Assertions.assertThat(result.get().getUser().getUserName())
                .isEqualTo(username);  // Ensure username matches the expected value

        Assertions.assertThat(result.get().getAccessToken())
                .isEqualTo(OAuth2TokensTestData.OAUTH2_TOKEN_1.accessToken);  // Ensure accessToken matches the expected value
    }
}
