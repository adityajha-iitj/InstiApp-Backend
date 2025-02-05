package in.ac.iitj.instiapp.Repository;

import in.ac.iitj.instiapp.database.entities.Auth.JWTRefreshToken;

import java.util.Map;

public interface JWTRefreshTokenRepository {

    /**
     * If refresh token already exists for the DeviceId and Username then it updates the info
     * @param jwtRefreshToken userId shouldn't be null
     *                        userName shouldn't be null
     */
    void save(JWTRefreshToken jwtRefreshToken);


    /**
     * @param deviceId
     * @param userName
     * @return Map with keys <b>refreshToken</b> -> string, <b>expirationTime</b> -> Instant<br>
     *         Empty map if it doesn't exist
     *
     */
    Map<String,Object> getRefreshTokenAndTokenExpireTimeByDeviceIdAndUserName(String deviceId, String userName);


    void deleteByUserNameAndDeviceId(String userName, String deviceId);

    void deleteByUserName(String userName);
}
