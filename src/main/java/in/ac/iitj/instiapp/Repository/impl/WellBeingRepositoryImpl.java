package in.ac.iitj.instiapp.Repository.impl;

import in.ac.iitj.instiapp.Repository.WellBeingRepository;
import in.ac.iitj.instiapp.database.entities.Scheduling.MessMenu.MessMenu;
import in.ac.iitj.instiapp.database.entities.User.Wellbeingmoderator.WellBeingMember;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.NoSuchElementException;

@Repository
public class WellBeingRepositoryImpl implements WellBeingRepository {

    private  final JdbcTemplate jdbcTemplate;
    private  final EntityManager entityManager;


    @Autowired
    public WellBeingRepositoryImpl(JdbcTemplate jdbcTemplate, EntityManager entityManager) {
        this.jdbcTemplate = jdbcTemplate;
        this.entityManager = entityManager;
    }


    @Override
    public void save(WellBeingMember member) {
        entityManager.persist(member);

    }

    @Override
    public void updateQualification(String username, String new_qualification) {
        if(memberExists(username)) {
            String sql = "UPDATE well_being_member SET qualification = ? WHERE username = ?";
            jdbcTemplate.update(sql, new_qualification, username);
        }
        throw new NoSuchElementException("the member with username " + username + " does not exist");

    }

    @Override
    public void updateAvailability(String username, String new_availability) {
        if (memberExists(username)) {
            String sql = "UPDATE well_being_member SET availability = ? WHERE username = ?";
            jdbcTemplate.update(sql, new_availability, username);
        }
        throw new NoSuchElementException("the member with username " + username + " does not exist");

    }

    @Override
    public List<WellBeingMember> getAllMembers() {
        String sql = "SELECT * FROM well_being_member";
        // Create the query using EntityManager
        Query query = entityManager.createNativeQuery(sql, WellBeingMember.class);
        return query.getResultList();
    }

    @Override
    public boolean memberExists(String username) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject("select exists from well_being_member where username=?", Boolean.class, username));
    }

    @Override
    public void deleteMember(String username) {
        if(memberExists(username)) {
            String sql = "DELETE FROM well_being_member WHERE username = ?";
            jdbcTemplate.update(sql, username);
        }
        throw new NoSuchElementException("the member with username " + username + " does not exist");
    }
}
