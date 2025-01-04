package in.ac.iitj.instiapp.Repository.impl;

import in.ac.iitj.instiapp.Repository.User.Student.Student.StudentRepository;
import in.ac.iitj.instiapp.database.entities.User.Student.Student.Student;
import in.ac.iitj.instiapp.database.entities.User.Student.StudentBranch;
import in.ac.iitj.instiapp.database.entities.User.Student.StudentProgram;
import in.ac.iitj.instiapp.database.entities.User.User;
import in.ac.iitj.instiapp.payload.User.Organisation.OrganisationBaseDto;
import in.ac.iitj.instiapp.payload.User.Student.StudentBaseDto;
import in.ac.iitj.instiapp.payload.User.Student.StudentBranchDto;
import in.ac.iitj.instiapp.payload.User.Student.StudentDetailedDto;
import in.ac.iitj.instiapp.payload.User.UserBaseDto;
import in.ac.iitj.instiapp.payload.User.UserDetailedDto;
import jakarta.persistence.EntityManager;
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
        if (!existStudent(username)) {
            throw new EmptyResultDataAccessException("Student with username '" + username + "' not found", 1);
        }

        Student student = entityManager.createQuery(
                        "SELECT s FROM Student s " +
                                "JOIN FETCH s.user u " +
                                "JOIN FETCH s.branch b " +
                                "JOIN FETCH s.program p " +
                                "JOIN FETCH b.organisation o " +
                                "WHERE u.userName = :username",
                        Student.class)
                .setParameter("username", username)
                .getSingleResult();

        return new StudentBaseDto(
                new UserBaseDto(
                        student.getUser().getName(),
                        student.getUser().getUserName(),
                        student.getUser().getEmail(),
                        student.getUser().getUserType().getName(),
                        student.getUser().getAvatarUrl()
                ),
                student.getProgram().getName(),
                new StudentBranchDto(
                        student.getBranch().getName(),
                        new OrganisationBaseDto(
                                null,
                                student.getBranch().getOrganisation().getParentOrganisation().getUser().getUserName(),
                                student.getBranch().getOrganisation().getTypeName(),
                                student.getBranch().getOrganisation().getDescription(),
                                student.getBranch().getOrganisation().getWebsite()
                        ),
                        student.getBranch().getOpeningYear(),
                        student.getBranch().getClosingYear()
                ),
                student.getAdmissionYear()
        );
    }

    @Override
    public List<StudentBaseDto> getStudentByFilter(Optional<String> programName, Optional<String> branchName,
                                                   Optional<Integer> admissionYear, Pageable pageable) {
        StringBuilder queryString = new StringBuilder(
                "SELECT new in.ac.iitj.instiapp.payload.User.Student.StudentBaseDto(" +
                        "new in.ac.iitj.instiapp.payload.User.UserBaseDto(" +
                        "u.name, u.userName, u.email, u.userTypeName, u.avatarUrl), " +
                        "s.program.name, " +
                        "new in.ac.iitj.instiapp.payload.User.Student.StudentBranchDto(" +
                        "s.branch.name, " +
                        "new in.ac.iitj.instiapp.payload.User.Organisation.OrganisationBaseDto(" +
                        "null, o.parentOrganisation.user.userName, o.typeName, o.description, o.website), " +
                        "s.branch.openingYear, s.branch.closingYear), " +
                        "s.admissionYear) " +
                        "FROM Student s " +
                        "JOIN s.user u " +
                        "JOIN s.branch.organisation o " +
                        "WHERE 1=1");

        if (programName.isPresent()) {
            queryString.append(" AND s.program.name = :programName");
        }
        if (branchName.isPresent()) {
            queryString.append(" AND s.branch.name = :branchName");
        }
        if (admissionYear.isPresent()) {
            queryString.append(" AND s.admissionYear = :admissionYear");
        }

        var query = entityManager.createQuery(queryString.toString(), StudentBaseDto.class);

        if (programName.isPresent()) {
            query.setParameter("programName", programName.get());
        }
        if (branchName.isPresent()) {
            query.setParameter("branchName", branchName.get());
        }
        if (admissionYear.isPresent()) {
            query.setParameter("admissionYear", admissionYear.get());
        }

        return query
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    @Override
    public boolean existStudent(String username) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                "SELECT EXISTS (SELECT 1 FROM student s JOIN users u ON u.id = s.user_id WHERE u.username = ?)",
                Boolean.class,
                username));
    }

    @Override
    public StudentDetailedDto getDetailedStudent(String username) {
        if (!existStudent(username)) {
            throw new EmptyResultDataAccessException("Student with username '" + username + "' not found", 1);
        }

        Student student = entityManager.createQuery(
                        "SELECT s FROM Student s " +
                                "JOIN FETCH s.user u " +
                                "JOIN FETCH s.branch b " +
                                "JOIN FETCH s.program p " +
                                "JOIN FETCH b.organisation o " +
                                "JOIN FETCH u.usertype ut " +
                                "WHERE u.userName = :username",
                        Student.class)
                .setParameter("username", username)
                .getSingleResult();

        return new StudentDetailedDto(
                new UserDetailedDto(
                        student.getUser().getName(),
                        student.getUser().getUserName(),
                        student.getUser().getEmail(),
                        student.getUser().getPhoneNumber(),
                        student.getUser().getUserType().getName(),
                        student.getUser().getCalendar(),
                        student.getUser().getAvatarUrl(),
                        student.getUser().getRoles()
                ),
                student.getProgram().getName(),
                new StudentBranchDto(
                        student.getBranch().getName(),
                        new OrganisationBaseDto(
                                new UserBaseDto(
                                        student.getBranch().getOrganisation().getUser().getName(),
                                        student.getBranch().getOrganisation().getUser().getUserName(),
                                        student.getBranch().getOrganisation().getUser().getEmail(),
                                        student.getBranch().getOrganisation().getUser().getUserType().getName(),
                                        student.getBranch().getOrganisation().getUser().getAvatarUrl()
                                ),
                                student.getBranch().getOrganisation().getParentOrganisation().getUser().getUserName(),
                                student.getBranch().getOrganisation().getTypeName(),
                                student.getBranch().getOrganisation().getDescription(),
                                student.getBranch().getOrganisation().getWebsite()
                        ),
                        student.getBranch().getOpeningYear(),
                        student.getBranch().getClosingYear()
                ),
                student.getAdmissionYear()
        );
    }

    @Transactional
    @Override
    public void updateStudent(Student student) {
        entityManager.createQuery(
                        "UPDATE Student s SET " +
                                "s.branch = :branch, " +
                                "s.program = :program, " +
                                "s.admissionYear = :admissionYear " +
                                "WHERE s.user.id = :userId")
                .setParameter("branch", entityManager.getReference(StudentBranch.class, student.getBranch().getId()))
                .setParameter("program", entityManager.getReference(StudentProgram.class, student.getProgram().getId()))
                .setParameter("admissionYear", student.getAdmissionYear())
                .setParameter("userId", student.getUser().getId())
                .executeUpdate();
    }

    @Override
    public Long deleteStudent(String username) {
        if (!existStudent(username)) {
            throw new EmptyResultDataAccessException("Student with username '" + username + "' not found", 1);
        }

        return jdbcTemplate.queryForObject(
                "DELETE FROM student s USING users u WHERE u.id = s.user_id AND u.username = ? RETURNING u.id",
                Long.class,
                username);
    }
}