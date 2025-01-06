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
import jakarta.transaction.Transactional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;
import in.ac.iitj.instiapp.database.entities.User.User;
import in.ac.iitj.instiapp.database.entities.Media.Media;
import in.ac.iitj.instiapp.Repository.UserRepository;

import java.util.List;


@Repository
public class GrievanceRepositoryImpl implements GrievanceRepository {

    private final JdbcTemplate jdbcTemplate;
    private final EntityManager entityManager;

    GrievanceRepositoryImpl(JdbcTemplate jdbcTemplate, EntityManager entityManager) {
        this.jdbcTemplate = jdbcTemplate;
        this.entityManager = entityManager;
    }

    public void addGrievance(Grievance grievance) {
        grievance.setUserFrom(entityManager.getReference(User.class,grievance.getUserFrom().getId()));
        grievance.setOrganisationRole(entityManager.getReference(OrganisationRole.class,grievance.getOrganisationRole().getId()));
        grievance.setMedia(entityManager.getReference(Media.class,grievance.getMedia().getId()));

        grievance.setTitle(grievance.getTitle());
        grievance.setDescription(grievance.getDescription());
        grievance.setResolved(false);

        entityManager.persist(grievance);
    }

    public List<GrievanceDto> getGrievances(String username){
        if(UserRepository.usernameExists(username) == -1L)
            throw new EmptyResultDataAccessException("User with username " + username + " not found", 1);

        else
            return entityManager.createQuery("select new in.ac.iitj.instiapp.payload.GrievanceDto(gr.Title,gr.Description,gr.userFrom.userName,gr.organisationRole.organisation.user.userName,gr.organisationRole.roleName,gr.organisationRole.permission,gr.resolved,gr.media.publicId)"+
                            " from Grievance gr where gr.userFrom = :username",GrievanceDto.class)
                    .setParameter("username",username)
                    .getResultList();
    }

    @Transactional
    public void updateGrievance(String userName, String grievanceTitle, Boolean resolved) {
        // Fetch the grievance using username and title
        String queryStr = "SELECT gr FROM Grievance gr WHERE gr.userFrom.userName = :userName AND gr.Title = :grievanceTitle";
        Grievance grievance;

        try {
            grievance = entityManager.createQuery(queryStr, Grievance.class)
                    .setParameter("userName", userName)
                    .setParameter("grievanceTitle", grievanceTitle)
                    .getSingleResult();
        } catch (Exception e) {
            throw new RuntimeException("Grievance not found for username: " + userName + " and title: " + grievanceTitle, e);
        }

        grievance.setResolved(resolved);

        // Persist the changes
        entityManager.merge(grievance);
    }

    public boolean grievanceExists(String username, String grievanceTitle) {
        String queryStr = "SELECT COUNT(gr) FROM Grievance gr WHERE gr.userFrom.userName = :username AND gr.Title = :grievanceTitle";
        Long count = entityManager.createQuery(queryStr, Long.class)
                .setParameter("username", username)
                .setParameter("grievanceTitle", grievanceTitle)
                .getSingleResult();
        return count > 0;
    }


    public GrievanceDto getGrievance(String username, String grievanceTitle) {
        if(UserRepository.usernameExists(username) == -1L)
            throw new EmptyResultDataAccessException("User with username " + username + " not found", 1);
        if(!grievanceExists(username, grievanceTitle))
            throw new EmptyResultDataAccessException("Grievance with the title" + grievanceTitle + " not found", 1);

        else
            return entityManager.createQuery(" select new in.ac.iitj.instiapp.payload.GrievanceDto(gr.Title,gr.Description,gr.userFrom.userName,gr.organisationRole.organisation.user.userName,gr.organisationRole.roleName,gr.organisationRole.permission,gr.resolved,gr.media.publicId)" +
                    "FROM Grievance gr WHERE gr.userFrom.userName = :username AND gr.Title =:grievanceTitle",GrievanceDto.class)
                    .setParameter("username",username)
                    .setParameter("grievanceTitle",grievanceTitle)
                    .getSingleResult();
    }

    public void deleteGrievance(String userName , String grievanceTitle){
        // TODO
    }

    public boolean checkGrievance(String title , String user_from_id){
        // TODO
        return false;
    }






}