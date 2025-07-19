package in.ac.iitj.instiapp.Repository.impl;

import in.ac.iitj.instiapp.Repository.UserRepository;
import in.ac.iitj.instiapp.database.entities.Scheduling.Calendar.Calendar;
import in.ac.iitj.instiapp.database.entities.User.User;
import in.ac.iitj.instiapp.database.entities.User.Usertype;
import in.ac.iitj.instiapp.payload.User.Organisation.OrganisationRoleDto;
import in.ac.iitj.instiapp.payload.User.UserBaseDto;
import in.ac.iitj.instiapp.payload.User.UserDetailedDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private  final  JdbcTemplate jdbcTemplate;
    private  final  EntityManager entityManager;


    @Autowired
    public UserRepositoryImpl(JdbcTemplate jdbcTemplate, EntityManager entityManager) {
        this.jdbcTemplate = jdbcTemplate;
        this.entityManager = entityManager;
    }


    @Override
    public void save(Usertype userType) {
        if(userTypeExists(userType.getName()) != -1L){
            throw new DataIntegrityViolationException("Usertype already exists");
        }
        entityManager.persist(userType);
    }

    @Override
    public List<String> getAllUserTypes(Pageable pageable) {
        return entityManager.createQuery("select ut.name from Usertype ut ",String.class)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    @Override
    public Long userTypeExists(String name) {
        return jdbcTemplate.queryForObject("select COALESCE(MAX (id), -1) from user_type where name = ?  ",Long.class,name);
    }

    @Override
    public List<Long> getUserTypeIds(List<String> userTypeNames, Pageable pageable) {
        return entityManager.createQuery("select id from Usertype  where  name in :names", Long.class)
                .setParameter("names", userTypeNames)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    @Override
    public void update(String oldName, String newName) {

        if(userTypeExists(oldName) == -1L){
            throw new EmptyResultDataAccessException("No usertype " + oldName + "exists",1);
        }
        if(userTypeExists(newName) != -1L){
            throw new DataIntegrityViolationException("Usertype with name " + newName + " already exists");
        }

        entityManager.createQuery("update Usertype ut set ut.name = :newName where ut.name = :oldName")
                .setParameter("newName", newName)
                .setParameter("oldName", oldName)
                .executeUpdate();
    }

    @Override
    public void delete(String userTypeName) {
            if (userTypeExists(userTypeName) == -1){
                throw new EmptyResultDataAccessException("No usertype " + userTypeName + "exists",1);
            }

            //TODO
    }

    @Override
    @Transactional
    public Long save(User user) {
        user.setCalendar(null);
        User newUser = entityManager.merge(user);
        entityManager.flush();
        return newUser.getId();
    }

    @Override
    public UserBaseDto getUserLimited(String username) {
        try {


            return entityManager.createQuery("select  new in.ac.iitj.instiapp.payload.User.UserBaseDto(u.name, u.userName,u.email, u.userType.name, u.avatarUrl) from User  u where u.userName = :username", UserBaseDto.class)
                    .setParameter("username", username)
                    .getSingleResult();
        }
        catch (NoResultException e) {
            throw new EmptyResultDataAccessException("No user found with username " + username, 1);
        }
    }

    @Override
    public UserDetailedDto getUserDetailed(String username, boolean isPrivate) {
        Long id = usernameExists(username);
        if (id == -1) {
            throw new EmptyResultDataAccessException("No user found with username " + username, 1);
        }

        // choose the correct JPQL
        String ql = isPrivate
                ? "select new in.ac.iitj.instiapp.payload.User.UserDetailedDto(" +
                "u.name, u.userName, u.email, u.password, u.phoneNumber, u.userType.name, u.calendar.publicId, u.avatarUrl) " +
                "from User u where u.userName = :username"
                : "select new in.ac.iitj.instiapp.payload.User.UserDetailedDto(" +
                "u.name, u.userName, u.email, u.userType.name, u.avatarUrl) " +
                "from User u where u.userName = :username";

        List<UserDetailedDto> results = entityManager
                .createQuery(ql, UserDetailedDto.class)
                .setParameter("username", username)
                .getResultList();

        if (results.isEmpty()) {
            throw new EmptyResultDataAccessException("No user detail found for username " + username, 1);
        }
        return results.get(0);
    }


    @Override
    public UserDetailedDto getUserDetailed(String username) {

        String ql = """
                      select new in.ac.iitj.instiapp.payload.User.UserDetailedDto(
                          u.name,
                          u.userName,
                          u.email,
                          u.password,
                          u.phoneNumber,
                          ut.name,
                          c.publicId,
                          u.avatarUrl
                      )
                      from User u
                        join u.userType ut
                        left join u.calendar c
                      where u.userName = :username
                    """;

        return entityManager
                .createQuery(ql, UserDetailedDto.class)
                .setParameter("username", username)
                .getSingleResult();
    }


    @Override
    public List<UserBaseDto> getListUserLimitedByUsertype(String usertype, Pageable pageable) {
        return entityManager.createQuery("select new in.ac.iitj.instiapp.payload.User.UserBaseDto(u.name, u.userName, u.email, u.userType.name,u.avatarUrl) from User  u where u.userType.name = :usertypename",UserBaseDto.class)
                .setParameter("usertypename",usertype)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    @Override
    public Optional<OrganisationRoleDto> getOrganisationPermission(String username, String organisationUsername) {
        try {
          return   Optional.of(
                    entityManager.createQuery("select new in.ac.iitj.instiapp.payload.User.Organisation.OrganisationRoleDto(uor.organisation.user.userName, uor.roleName, uor.permission) from User  u join u.organisationRoleSet  uor where uor.organisation.user.userName = :organisationUsername and u.userName = :userUsername",OrganisationRoleDto.class)
                            .setParameter("organisationUsername",organisationUsername)
                            .setParameter("userUsername",username)
                            .getSingleResult()
            );
        }catch (NoResultException ignored){
            return Optional.empty();
        }
    }

    @Override
    public Set<OrganisationRoleDto> getOrganisationRoleDTOsByUsername(String username, Pageable pageable) {
        List<OrganisationRoleDto> roleDtoList = entityManager.createQuery(
                        "select new in.ac.iitj.instiapp.payload.User.Organisation.OrganisationRoleDto(uor.organisation.user.userName, uor.roleName, uor.permission) " +
                                "from User u join u.organisationRoleSet uor where u.userName = :username",
                        OrganisationRoleDto.class)
                .setParameter("username", username)
                .setMaxResults(pageable.getPageSize())
                .setFirstResult((int) pageable.getOffset())
                .getResultList();

        // Convert List to Set to remove duplicates and return
        return new LinkedHashSet<>(roleDtoList);
    }


    @Override
    public Long usernameExists(String username) {
        return jdbcTemplate.queryForObject("select coalesce(MAX(id), -1) from users where user_name = ?",Long.class,username);
    }

    @Override
    public Long emailExists(String email) {
        return jdbcTemplate.queryForObject("select coalesce(max(id), -1)  from users  u where  u.email = ?", Long.class, email);

    }

    @Override
    public void updateOauth2Info(String newName, String avatarURL, String userName) {
        if(usernameExists(userName) == -1L){
            throw new EmptyResultDataAccessException("No user found with username " + userName,1);
        }
        entityManager.createQuery("update User u set u.name = :name, u.avatarUrl = :avatarUrl where u.userName = :username")
                .setParameter("name",newName)
                .setParameter("avatarUrl",avatarURL)
                .setParameter("username",userName)
                .executeUpdate();
    }

    @Override
    public void setUserType(String username, String newUserType) {
        Long newUserTypeId = userTypeExists(newUserType);
        Long userId = usernameExists(username);
        if(newUserTypeId == -1L || userId == -1L){
            throw new EmptyResultDataAccessException("Usertype  " +newUserType + "not found",1);
        }

        entityManager.createNativeQuery("update users  set user_type_id = :newUserTypeId where id = :userId")
                .setParameter("newUserTypeId",newUserTypeId)
                .setParameter("userId",userId)
                .executeUpdate();
    }

    @Override
    public void updatePhoneNumber(String username, String newPhoneNumber) {
        Long userId = usernameExists(username);
        if(userId == -1L){
            throw new EmptyResultDataAccessException("No user found with username " + username,1);
        }
        entityManager.createQuery("update User u set u.phoneNumber = :newPhoneNumber where u.id = :userId")
                .setParameter("newPhoneNumber",newPhoneNumber)
                .setParameter("userId",userId)
                .executeUpdate();
    }

    public String getUserNameFromEmail(String email){
        return entityManager.createQuery(
                        "select u.userName from User u where u.email = :email", String.class)
                .setParameter("email", email)
                .getSingleResult();
    }

    @Override
    public Long getUserIdFromUsername(String username) {
        return entityManager.createQuery(
                        "SELECT u.id FROM User u WHERE u.userName = :username", Long.class)
                .setParameter("username", username)
                .getSingleResult();
    }

    public User getUserFromUsername(String username){
        return entityManager.createQuery("SELECT u FROM User u WHERE u.userName = :username", User.class)
                .setParameter("username",username)
                .getSingleResult();
    }

    @Override
    public User findByGoogleId(String googleId) {
        try {
            return entityManager.createQuery("SELECT u FROM User u WHERE u.googleId = :googleId", User.class)
                    .setParameter("googleId", googleId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }





}
