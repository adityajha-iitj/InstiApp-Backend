package in.ac.iitj.instiapp.Repository.impl;

import in.ac.iitj.instiapp.Repository.UserTypeRepository;
import in.ac.iitj.instiapp.database.entities.User.Usertype;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.hibernate.usertype.UserType;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserTypeRepositoryImpl implements UserTypeRepository {

    private final EntityManager entityManager;
    private final JdbcTemplate jdbcTemplate;



    public UserTypeRepositoryImpl(EntityManager entityManager, JdbcTemplate jdbcTemplate) {
        this.entityManager = entityManager;
        this.jdbcTemplate = jdbcTemplate;
    }


    @Transactional
    @Override
    public void save(Usertype userType) {
        if(exists(userType.getName())){
            throw new DataIntegrityViolationException("User type "+ userType.getName()+ " already exists");
        }
        entityManager.persist(userType);
    }

    @Override
    public void update(String oldName, Usertype userType) {
        if(!exists(oldName)){
            throw new EmptyResultDataAccessException("User type "+oldName+ " doesn't exists",1);
        }
        if(exists(userType.getName())){
            throw new  DataIntegrityViolationException("User type "+ userType.getName()+ " already exists");
        }

        jdbcTemplate.update("update user_type set name = ? where name = ?", userType.getName(), oldName);
    }

    /*Delete all users before it else will throw exceptions To be used with caution*/
    @Override
    public void delete(String userTypeName) {
        jdbcTemplate.update("delete from user_type where name = ?", userTypeName);
    }

    @Override
    public boolean exists(String name) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject("SELECT EXISTS(select 1 from user_type where name = ?)", Boolean.class, name));
    }

    @Override
    public List<String> getAllUserTypes(Pageable pageable) {
        return entityManager.createQuery("select u.name from Usertype u", String.class)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    @Override
    public Optional<Usertype> findByName(String name) {
        // A try-catch block is ESSENTIAL when using getSingleResult() to safely handle the "not found" case.
        try {
            // Query is now complete ("WHERE u.name = :name"), and parameter binding is correct.
            Usertype result = entityManager.createQuery("SELECT u FROM Usertype u WHERE u.name = :name", Usertype.class)
                    .setParameter("name", name) // Correct: ("placeholder", value)
                    .getSingleResult();

            // If a result is found, wrap it in an Optional and return it.
            return Optional.of(result);

        } catch (NoResultException e) {
            // This is the expected case when no Usertype with that name exists.
            // We catch the exception and return an empty Optional.
            return Optional.empty();
        }
    }
}
