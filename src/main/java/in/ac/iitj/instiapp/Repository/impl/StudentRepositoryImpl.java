package in.ac.iitj.instiapp.Repository.impl;

import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.BusLocation;
import in.ac.iitj.instiapp.database.entities.User.Student.Student;
import in.ac.iitj.instiapp.database.entities.User.Student.StudentBranch;
import in.ac.iitj.instiapp.database.entities.User.Student.StudentProgram;
import in.ac.iitj.instiapp.database.entities.User.User;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.NoSuchElementException;

@Repository
public class StudentRepositoryImpl {

    private  final JdbcTemplate jdbcTemplate;
    private  final EntityManager entityManager;


    @Autowired
    public StudentRepositoryImpl(JdbcTemplate jdbcTemplate, EntityManager entityManager) {
        this.jdbcTemplate = jdbcTemplate;
        this.entityManager = entityManager;
    }

    void save(User user,Student student, StudentBranch studentBranch, StudentProgram studentProgram, int admission) {
        StudentBranch studentBranch1 = entityManager.getReference(StudentBranch.class, studentBranch.getId());
        StudentProgram studentProgram1 = entityManager.getReference(StudentProgram.class, studentProgram.getId());
        User user1 = entityManager.getReference(User.class, user.getId());
        student.setUser(user1);
        student.setBranch(studentBranch1);
        student.setProgram(studentProgram1);
        entityManager.persist(student);
    }

    Student getStudent(String username) {
        Student student;
        try {
            Long studentId = jdbcTemplate.queryForObject("select id from student where username=?", Long.class, username);
            return entityManager.getReference(Student.class, studentId);
        } catch (EmptyResultDataAccessException e) {
            // Handle case where the username does not exist
            throw new NoSuchElementException("Student with username '" + username + "' not found.");
        } catch (Exception e) {
            // Catch-all for unexpected exceptions
            throw new RuntimeException("An error occurred while retrieving the student.");
        }
    }

    void update(Student student) {
        entityManager.merge(student);
    }

    void delete(Student student) {
        entityManager.remove(student);
    }
}
