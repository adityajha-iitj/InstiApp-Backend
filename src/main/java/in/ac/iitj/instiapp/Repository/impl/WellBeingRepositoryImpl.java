package in.ac.iitj.instiapp.Repository.impl;

import in.ac.iitj.instiapp.Repository.WellBeingRepository;
import in.ac.iitj.instiapp.database.entities.User.User;
import in.ac.iitj.instiapp.database.entities.User.Wellbeingmoderator.WellBeingMember;
import in.ac.iitj.instiapp.payload.User.WellBeingModerator.WellBeingMemberDtoFull;
import in.ac.iitj.instiapp.payload.User.WellBeingModerator.WellBeingMemberDtoLimited;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
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

    @Transactional
    @Override
    public void save(WellBeingMember member ,long user_id) {
        User user = entityManager.getReference(User.class, user_id);
        member.setUser(user);
        entityManager.persist(member);

    }

    @Override
    public void updateQualification(String username, String new_qualification) {
        if(memberExists(username)) {
            String sql = "UPDATE well_being_member wm " +
                    "SET qualification = ? " +
                    "FROM users u "+
                    "WHERE wm.user_id = u.id AND u.user_name = ?";

            jdbcTemplate.update(sql, new_qualification, username);        }
        else {
            throw new NoSuchElementException("the member with username " + username + " does not exist");
        }

    }

    @Override
    public void updateAvailability(String username, String new_availability) {
        if (memberExists(username)) {
            String sql = "UPDATE well_being_member wm " +
                    "SET availability = ? " +
                    "FROM users u " +
                    "WHERE wm.user_id = u.id AND u.user_name = ?";

            jdbcTemplate.update(sql, new_availability, username);
        }
        else{
            throw new NoSuchElementException("the member with username " + username + " does not exist");
        }


    }

    @Override
    public WellBeingMemberDtoLimited findByUsernameLimited(String username){
        if(memberExists(username)) {
            return (WellBeingMemberDtoLimited) entityManager.createQuery(
                            "SELECT new in.ac.iitj.instiapp.payload.User.WellBeingModerator.WellBeingMemberDtoLimited" +
                                    "(u.name, u.userName, u.email, c.public_id, a.publicId, wm.qualification, wm.availability) " +
                                    "FROM User u " +
                                    "JOIN WellBeingMember wm ON u.id = wm.user.id " +
                                    "JOIN UserAvatar a ON u.avatar.id = a.id " +
                                    "JOIN Calendar c ON u.calendar.id = c.id "+
                                    "WHERE u.userName = :username")
                    .setParameter("username", username)
                    .getSingleResult();
        }
        else {
            throw new NoSuchElementException("the member with username " + username + " does not exist");
        }
    }

    @Override
    public WellBeingMemberDtoFull findByUsernameFull(String username){
        if(memberExists(username)) {
            return (WellBeingMemberDtoFull) entityManager.createQuery("SELECT " +
                            "new in.ac.iitj.instiapp.payload.User.WellBeingModerator.WellBeingMemberDtoFull"+
                            "(u.name, u.userName, u.email, u.phoneNumber, c.public_id, a.publicId, wm.qualification, wm.availability)" +
                            "FROM User u " +
                            "JOIN WellBeingMember wm ON u.id = wm.user.id " +
                            "JOIN UserAvatar a ON u.avatar.id = a.id " +
                            "JOIN Calendar c ON u.calendar.id = c.id "+
                            "WHERE u.userName = :username")
                    .setParameter("username" , username)
                    .getSingleResult();
        }
        else {
            throw new NoSuchElementException("the member with username " + username + " does not exist");
        }
    }


    @Override
    public List<WellBeingMemberDtoLimited> getAllMembers() {
        // Create the query using EntityManager
        return entityManager.createQuery(
                        "SELECT new in.ac.iitj.instiapp.payload.User.WellBeingModerator.WellBeingMemberDtoLimited" +
                                "(u.name, u.userName, u.email, c.public_id, a.publicId, wm.qualification , wm.availability)" +
                                " FROM User u " +
                                "JOIN WellBeingMember wm ON u.id = wm.user.id " +
                                "JOIN UserAvatar a ON u.avatar.id = a.id " +
                                "JOIN Calendar c ON u.calendar.id = c.id", WellBeingMemberDtoLimited.class)
                .getResultList();
    }

    @Override
    public boolean memberExists(String username) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject("SELECT EXISTS (SELECT 1 FROM well_being_member w JOIN users u ON u.id = w.user_id WHERE u.user_name = ?)", Boolean.class, username));
    }

    @Override
    public void deleteMember(String username) {
        if(memberExists(username)) {
            String sql = "DELETE wm FROM well_being_member JOIN users u ON u.id = wm.user_id WHERE u.user_name = ?";
            jdbcTemplate.update(sql, username);
        }
        throw new NoSuchElementException("the member with username " + username + " does not exist");
    }
}
