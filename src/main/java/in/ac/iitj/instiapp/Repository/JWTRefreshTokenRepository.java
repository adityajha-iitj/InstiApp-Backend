package in.ac.iitj.instiapp.Repository;

import in.ac.iitj.instiapp.database.entities.Auth.JWTRefreshToken;

import java.util.Map;

public interface JWTRefreshTokenRepository {

    void save(JWTRefreshToken jwtRefreshToken);

    Map<String,Object> getRefreshTokenAndTokenExpireTimeByDeviceIdAndUserName(String deviceId, String userName);

    void updateRefreshTokenAndTokenExpireTimeByDeviceIdAndUserName(JWTRefreshToken jwtRefreshToken);

    void deleteByUserName(String userName);

    Boolean existsByUserNameAndDeviceId(String userName, String deviceId);

}
