package in.ac.iitj.instiapp.Repository.impl;

import in.ac.iitj.instiapp.Repository.UserRepository;
import in.ac.iitj.instiapp.database.entities.Media.UserAvatar;
import in.ac.iitj.instiapp.database.entities.Scheduling.Calendar.Calendar;
import in.ac.iitj.instiapp.database.entities.User.User;
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


}
