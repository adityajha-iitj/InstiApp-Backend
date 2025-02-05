package in.ac.iitj.instiapp.Repository.impl;

import in.ac.iitj.instiapp.Repository.User.Student.StudentBranchRepository;
import in.ac.iitj.instiapp.database.entities.User.Organisation.Organisation;
import in.ac.iitj.instiapp.database.entities.User.Student.StudentBranch;
import in.ac.iitj.instiapp.payload.User.Student.StudentBranchDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

public class StudentBranchRepositoryImpl implements StudentBranchRepository {

    private final EntityManager entityManager;
    private final JdbcTemplate jdbcTemplate;

    public StudentBranchRepositoryImpl(EntityManager entityManager, JdbcTemplate jdbcTemplate) {
        this.entityManager = entityManager;
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public void saveStudentBranch(StudentBranch studentBranch) {
        if(existsStudentBranch(studentBranch.getName()) != -1L){
            throw new DataIntegrityViolationException("Student branch already exists with name " + studentBranch.getName());
        }
        studentBranch.setOrganisation(entityManager.getReference(Organisation.class, studentBranch.getOrganisation().getId()));
        entityManager.persist(studentBranch);
    }

    @Override
    public List<StudentBranchDto> getListOfStudentBranch(Pageable pageable) {
        return entityManager.createQuery("select new in.ac.iitj.instiapp.payload.User.Student.StudentBranchDto(st.name,st.organisation.user.userName,st.openingYear,st.closingYear) from StudentBranch  st",StudentBranchDto.class)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    @Override
    public StudentBranchDto getStudentBranch(String name) {
        try{
          return   entityManager.createQuery("select new in.ac.iitj.instiapp.payload.User.Student.StudentBranchDto(st.name,st.organisation.user.userName,st.openingYear,st.closingYear) from StudentBranch  st where st.name = :name",StudentBranchDto.class)
                    .setParameter("name", name)
                    .getSingleResult();
        }catch (NoResultException ignored){
            throw new EmptyResultDataAccessException("Student Branch with name "+name + " doesn't exist",1);
        }}

    @Override
    public Long existsStudentBranch(String name) {
        return jdbcTemplate.queryForObject("select coalesce(max(id), -1) from student_branch where name = ?", Long.class, name);
    }

    @Override
    public void updateStudentBranch(String name, StudentBranch studentBranch) {




        Long id = existsStudentBranch(name);
        if(id == -1){
            throw new EmptyResultDataAccessException("Student branch with name "+name + " doesn't exist",1);
        }

        if(existsStudentBranch(studentBranch.getName()) != -1L && studentBranch.getName() != name){
            throw new DataIntegrityViolationException("Student branch already exists with name " + studentBranch.getName());
        }

        jdbcTemplate.update(
                "update student_branch set " +
                        "organisation_id = case when cast(? as integer) is null then organisation_id else ? end," +
                        "name = case when cast(? as varchar) is null then name else ? end," +
                        "opening_year = case when cast(? as integer) is null then opening_year else ? end," +
                        "closing_year = case when cast(? as integer) is null then closing_year else ? end " +
                        "where id = ?",

                Optional.ofNullable(studentBranch.getOrganisation())
                        .map(Organisation::getId).orElse(null),
                Optional.ofNullable(studentBranch.getOrganisation())
                        .map(Organisation::getId).orElse(null),

                studentBranch.getName(),
                studentBranch.getName(),

                studentBranch.getOpeningYear(),
                studentBranch.getOpeningYear(),

                studentBranch.getClosingYear(),
                studentBranch.getClosingYear(),

                id
        );


    }

    @Override
    public List<Long> getBranchIdsFromNames(List<String> names, Pageable pageable) {
        return  entityManager.createQuery("select id from StudentBranch  where name in :names", Long.class)
                .setParameter("names", names)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }


    @Override
    public void deleteStudentBranch(String name) {
        // todo
    }
}
