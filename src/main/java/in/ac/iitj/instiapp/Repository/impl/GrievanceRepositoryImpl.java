package in.ac.iitj.instiapp.Repository.impl;


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
}
