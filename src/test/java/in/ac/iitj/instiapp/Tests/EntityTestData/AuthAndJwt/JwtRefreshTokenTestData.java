package in.ac.iitj.instiapp.Tests.EntityTestData.AuthAndJwt;

import in.ac.iitj.instiapp.database.entities.Auth.JWTRefreshToken;

import java.time.Instant;

public enum JwtRefreshTokenTestData {

    JWT_REFRESH_TOKEN_1("E2GzQYvPshjX","f1LwT8ZrmJNF",Instant.parse("2027-08-14T16:35:22.836902Z")),
    JWT_REFRESH_TOKEN_2("V9NdWQmZJr3K","X5LgT2pFq7HY",Instant.parse("2026-05-23T08:12:47.836902Z")),
    JWT_REFRESH_TOKEN_3("M1XpRbTvL6YK","P4QsN8WZf2JD",Instant.parse("2028-11-30T21:58:13.836902Z")),
    JWT_REFRESH_TOKEN_4("B7FzJ2NwTQXm","G9KpL5YVrd86",Instant.parse("2029-06-19T14:25:34.836902Z")),
    JWT_REFRESH_TOKEN_5("K4YpT2JXW9Lq","M8VNZF7G6RdJ",Instant.parse("2027-12-02T05:47:59.836902Z")),
    JWT_REFRESH_TOKEN_6("X5TQK9J7L2Wp","YVF4M8NZG6Rd",Instant.parse("2028-04-10T11:32:20.836902Z")),
    JWT_REFRESH_TOKEN_7("Z6LJQ9K7TWXp","YVF8M5N2G4Rd",Instant.parse("2026-09-27T18:15:41.836902Z")),
    JWT_REFRESH_TOKEN_8("P3TQJ9K7WXLz","G6YVF8M5N2Rd",Instant.parse("2029-02-15T23:50:05.836902Z"));


    public  final String deviceId;
    public  final String refreshToken;
    public final Instant refreshTokenExpires;


    JwtRefreshTokenTestData(String deviceId, String refreshToken, Instant refreshTokenExpires) {
        this.refreshToken = refreshToken;
        this.deviceId = deviceId;
        this.refreshTokenExpires = refreshTokenExpires;
    }

    public JWTRefreshToken toEntity() {
        return  new JWTRefreshToken(null, null, this.deviceId, this.refreshToken, this.refreshTokenExpires );
    }


}
