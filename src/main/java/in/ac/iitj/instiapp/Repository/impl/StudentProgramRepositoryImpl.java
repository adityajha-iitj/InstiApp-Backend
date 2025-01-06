package in.ac.iitj.instiapp.Repository.impl;

import in.ac.iitj.instiapp.Repository.User.Student.StudentProgramRepository;
import in.ac.iitj.instiapp.database.entities.User.Student.StudentProgram;
import jakarta.persistence.EntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Objects;

public class StudentProgramRepositoryImpl implements StudentProgramRepository {

    private final EntityManager entityManager;
    private final JdbcTemplate jdbcTemplate;



    public StudentProgramRepositoryImpl(EntityManager entityManager, JdbcTemplate jdbcTemplate) {
        this.entityManager = entityManager;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(StudentProgram studentProgram) {
        if(existsStudentProgram(studentProgram.getName()) != -1L){
            throw new DataIntegrityViolationException("Student Program already exists with name " + studentProgram.getName());
        }

        this.entityManager.persist(studentProgram);
    }

    @Override
    public Long existsStudentProgram(String name) {
        return jdbcTemplate.queryForObject("select coalesce(max(id), -1) from student_program where name = ?", Long.class, name);
    }

    @Override
    public List<String> getListOfStudentPrograms(Pageable pageable, boolean all) {
        if(all){
            return entityManager.createQuery("select sp.name from StudentProgram  sp",String.class)
                    .setFirstResult((int) pageable.getOffset())
                    .setMaxResults(pageable.getPageSize())
                    .getResultList();
        }
        return entityManager.createQuery("select  sp.name from StudentProgram  sp where sp.isActive = true",String.class)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    @Override
    public void updateStudentProgram(String oldName, String newName, boolean isActive) {
        if(existsStudentProgram(oldName) == -1L){
            throw new EmptyResultDataAccessException("Student Program does not exist with name " + oldName, 1);
        }
        if(existsStudentProgram(newName) != -1L && !Objects.equals(oldName, newName)){
            throw new DataIntegrityViolationException("Student Program already exists with name " + newName);
        }

        entityManager.createQuery("update StudentProgram  sp set sp.name = :newName ,sp.isActive = :isActive where sp.name = :oldName")
                .setParameter("newName", newName)
                .setParameter("isActive", isActive)
                .setParameter("oldName", oldName)
                .executeUpdate();

    }

    @Override
    public void deleteStudentProgram(String name) {
        // TODO
    }
}
