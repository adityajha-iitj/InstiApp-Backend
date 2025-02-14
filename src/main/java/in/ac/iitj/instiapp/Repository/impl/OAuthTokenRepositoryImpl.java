package in.ac.iitj.instiapp.Repository.impl;

import in.ac.iitj.instiapp.Repository.OAuth2TokenRepository;
import in.ac.iitj.instiapp.database.entities.Auth.OAuth2Tokens;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class OAuthTokenRepositoryImpl implements OAuth2TokenRepository {

    private final EntityManager entityManager;
    private final JdbcTemplate jdbcTemplate;

    public OAuthTokenRepositoryImpl(EntityManager entityManager, JdbcTemplate jdbcTemplate) {
        this.entityManager = entityManager;
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    @Transactional
    public void save(OAuth2Tokens oAuth2Token) {

        if(getByUsernameAndDeviceId(oAuth2Token.getUser().getUserName(), oAuth2Token.getDeviceId()).isPresent()) {
            jdbcTemplate.update("update  oauth2tokens set access_token = ?, refresh_token = ? where user_id = ? and device_id = ? ",
                    oAuth2Token.getAccessToken(),
                    oAuth2Token.getRefreshToken(),
                    oAuth2Token.getUser().getId(),
                    oAuth2Token.getDeviceId()
                    );
            return;
        }
        entityManager.persist(oAuth2Token);

    }

    @Override
    public Optional<OAuth2Tokens > getByUsernameAndDeviceId(String username, String deviceId) {

        try{
        return Optional.of(entityManager.createQuery("select new OAuth2Tokens(ot.user.userName, ot.deviceId, ot.accessToken, ot.refreshToken) from OAuth2Tokens ot where ot.user.userName = :username and ot.deviceId = :deviceId",OAuth2Tokens.class)
                .setParameter("username", username)
                .setParameter("deviceId", deviceId)
                .getSingleResult());}
        catch (NoResultException ignored) {
            return Optional.empty();
        }


    }

    @Override
    public void updateAccessToken(String deviceId, String userName, String accessToken) {
        jdbcTemplate.update("update oauth2tokens  set access_token = ? from users u where user_id = u.id and device_id = ? and u.user_name = ? ",accessToken,deviceId, userName);
    }

    @Override
    public void deleteByUsername(String username) {
        jdbcTemplate.update("delete from oauth2tokens using users u where u.user_name = ? and user_id = u.id",username);
    }

    @Override
    public void deleteByDeviceIdAndUserName(String deviceId, String userName) {
        jdbcTemplate.update("delete  from oauth2tokens using users u where device_id = ? and user_id = u.id and u.user_name = ? ",deviceId,userName);
    }
}
