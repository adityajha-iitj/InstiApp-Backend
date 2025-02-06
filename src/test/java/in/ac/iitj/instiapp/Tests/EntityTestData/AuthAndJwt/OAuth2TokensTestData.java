package in.ac.iitj.instiapp.Tests.EntityTestData.AuthAndJwt;

import in.ac.iitj.instiapp.database.entities.Auth.OAuth2Tokens;

import java.sql.Timestamp;

public enum OAuth2TokensTestData {

    OAUTH2_TOKEN_1( "Device123", "AccessToken123", "RefreshToken123"),
    OAUTH2_TOKEN_2(  "Device456", "AccessToken456", "RefreshToken456"),
    OAUTH2_TOKEN_3( "Device789", "AccessToken789", "RefreshToken789"),
    OAUTH2_TOKEN_4( "Device101", "AccessToken101", "RefreshToken101"),
    OAUTH2_TOKEN_5( "Device202", "AccessToken202", "RefreshToken202"),
    OAUTH2_TOKEN_6( "Device303", "AccessToken303", "RefreshToken303"),
    OAUTH2_TOKEN_7("Device404", "AccessToken404", "RefreshToken404"),
    OAUTH2_TOKEN_8( "Device505", "AccessToken505", "RefreshToken505");

    public final String deviceId;
    public final String accessToken;
    public final String refreshToken;

    OAuth2TokensTestData(String deviceId, String accessToken, String refreshToken) {
        this.deviceId = deviceId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;

    }

    public OAuth2Tokens toEntity() {
        return new OAuth2Tokens(
                null,
                null,
                this.deviceId,
                this.accessToken,
                this.refreshToken
        );
    }
}
