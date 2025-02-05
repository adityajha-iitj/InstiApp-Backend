package in.ac.iitj.instiapp.Repository.impl;

import in.ac.iitj.instiapp.Repository.JWTRefreshTokenRepository;
import in.ac.iitj.instiapp.database.entities.Auth.JWTRefreshToken;
import in.ac.iitj.instiapp.database.entities.User.User;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.Map;

@Repository
public class JWTRefreshTokenRepositoryImpl implements JWTRefreshTokenRepository {
    private final EntityManager entityManager;
    private final JdbcTemplate jdbcTemplate;


    public JWTRefreshTokenRepositoryImpl(EntityManager entityManager, JdbcTemplate jdbcTemplate) {
        this.entityManager = entityManager;
        this.jdbcTemplate = jdbcTemplate;
    }

    /*Username should not be null*/
    @Transactional
    @Override
    public void save(JWTRefreshToken jwtRefreshToken) {


        if (!getRefreshTokenAndTokenExpireTimeByDeviceIdAndUserName(jwtRefreshToken.getUser().getUserName(), jwtRefreshToken.getDeviceId()).isEmpty()) {

            jdbcTemplate.update("update jwt_refresh_token  set refresh_token = ?, refresh_token_expires = ? where device_id = ? and user_id = ?", jwtRefreshToken.getRefreshToken(), jwtRefreshToken.getRefreshTokenExpires(), jwtRefreshToken.getDeviceId(), jwtRefreshToken.getUser().getId());
        }
        User u = entityManager.getReference(User.class, jwtRefreshToken.getUser().getId());
        jwtRefreshToken.setUser(u);
        entityManager.persist(jwtRefreshToken);
    }

    @Override
    public Map getRefreshTokenAndTokenExpireTimeByDeviceIdAndUserName(String deviceId, String userName) {
        try {
            return jdbcTemplate.queryForMap("select refresh_token as refreshToken, refresh_token_expires as expirationTime from jwt_refresh_token WHERE user_id = (SELECT id FROM users WHERE user_name = ?)  and device_id = ?", userName, deviceId);
        } catch (DataAccessException ignored) {
            return Collections.emptyMap();
        }
    }

    @Override
    public void deleteByUserNameAndDeviceId(String userName, String deviceId) {
        try{
        jdbcTemplate.update("DELETE FROM jwt_refresh_token USING users u WHERE user_id = u.id and u.user_name = ? and device_id = ?  ", userName, deviceId);}
        catch (DataAccessException ignored){}
    }


    @Override
    public void deleteByUserName(String userName) {
        try{
        jdbcTemplate.update("DELETE FROM jwt_refresh_token WHERE user_id = (SELECT id FROM users WHERE user_name = ?)", userName);}
        catch (DataAccessException ignored){}
    }
}
