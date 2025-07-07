package in.ac.iitj.instiapp.Repository.impl;

import in.ac.iitj.instiapp.Repository.User.Organisation.OrganisationRepository;
import in.ac.iitj.instiapp.Repository.User.Organisation.OrganisationRoleRepository;
import in.ac.iitj.instiapp.Repository.UserRepository;
import in.ac.iitj.instiapp.database.entities.User.Organisation.OrganisationRole;
import in.ac.iitj.instiapp.database.entities.User.Organisation.OrganisationType;
import in.ac.iitj.instiapp.payload.User.Organisation.OrganisationBaseDto;
import in.ac.iitj.instiapp.payload.User.Organisation.OrganisationRoleDto;
import in.ac.iitj.instiapp.payload.User.UserBaseDto;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Repository
public class OrganisationRoleRepositoryImpl implements OrganisationRoleRepository {

    private final EntityManager entityManager;
    private final JdbcTemplate jdbcTemplate;
    private final OrganisationRepository organisationRepository;

    @Autowired
    public OrganisationRoleRepositoryImpl(EntityManager entityManager, JdbcTemplate jdbcTemplate , OrganisationRepository organisationRepository) {
        this.entityManager = entityManager;
        this.jdbcTemplate = jdbcTemplate;
        this.organisationRepository = organisationRepository;
    }

    @Override
    public void saveOrganisationRole(OrganisationRole organisationRole) {
        entityManager.persist(organisationRole);
    }
    @Override
    public List<OrganisationRoleDto> getOrganisationRoles(String organisationUserName, Pageable pageable) {
            return entityManager.createQuery("select new in.ac.iitj.instiapp.payload.User.Organisation.OrganisationRoleDto(o.organisation.user.userName,o.roleName,o.permission) from OrganisationRole o where o.organisation.user.userName= :organisationUserName",OrganisationRoleDto.class)
                .setParameter("organisationUserName",organisationUserName)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    @Override
    public Long existOrganisationRole(String organisationName, String roleName) {
        return jdbcTemplate.queryForObject("select COALESCE(r.id, -1) from organisation_role r " + "join organisation o on o.id = r.organisation_id join users u on o.user_id = u.id " + "where r.role_name = ? and u.user_name = ?", Long.class, roleName, organisationName);
    }

    @Override
    public List<Long> getOrganisationRoleIds(List<String> organisationName, List<String> organisationRoleName, Pageable pageable) {
        return entityManager.createQuery("select id from OrganisationRole  where organisation.user.userName in :names and roleName in :roleNames",Long.class)
                .setParameter("names", organisationName)
                .setParameter("roleNames", organisationRoleName)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }


    @Override
    public void updateOrganisationRole(OrganisationRole oldOrganisationRole,OrganisationRole newOrganisationRole){
        if(existOrganisationRole(oldOrganisationRole.getOrganisation().getUser().getUserName(), oldOrganisationRole.getRoleName()) == -1L){
            throw new EmptyResultDataAccessException("No role " + oldOrganisationRole.getRoleName() + "exists", 1);
        }
        if(existOrganisationRole(oldOrganisationRole.getOrganisation().getUser().getUserName(), newOrganisationRole.getRoleName()) != -1L){
            throw new DataIntegrityViolationException("Organisation role with name " + newOrganisationRole.getRoleName() + " already exists");
        }

        entityManager.createQuery("UPDATE OrganisationRole  set roleName = CASE WHEN :newRoleName IS NULL THEN roleName ELSE :newRoleName END, " +
                        "permission = CASE WHEN :newPermission IS NULL THEN permission ELSE :newPermission END " +
                        "WHERE organisation.user.userName = :organisationUserName" +
                        " AND roleName = :oldRoleName")
                .setParameter("newRoleName", newOrganisationRole.getRoleName())
                .setParameter("newPermission", newOrganisationRole.getPermission().name())
                .setParameter("organisationUserName", oldOrganisationRole.getOrganisation().getUser().getUserName())
                .setParameter("oldRoleName", oldOrganisationRole.getRoleName())
                .executeUpdate();

    }

    @Override
    public void deleteOrganisationRole(String organisationRoleName, String OrganisationName,String deletedRoleName){
        //TODO
    }

    @Override
    public void insertIntoOrganisationRole(String organisationUsername, String organisationRoleName, Long idOfPerson){
        Long organisationRoleId = existOrganisationRole(organisationUsername, organisationRoleName);
        if (organisationRoleId == -1L) {
            throw new EmptyResultDataAccessException("No role " + organisationRoleName + " exists for organisation " + organisationUsername, 1);
        }

        Long exists =(Long) entityManager.createNativeQuery("SELECT  count(1) from  users_organisation_role_set where organisation_role_set_id = ? and user_id = ?")
                .setParameter(1, organisationRoleId)
                .setParameter(2,  idOfPerson)
                .getSingleResult();



        if (exists > 0) {
            throw new DataIntegrityViolationException("User already exists in an organisation at a role");
        }

        // Insert into organisation_role_set
        entityManager.createNativeQuery(
                        "insert into users_organisation_role_set (organisation_role_set_id, user_id) values (?, ?)")
                .setParameter(1, organisationRoleId)
                .setParameter(2, idOfPerson)
                .executeUpdate();
    }

    @Override
    public List<Map<UserBaseDto, OrganisationRoleDto>> getAllOrganisationRoles(String usernameOfOrganisation, Pageable pageable){
        return entityManager.createQuery("select   new in.ac.iitj.instiapp.payload.User.UserBaseDto(o.userName),new  in.ac.iitj.instiapp.payload.User.Organisation.OrganisationRoleDto(or.organisation.user.userName, or.roleName, or.permission) from User  o join o.organisationRoleSet or where or.organisation.user.userName = :organisationUserName",Object[].class)
                .setParameter("organisationUserName", usernameOfOrganisation)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList()
                .stream()
                .map(result -> {
                    UserBaseDto userBaseDto = (UserBaseDto) ((Object[]) result)[0];
                    OrganisationRoleDto organisationRoleDto = (OrganisationRoleDto) ((Object[]) result)[1];
                    Map<UserBaseDto, OrganisationRoleDto> map = new HashMap<>();
                    map.put(userBaseDto, organisationRoleDto);
                    return map;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void removePersonFromOrganisationRole(Long organisationId, String organisationRoleName, Long idOfPerson) {
        // Check if the person exists in the specified role
        String sql = """
    SELECT count(1)
    FROM organisation org
    JOIN organisation_role orl ON orl.organisation_id = org.id
    JOIN users_organisation_role_set uors ON uors.organisation_role_set_id = orl.id
    WHERE org.id = ?
      AND orl.role_name = ?
      AND uors.user_id = ?
""";

        Long existingCount = (Long) entityManager.createNativeQuery(sql)
                .setParameter(1 , organisationId )
                .setParameter(2 , organisationRoleName)
                .setParameter(3 , idOfPerson)
                .getSingleResult()
        ;

        if (existingCount == 0) {
            throw new EmptyResultDataAccessException("Person not found in the specified organisation role", 1);
        }

        // Remove the person from the organisation role
        entityManager.createNativeQuery(
                        "delete from users_organisation_role_set ors " +
                                "where ors.user_id = ? and " +
                                "ors.organisation_role_set_id = (select id from organisation_role where organisation_id = ? and role_name = ?)")
                .setParameter(1, idOfPerson)
                .setParameter(2, organisationId)
                .setParameter(3, organisationRoleName)
                .executeUpdate();
    }





}
