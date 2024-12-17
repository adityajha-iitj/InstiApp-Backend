package in.ac.iitj.instiapp.Repository.impl;

import in.ac.iitj.instiapp.Repository.AlumniRepository;
import in.ac.iitj.instiapp.database.entities.User.Alumni.Alumni;
import in.ac.iitj.instiapp.database.entities.User.Student.StudentBranch;
import in.ac.iitj.instiapp.database.entities.User.Student.StudentProgram;
import in.ac.iitj.instiapp.database.entities.User.User;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.NoSuchElementException;

@Repository
public class AlumniRepositoryImpl implements AlumniRepository {

    private final JdbcTemplate jdbcTemplate;
    private final EntityManager entityManager;

    @Autowired
    public AlumniRepositoryImpl(JdbcTemplate jdbcTemplate, EntityManager entityManager) {
        this.jdbcTemplate = jdbcTemplate;
        this.entityManager = entityManager;
    }

    @Override
    public void save(User user, Alumni alumni, StudentBranch studentBranch, StudentProgram studentProgram, int admissionYear, int passOutYear) {
        StudentBranch studentBranch1 = entityManager.getReference(StudentBranch.class, studentBranch.getId());
        StudentProgram studentProgram1 = entityManager.getReference(StudentProgram.class, studentProgram.getId());
        User user1 = entityManager.getReference(User.class, user.getId());

        alumni.setUser(user1);
        alumni.setBranch(studentBranch1);
        alumni.setProgram(studentProgram1);
        alumni.setAdmissionYear(admissionYear);
        alumni.setPassOutYear(passOutYear);

        entityManager.persist(alumni);
    }

    @Override
    public Alumni getAlumni(String username) {
        try {
            Long alumniId = jdbcTemplate.queryForObject("SELECT id FROM alumni WHERE username = ?", Long.class, username);
            return entityManager.getReference(Alumni.class, alumniId);
        } catch (EmptyResultDataAccessException e) {
            throw new NoSuchElementException("Alumni with username '" + username + "' not found.");
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while retrieving the alumni.");
        }
    }

    @Override
    public void updateAlumni(Alumni alumni) {
        entityManager.merge(alumni);
    }

    @Override
    public void deleteAlumni(Alumni alumni) {
        entityManager.remove(alumni);
    }

    @Override
    public boolean alumniExists(String username) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                "SELECT EXISTS (SELECT 1 FROM alumni WHERE username = ?)",
                Boolean.class,
                username
        ));
    }

}