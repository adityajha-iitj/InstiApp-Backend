package in.ac.iitj.instiapp.Repository.impl;

import in.ac.iitj.instiapp.Repository.User.Student.Alumni.AlumniRepository;
import in.ac.iitj.instiapp.database.entities.User.Student.Alumni.Alumni;
import in.ac.iitj.instiapp.database.entities.User.Student.StudentBranch;
import in.ac.iitj.instiapp.database.entities.User.Student.StudentProgram;
import in.ac.iitj.instiapp.database.entities.User.User;
import in.ac.iitj.instiapp.payload.User.Alumni.AlumniBaseDto;
import in.ac.iitj.instiapp.payload.User.Alumni.AlumniDetailedDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AlumniRepositoryImpl implements AlumniRepository {

    private final JdbcTemplate jdbcTemplate;
    private final EntityManager entityManager;


public AlumniRepositoryImpl(JdbcTemplate jdbcTemplate, EntityManager entityManager) {
    this.jdbcTemplate = jdbcTemplate;
    this.entityManager = entityManager;
}


    @Override
    public void save(Alumni alumni) {
        User u = entityManager.getReference(User.class, alumni.getUser().getId());
        StudentBranch branch = entityManager.getReference(StudentBranch.class, alumni.getBranch().getId());
        StudentProgram program = entityManager.getReference(StudentProgram.class, alumni.getProgram().getId());


        alumni.setUser( u);
        alumni.setBranch(branch);
        alumni.setProgram(program);
    }

    @Override
    public AlumniBaseDto getAlumni(String username) {
        try{
            return  entityManager.createQuery("select new in.ac.iitj.instiapp.payload.User.Alumni.AlumniBaseDto(al.user.userName, al.program.name, al.branch.name, al.admissionYear) from Alumni al where al.user.name = :username", AlumniBaseDto.class)
                    .setParameter("username",username)
                    .getSingleResult();
        }catch (NoResultException ignored){
            throw new EmptyResultDataAccessException("Alumni not exist with username " + username ,1);
        }
    }

    @Override
    public AlumniDetailedDto getDetailedAlumni(String username) {
        Long id = alumniExists(username);
        if(id == -1L) {
            throw new EmptyResultDataAccessException("Alumni not exist with username " + username ,1);
        }

        return entityManager.createQuery("select  new in.ac.iitj.instiapp.payload.User.Alumni.AlumniDetailedDto(al.user.userName, al.program.name, al.branch.name, al.admissionYear, al.passOutYear) from Alumni  al where al.user.userName = :username",AlumniDetailedDto.class)
                .setParameter("username", username)
                .getSingleResult();
    }

    @Override
    public List<AlumniBaseDto> getAlumniByFilter(Optional<String> programName, Optional<String> branchNameName, Optional<Integer> admissionYear, Optional<Integer> passOutYear, Pageable pageable) {
        return entityManager.createQuery("select new in.ac.iitj.instiapp.payload.User.Alumni.AlumniBaseDto(al.user.userName,al.program.name, al.branch.name, al.admissionYear ) from Alumni  al where " +
                "(:programName is null or al.program.name = :programName) and" +
                "(:branchName is null or al.branch.name = :branchName) and " +
                "(:admissionYear is null or al.admissionYear = :admissionYear) and " +
                "(:passOutYear is null  or al.passOutYear  = :passOutYear)",AlumniBaseDto.class)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    @Override
    public Long alumniExists(String username) {
        return jdbcTemplate.queryForObject("select coalesce(max(al.id) , -1 ) from alumni al join users u on u.id = al.user_id where u.user_name = ?",Long.class, username);
    }

    @Override
    public void updateAlumni(Alumni alumni) {
        jdbcTemplate.update("update alumni set " +
                "program_id = case when ? is null then program_id else ? end," +
                "branch_id = case when ? is null then branch_id else ? end," +
                "admission_year = case when ? is null then admission_year else ? end," +
                "pass_out_year = case when ? is null then pass_out_year else  ? end " +
                "where id =?",
                alumni.getProgram().getId(),alumni.getProgram().getId(),
                alumni.getBranch().getId(),alumni.getBranch().getId(),
                alumni.getAdmissionYear(), alumni.getAdmissionYear(),
                alumni.getPassOutYear(), alumni.getPassOutYear(),
                alumni.getId()
        );
    }

    @Override
    public Long deleteAlumni(String username) {

    // TODO
        return  0L;
    }
}