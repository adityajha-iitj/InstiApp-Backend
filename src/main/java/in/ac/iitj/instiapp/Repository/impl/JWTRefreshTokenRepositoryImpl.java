package in.ac.iitj.instiapp.Repository.impl;

import in.ac.iitj.instiapp.Repository.JWTRefreshTokenRepository;
import in.ac.iitj.instiapp.database.entities.Auth.JWTRefreshToken;
import in.ac.iitj.instiapp.database.entities.User.User;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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


        if(existsByUserNameAndDeviceId(jwtRefreshToken.getUser().getUserName(), jwtRefreshToken.getDeviceId())) {
            throw new DataIntegrityViolationException("User with username " +jwtRefreshToken.getUser().getUserName()+"  already exists with this device Id + " +jwtRefreshToken.getDeviceId());
        }

        try {
            Long id = jdbcTemplate.queryForObject("SELECT id from users where user_name = ?", Long.class, jwtRefreshToken.getUser().getUserName());
            User u = entityManager.getReference(User.class, id);
            jwtRefreshToken.setUser(u);
            entityManager.persist(jwtRefreshToken);
        } catch (DataAccessException e) {
            throw new EmptyResultDataAccessException("User doesn't exist with username "+ jwtRefreshToken.getUser().getUserName(), 1);
        }

    }

    @Override
    public Map getRefreshTokenAndTokenExpireTimeByDeviceIdAndUserName(String deviceId, String userName) {
       if(!existsByUserNameAndDeviceId(userName, deviceId)) {
           throw new EmptyResultDataAccessException("User with username " +userName+" does not exists with this device Id + " +deviceId,1);
       }
       return jdbcTemplate.queryForObject("select refresh_token, refresh_token_expires from jwt_refresh_token WHERE user_id = (SELECT id FROM users WHERE user_name = ?)  and device_id = ?", Map.class, userName,deviceId);
    }

    @Override
    public void updateRefreshTokenAndTokenExpireTimeByDeviceIdAndUserName(JWTRefreshToken jwtRefreshToken) {
       if(! existsByUserNameAndDeviceId(jwtRefreshToken.getUser().getUserName(), jwtRefreshToken.getDeviceId())){
           throw new EmptyResultDataAccessException("User with username " +jwtRefreshToken.getUser().getUserName()+" does not exists with this device Id + " +jwtRefreshToken.getDeviceId(),1);
       }

       jdbcTemplate.update("UPDATE jwt_refresh_token SET refresh_token = ?, refresh_token_expires = ? WHERE user_id = (SELECT id FROM users WHERE user_name = ?) AND device_id = ? "
               ,jwtRefreshToken.getRefreshToken()
               ,jwtRefreshToken.getRefreshTokenExpires()
               ,jwtRefreshToken.getUser().getUserName()
               ,jwtRefreshToken.getDeviceId()
               );
    }

    @Override
    public void deleteByUserName(String userName) {
        jdbcTemplate.update("DELETE FROM jwt_refresh_token WHERE user_id = (SELECT id FROM users WHERE user_name = ?)", userName);
    }

    @Override
    public Boolean existsByUserNameAndDeviceId(String userName, String deviceId) {
         return jdbcTemplate.queryForObject("SELECT EXISTS(SELECT 1 from jwt_refresh_token where user_id = (SELECT id FROM users WHERE user_name = ?) AND device_id = ?)", Boolean.class, userName, deviceId);
    }
}
