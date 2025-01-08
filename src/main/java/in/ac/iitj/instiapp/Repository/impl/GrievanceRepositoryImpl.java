/*package in.ac.iitj.instiapp.Repository.impl;


import in.ac.iitj.instiapp.database.entities.Grievance;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GrievanceRepositoryImpl implements in.ac.iitj.instiapp.Repository.GrievanceRepository {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(GrievanceRepositoryImpl.class);
    private final JdbcTemplate jdbcTemplate;
    private final EntityManager entityManager;


    public GrievanceRepositoryImpl(JdbcTemplate jdbcTemplate, EntityManager entityManager) {
        this.jdbcTemplate = jdbcTemplate;
        this.entityManager = entityManager;
    }

    public boolean checkGrievance(String title , String user_from_id){
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject("SELECT EXISTS(SELECT id FROM grivance  WHERE title = ? AND user_from_id = ? )", Boolean.class, title , user_from_id ));
    }

    @Transactional
    public void addGrievance(Grievance grievance){
        if(checkGrievance(grievance.getTitle() , grievance.getUserFrom().getUserName())){
            throw new DataIntegrityViolationException("Grivance for this title already exists");
        }
        entityManager.persist(grievance);

    }

    public List<Grievance> getGrievances(String username){
        String sql = "select * from grievance where user_from_id = :username";
        Query  query = entityManager.createQuery(sql , Grievance.class);
        query.setParameter("username", username);

        return query.getResultList();
    }
    public void deleteGrievance(String userName , String grievanceTitle){
        jdbcTemplate.update("delete from grievance where user_from_id = ? and title = ?", userName , grievanceTitle);
    }

    public void updateGrievance(String userName, String grievanceTitle, Boolean resolved){
        String sql = "UPDATE grievance SET resolved = ? WHERE userFromId = ? AND Title = ?";

        int rowsAffected = jdbcTemplate.update(sql, resolved, userName, grievanceTitle);

        if (rowsAffected == 0) {
            throw new RuntimeException("Grievance not found or could not be updated.");
        }
    }

    public Grievance getGrievance(String userName , String grievanceTitle){
        String sql = "select * from grievance where user_from_id = :username AND title =:grievanceTitle";
        Query query = entityManager.createQuery(sql , Grievance.class);
        query.setParameter("username", userName);
        query.setParameter("grievanceTitle", grievanceTitle);

        return (Grievance) query.getSingleResult();
    }
}*/

package in.ac.iitj.instiapp.Repository.impl;
import in.ac.iitj.instiapp.Repository.GrievanceRepository;
import in.ac.iitj.instiapp.database.entities.Grievance;
import in.ac.iitj.instiapp.database.entities.User.Organisation.OrganisationRole;
import in.ac.iitj.instiapp.payload.GrievanceDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;
import in.ac.iitj.instiapp.database.entities.User.User;
import in.ac.iitj.instiapp.database.entities.Media.Media;
import in.ac.iitj.instiapp.Repository.UserRepository;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;



@Repository
public class GrievanceRepositoryImpl implements GrievanceRepository {

    private final JdbcTemplate jdbcTemplate;
    private final EntityManager entityManager;

    GrievanceRepositoryImpl(JdbcTemplate jdbcTemplate, EntityManager entityManager) {
        this.jdbcTemplate = jdbcTemplate;
        this.entityManager = entityManager;
    }

    @Transactional
    public void save(Grievance grievance) {
        grievance.setUserFrom(entityManager.getReference(User.class,grievance.getUserFrom().getId()));
        grievance.setOrganisationRole(entityManager.getReference(OrganisationRole.class,grievance.getOrganisationRole().getId()));
        grievance.setMedia(entityManager.getReference(Media.class,grievance.getMedia().getId()));

        grievance.setTitle(grievance.getTitle());
        grievance.setDescription(grievance.getDescription());
        grievance.setResolved(false);

        entityManager.persist(grievance);
    }

    public List<GrievanceDto> getGrievancesByFilter(Optional<String> title, Optional<String> description,Optional<String> organisationName, Optional<Boolean> resolved,Pageable pageable){

            return entityManager.createQuery("select new in.ac.iitj.instiapp.payload.GrievanceDto(gr.Title,gr.Description,gr.userFrom.userName,gr.organisationRole.organisation.user.userName,gr.organisationRole.roleName,gr.organisationRole.permission,gr.resolved,gr.media.publicId) from Grievance gr where"+
                                    "(:title is null or gr.Title = :title) and " +
                                    "(:description is null or gr.Description = :description) and " +
                                    "(:organisationName is null or gr.organisationRole.organisation.user.userName = :organisationName) and "+
                                    "(:resolved is null or gr.resolved = :resolved)",
                            GrievanceDto.class)
                    .setParameter("title", title.orElse(null))
                    .setParameter("description", description.orElse(null))
                    .setParameter("organisationName", organisationName.orElse(null))
                    .setParameter("resolved",resolved.orElse(null))
                    .setFirstResult((int) pageable.getOffset())
                    .setMaxResults(pageable.getPageSize())
                    .getResultList();
    }

    @Override
    public void updateGrievance(String publicId, Grievance grievance) {

        Long organisationRoleId = grievance.getOrganisationRole().getId();

// Update query with extracted ID
        String updateQuery = "UPDATE grievance SET " +
                "title = CASE WHEN ? IS NULL THEN title ELSE ? END, " +
                "description = CASE WHEN ? IS NULL THEN description ELSE ? END, " +
                "organisation_role_id = CASE WHEN cast(? as bigint) IS NULL THEN organisation_role_id ELSE ? END, " +
                "resolved = CASE WHEN ? IS NULL THEN resolved else ? end " +
                "WHERE public_id = ?";


        jdbcTemplate.update(updateQuery,
                grievance.getTitle(),
                grievance.getTitle(),
                grievance.getDescription(),
                grievance.getDescription(),
                organisationRoleId,
                organisationRoleId,
                grievance.getResolved(),
                grievance.getResolved(),
                publicId
        );

    }



    public Long existGrievance(String publicId) {
        String queryStr = "SELECT COUNT(gr) FROM Grievance gr WHERE gr.publicId = :publicId";
        Long count = entityManager.createQuery(queryStr, Long.class)
                .setParameter("publicId", publicId)
                .getSingleResult();
        if(count == 0)
            return -1L;
        else
            return count;
    }


    public GrievanceDto getGrievance(String publicId){

        return entityManager.createQuery("select new in.ac.iitj.instiapp.payload.GrievanceDto(gr.Title,gr.Description,gr.userFrom.userName,gr.organisationRole.organisation.user.userName,gr.organisationRole.roleName,gr.organisationRole.permission,gr.resolved,gr.media.publicId)"+
                        " from Grievance gr " +
                        "where gr.publicId = :publicId",GrievanceDto.class)
                .setParameter("publicId",publicId)
                .getSingleResult();
    }


    @Override
    public void deleteGrievance(String publicId) {
        // TODO
    }





}