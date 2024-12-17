package in.ac.iitj.instiapp.Repository.impl;

import in.ac.iitj.instiapp.Repository.UserRepository;
import in.ac.iitj.instiapp.database.entities.Media.UserAvatar;
import in.ac.iitj.instiapp.database.entities.Scheduling.Calendar.Calendar;
import in.ac.iitj.instiapp.database.entities.User.User;
import in.ac.iitj.instiapp.database.entities.User.Usertype;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private  final  JdbcTemplate jdbcTemplate;
    private  final  EntityManager entityManager;


    @Autowired
    public UserRepositoryImpl(JdbcTemplate jdbcTemplate, EntityManager entityManager) {
        this.jdbcTemplate = jdbcTemplate;
        this.entityManager = entityManager;
    }


    /**
     * Media Type name check exists outside if it exists
     */
    @Override
    @Transactional
    public void save(User user, Long avatarId, Long CalendarId, String MediaTypeName) {
        if (existsByUsername(user.getUserName())){
            throw new DataIntegrityViolationException("User name already exists");
        }
        if(existsByEmail(user.getEmail())){
            throw new DataIntegrityViolationException("User email already exists");
        }
        if(existsByPhoneNumber(user.getPhoneNumber())){
            throw new DataIntegrityViolationException("Phone number already exists");
        }
        UserAvatar userAvatar =  entityManager.getReference(UserAvatar.class,avatarId);
        Calendar calendar =  entityManager.getReference(Calendar.class, CalendarId);

        user.setAvatar(userAvatar);
        user.setCalendar(calendar);

        entityManager.persist(user);
    }

    @Override
    public boolean existsByEmail(String email) {


        return Boolean.TRUE.equals(jdbcTemplate.queryForObject("SELECT EXISTS (SELECT 1 FROM users WHERE email = ?)", Boolean.class, email));
    }

    @Override
    public boolean existsByUsername(String username) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject("SELECT EXISTS (SELECT 1 FROM users WHERE user_name = ?)", Boolean.class, username));
    }

    @Override
    public boolean existsByPhoneNumber(String phoneNumber) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject("SELECT EXISTS (SELECT 1 FROM users WHERE phone_number = ?)", Boolean.class, phoneNumber));
    }

    @Override
    public boolean deleteByUsername(String username) {
        return Boolean.TRUE.equals(jdbcTemplate.update("DELETE FROM users WHERE user_name = ?", username));
    }

    @Override
    public boolean userExists(String username){
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject("SELECT EXISTS (SELECT 1 FROM users WHERE user_name = ?)", Boolean.class, username));
    }
    @Override
    public void updatePhoneNumber(String phoneNumber, String username) {
        if(userExists(username)){
            String sql = "UPDATE users SET phone_number = ? WHERE user_name = ?";
            jdbcTemplate.update(sql, phoneNumber, username);
        }
        throw new DataIntegrityViolationException("No user with the given username exists");
    }

    @Override
    public void updateUserType(String username , String newusertype){
        if(userExists(username)){
            String sql = "UPDATE users u " +
                    "JOIN user_type ut ON ut.name = ? " +
                    "SET u.user_type_id = ut.id " +
                    "WHERE u.user_name = ?";

            jdbcTemplate.update(sql, newusertype, username);
        }
        throw new DataIntegrityViolationException("No user with the given username exists");
    }




}
