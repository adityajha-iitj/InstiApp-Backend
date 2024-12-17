package in.ac.iitj.instiapp.Repository.impl;

import in.ac.iitj.instiapp.Repository.FacultyRepository;
import in.ac.iitj.instiapp.database.entities.User.Faculty.Faculty;
import in.ac.iitj.instiapp.database.entities.User.User;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Repository
public class FacultyRepositoryImpl{

    private final JdbcTemplate jdbcTemplate;
    private final EntityManager entityManager;

    @Autowired
    public FacultyRepositoryImpl(JdbcTemplate jdbcTemplate, EntityManager entityManager) {
        this.jdbcTemplate = jdbcTemplate;
        this.entityManager = entityManager;
    }

    public void save(User user, Faculty faculty) {
        // Fetch the user reference to ensure it exists and is managed
        User managedUser = entityManager.getReference(User.class, user.getId());

        // Set the user for the faculty
        faculty.setUser(managedUser);

        // Persist the faculty
        entityManager.persist(faculty);
    }

    public Faculty getFaculty(String username) {
        try {
            // First, retrieve the faculty ID based on the username
            Long facultyId = jdbcTemplate.queryForObject("SELECT id FROM faculty f JOIN users u ON f.user_id = u.id WHERE u.user_name = ?",
                    Long.class,
                    username
            );

            // Then, fetch the faculty entity
            return entityManager.getReference(Faculty.class, facultyId);
        } catch (EmptyResultDataAccessException e) {
            // Handle case where the username does not exist
            throw new NoSuchElementException("Faculty with username '" + username + "' not found.");
        } catch (Exception e) {
            // Catch-all for unexpected exceptions
            throw new RuntimeException("An error occurred while retrieving the faculty.", e);
        }
    }

    public void updateFaculty(Faculty faculty) {
        // Merge the faculty entity
        entityManager.merge(faculty);
    }

    public void deleteFaculty(Faculty faculty) {
        // Remove the faculty entity
        entityManager.remove(entityManager.contains(faculty) ? faculty : entityManager.merge(faculty));
    }

    public boolean facultyExists(String username) {
        try {
            Boolean result = jdbcTemplate.queryForObject("SELECT EXISTS (SELECT 1 FROM faculty f JOIN users u ON f.user_id = u.id WHERE u.user_name = ?)", Boolean.class, username);
            return result != null && result;
        } catch (DataAccessException e) {
            return false;
        }
    }
}