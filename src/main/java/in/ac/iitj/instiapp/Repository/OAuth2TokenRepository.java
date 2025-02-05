package in.ac.iitj.instiapp.Repository;

import in.ac.iitj.instiapp.database.entities.Auth.OAuth2Tokens;

import java.util.Optional;

public interface OAuth2TokenRepository {

    /**
     * If it exists it updates acessToken and refreshToken and expirationTime
     * @param oAuth2Token userId shouldn't be null
     *                    userName shouldn't be null
     */
    void save(OAuth2Tokens oAuth2Token);

    /**
     * @param username
     * @param deviceId
     * @return only username filled in Oauth2Tokens <br>
     *         Optional.empty() if it doesn't exist
     */
    Optional<OAuth2Tokens> getByUsernameAndDeviceId(String username, String deviceId);

    void updateAccessToken(String deviceId, String userName, String accessToken);

    void deleteByUsername(String username);

    void deleteByDeviceIdAndUserName(String deviceId, String userName);

}
