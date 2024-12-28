package in.ac.iitj.instiapp.Repository.impl;

import in.ac.iitj.instiapp.Repository.WellBeingRepository;
import in.ac.iitj.instiapp.database.entities.User.Student.Alumni.Alumni;
import in.ac.iitj.instiapp.database.entities.User.User;
import in.ac.iitj.instiapp.database.entities.User.Wellbeingmoderator.WellBeingMember;
import in.ac.iitj.instiapp.payload.User.WellBeingModerator.WellBeingMemberDtoFull;
import in.ac.iitj.instiapp.payload.User.WellBeingModerator.WellBeingMemberDtoLimited;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
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
    public void save(WellBeingMember member) {
        User user = entityManager.getReference(User.class, member.getUser().getId());
        member.setUser(user);
        entityManager.persist(member);

    }

    @Override
    public List<WellBeingMemberDtoLimited> getAllMembers(Pageable pageable) {
       return entityManager.createQuery("select new in.ac.iitj.instiapp.payload.User.WellBeingModerator.WellBeingMemberDtoLimited(wbm.user.userName, wbm.qualification,wbm.availability) from WellBeingMember wbm",WellBeingMemberDtoLimited.class)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }




    @Override
    public WellBeingMemberDtoFull findByUsernameFull(String username){
        if(memberExists(username)) {
            return (WellBeingMemberDtoFull) entityManager.createQuery("SELECT " +
                            "new in.ac.iitj.instiapp.payload.User.WellBeingModerator.WellBeingMemberDtoFull"+
                            "( :username, wm.qualification, wm.availability)" +
                            "FROM WellBeingMember wm  where wm.user.userName = :username",WellBeingMemberDtoFull.class)
                    .setParameter("username" , username)
                    .getSingleResult();
        }
        else {
            throw new EmptyResultDataAccessException("the member with username " + username + " does not exist",1);
        }
    }






    @Override
    public boolean memberExists(String username) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject("SELECT EXISTS (SELECT 1 FROM well_being_member w JOIN users u ON u.id = w.user_id WHERE u.user_name = ?)", Boolean.class, username));
    }

    @Override
    public void updateMember(WellBeingMember wellBeingMember) {

        entityManager.createQuery("UPDATE  WellBeingMember  wm set wm.availability = :availability, wm.qualification = :qualification where wm.user.id = :userId")
                .setParameter("availability", wellBeingMember.getAvailability())
                .setParameter("qualification", wellBeingMember.getQualification())
                .setParameter("userId", wellBeingMember.getUser().getId())
                .executeUpdate();
    }


    @Override
    public Long deleteMember(String username) {

        if(memberExists(username)) {
            jdbcTemplate.queryForObject("DELETE FROM well_being_member w USING users u WHERE u.id = w.user_id AND u.user_name = :userName RETURNING u.id;\n",Long.class, username);
        }
        throw new EmptyResultDataAccessException("the member with username " + username + " does not exist",1);
    }
}
