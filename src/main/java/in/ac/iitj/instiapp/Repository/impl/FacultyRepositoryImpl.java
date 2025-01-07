package in.ac.iitj.instiapp.Repository.impl;

import in.ac.iitj.instiapp.Repository.FacultyRepository;
import in.ac.iitj.instiapp.database.entities.User.Faculty.Faculty;
import in.ac.iitj.instiapp.database.entities.User.Organisation.Organisation;
import in.ac.iitj.instiapp.database.entities.User.Student.Alumni.Alumni;
import in.ac.iitj.instiapp.database.entities.User.User;
import in.ac.iitj.instiapp.payload.User.Alumni.AlumniBaseDto;
import in.ac.iitj.instiapp.payload.User.Faculty.FacultyBaseDto;
import in.ac.iitj.instiapp.payload.User.Faculty.FacultyDetailedDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jdk.jfr.Description;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.*;


import java.util.List;
import java.util.Optional;

@Repository
public class FacultyRepositoryImpl implements FacultyRepository {

    private final JdbcTemplate jdbcTemplate;
    private final EntityManager entityManager;

    @Autowired
    public FacultyRepositoryImpl(JdbcTemplate jdbcTemplate, EntityManager entityManager) {
        this.jdbcTemplate = jdbcTemplate;
        this.entityManager = entityManager;
    }

    /**
     * Saves a Faculty entity to the database.
     * Throws DataIntegrityViolationException if the faculty record already exists.
     */
    @Override
    @Transactional
    public void save(Faculty faculty) {
        faculty.setUser(entityManager.getReference(User.class, faculty.getUser().getId()));
        faculty.setOrganisation(entityManager.getReference(Organisation.class, faculty.getOrganisation().getId()));


        
        entityManager.persist(faculty);
    }

    /**
     * Checks if a Faculty record exists in the database by user name
     *
     * @param username        Username of the faculty
     * @return true if the record exists, false otherwise
     */
    @Override
    public Long facultyExists(String username) {
        return jdbcTemplate.queryForObject("select coalesce(max(fa.id) , -1 ) from faculty fa join users u on u.id = fa.user_id where u.user_name = ?",Long.class, username);
    }

    @Override
    public FacultyBaseDto getFaculty(String username) {
        try{
            return  entityManager.createQuery("select new in.ac.iitj.instiapp.payload.User.Faculty.FacultyBaseDto(fa.user.userName, fa.organisation.user.userName, fa.Description, fa.websiteUrl) from Faculty fa where fa.user.userName = :username", FacultyBaseDto.class)
                    .setParameter("username",username)
                    .getSingleResult();
        }catch (NoResultException ignored){
            throw new EmptyResultDataAccessException("Faculty with username " + username+" does not exist" ,1);
        }
    }

    public FacultyDetailedDto getDetailedFaculty(String username){
        try{
            return entityManager.createQuery("select new in.ac.iitj.instiapp.payload.User.Faculty.FacultyDetailedDto(fa.user.userName,fa.organisation.user.userName, fa.Description, fa.websiteUrl) from Faculty fa where fa.user.userName= :username", FacultyDetailedDto.class)
                    .setParameter("username",username)
                    .getSingleResult();
        }catch (NoResultException ignored){
            throw new EmptyResultDataAccessException("Alumni with username " + username +" does not exist" ,1);
        }
    }

    @Override
    public void updateFaculty(Faculty faculty) {
        jdbcTemplate.update("update faculty set " +
                        "organisation_id = case when ? is null then organisation_id else ? end," +
                        "description = case when ? is null then description else ? end," +
                        "website_url = case when ? is null then website_url else ? end " +
                        "where id =?",
                faculty.getOrganisation().getId(),faculty.getOrganisation().getId(),
                faculty.getDescription(),faculty.getDescription(),
                faculty.getWebsiteUrl(),faculty.getWebsiteUrl(),
                faculty.getId()
        );
    }


    @Override
    public List<FacultyBaseDto> getFacultyByFilter(Optional<String> organisationName, Optional<String> description, Optional<String> websiteUrl, Pageable pageable) {
        return entityManager.createQuery(
                        "select new in.ac.iitj.instiapp.payload.User.Faculty.FacultyBaseDto(fa.user.userName, fa.organisation.user.userName, fa.Description, fa.websiteUrl) " +
                                "from Faculty fa " +
                                "where (:organisationName is null or fa.organisation.user.userName = :organisationName) " +
                                "and (:description is null or fa.Description = :description) " +
                                "and (:websiteUrl is null or fa.websiteUrl = :websiteUrl)",
                        FacultyBaseDto.class)
                .setParameter("organisationName", organisationName.orElse(null)) // Unwrap Optional
                .setParameter("description", description.orElse(null)) // Unwrap Optional
                .setParameter("websiteUrl", websiteUrl.orElse(null)) // Unwrap Optional
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

}
