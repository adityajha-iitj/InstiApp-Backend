package in.ac.iitj.instiapp.Repository.impl;

import in.ac.iitj.instiapp.Repository.User.Student.Student.StudentRepository;
import in.ac.iitj.instiapp.database.entities.User.Student.Student.Student;
import in.ac.iitj.instiapp.database.entities.User.Student.StudentBranch;
import in.ac.iitj.instiapp.database.entities.User.Student.StudentProgram;
import in.ac.iitj.instiapp.database.entities.User.User;

import in.ac.iitj.instiapp.payload.User.Student.StudentBaseDto;

import in.ac.iitj.instiapp.payload.User.Student.StudentDetailedDto;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class StudentRepositoryImpl implements StudentRepository {

    private final JdbcTemplate jdbcTemplate;
    private final EntityManager entityManager;

    @Autowired
    public StudentRepositoryImpl(JdbcTemplate jdbcTemplate, EntityManager entityManager) {
        this.jdbcTemplate = jdbcTemplate;
        this.entityManager = entityManager;
    }

    @Transactional
    @Override
    public void save(Student student) {
        User user = entityManager.getReference(User.class, student.getUser().getId());
        StudentBranch branch = entityManager.getReference(StudentBranch.class, student.getBranch().getId());
        StudentProgram program = entityManager.getReference(StudentProgram.class, student.getProgram().getId());

        student.setUser(user);
        student.setBranch(branch);
        student.setProgram(program);

        entityManager.persist(student);
    }

    @Override
    public StudentBaseDto getStudent(String username) {
        try {
         return    entityManager.createQuery("select new in.ac.iitj.instiapp.payload.User.Student.StudentBaseDto(s.user.userName, s.program.name, s.branch.name, s.admissionYear) from Student  s where s.user.userName = :username", StudentBaseDto.class)
                    .setParameter("username", username)
                    .getSingleResult();
        }
        catch (NoResultException e) {
            throw new EmptyResultDataAccessException("Student with username " + username +  "doesn't exist",1);
        }
    }

    @Override
    public List<StudentBaseDto> getStudentByFilter(Optional<String> programName, Optional<String> branchName,
                                                   Optional<Integer> admissionYear, Pageable pageable) {


        return  entityManager.createQuery("select new in.ac.iitj.instiapp.payload.User.Student.StudentBaseDto(st.user.userName, st.program.name, st.branch.name, st.admissionYear) from Student st where " +
                "(:programName is NULL or st.program.name = :programName) and " +
                "(:branchName is NULL or st.branch.name = :branchName) and " +
                "(:admissionYear is NULL or st.admissionYear = :admissionYear)", StudentBaseDto.class)
                .setParameter("programName",programName.orElse(null))
                .setParameter("branchName",branchName.orElse(null))
                .setParameter("admissionYear",admissionYear.orElse(null))
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

    }

    @Override
    public Long existStudent(String username) {
        return jdbcTemplate.queryForObject("select coalesce(max(s.id), -1) from student s join users u on u.id = s.user_id where u.user_name = ?", Long.class, username);
    }

    @Override
    public StudentDetailedDto getDetailedStudent(String username) {
        if (existStudent(username) == -1) {
            throw new EmptyResultDataAccessException("Student with username '" + username + "' not found", 1);
        }

        return entityManager.createQuery("select new in.ac.iitj.instiapp.payload.User.Student.StudentDetailedDto(:username,st.program.name, st.branch.name, st.admissionYear ) from Student st where st.user.userName = :username", StudentDetailedDto.class)
                .setParameter("username", username)
                .getSingleResult();


    }

    @Transactional
    @Override
    public void updateStudent(String username, Student student) {

      if ( existStudent(username) == -1L){
          throw new EmptyResultDataAccessException("Student with username '" + username + "' not found", 1);
      }


        jdbcTemplate.update("update  student set " +
                "branch_id = case when ? is null then branch_id else ? end," +
                "program_id = case when ? is null then program_id else ? end, " +
                "admission_year = case  when ? is null then admission_year else ? end where id = ?",
                student.getBranch().getId(), student.getBranch().getId(),
                student.getProgram().getId(), student.getProgram().getId(),
                student.getAdmissionYear(), student.getAdmissionYear()
                        ,student.getId()
                );

    }

    @Override
    public Long deleteStudent(String username) {
        if (existStudent(username) == -1L) {
            throw new EmptyResultDataAccessException("Student with username '" + username + "' not found", 1);
        }

        return jdbcTemplate.queryForObject(
                "DELETE FROM student s USING users u WHERE u.id = s.user_id AND u.username = ? RETURNING u.id",
                Long.class,
                username);
    }

}