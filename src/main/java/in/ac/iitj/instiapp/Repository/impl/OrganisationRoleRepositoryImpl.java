package in.ac.iitj.instiapp.Repository.impl;

import in.ac.iitj.instiapp.Repository.User.Organisation.OrganisationRoleRepository;
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

    @Autowired
    public OrganisationRoleRepositoryImpl(EntityManager entityManager, JdbcTemplate jdbcTemplate) {
        this.entityManager = entityManager;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void saveOrganisationRole(OrganisationRole organisationRole) {
        entityManager.persist(organisationRole);
    }

    @Override
    public List<OrganisationRoleDto> getOrganisationRoles(String organisationName, Pageable pageable) {
            return entityManager.createQuery("select new in.ac.iitj.instiapp.payload.User.Organisation.OrganisationRoleDto(o.organisation.user.name,o.roleName,o.permission) from OrganisationRole o where o.organisation.user.name= :organisationName",OrganisationRoleDto.class)
                .setParameter("organisationName",organisationName)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    @Override
    public Long existOrganisationRole(String organisationName, String roleName) {
        return jdbcTemplate.queryForObject("select COALESCE(MAX(r.id), -1) from OrganisationRole r " + "join organisation o on o.id = r.organisation_id " + "where r.role_name = ? and o.name = ?", Long.class, roleName, organisationName);
    }

    @Override
    public void updateOrganisationRole(OrganisationRole oldOrganisationRole,OrganisationRole newOrganisationRole){
        if(existOrganisationRole(oldOrganisationRole.getOrganisation().getUser().getName(), oldOrganisationRole.getRoleName()) == -1L){
            throw new EmptyResultDataAccessException("No role " + oldOrganisationRole.getRoleName() + "exists", 1);
        }
        if(existOrganisationRole(newOrganisationRole.getOrganisation().getUser().getName(), newOrganisationRole.getRoleName()) != -1L){
            throw new DataIntegrityViolationException("Organisation role with name " + newOrganisationRole.getRoleName() + " already exists");
        }

        entityManager.createNativeQuery(
                        "update organisation_role set " +
                                "role_name = case when ? is null then role_name else ? end, " +
                                "organisation_id = case when ? is null then organisation_id else " +
                                "(select id from organisation where user_id = (select id from users where name = ?)) end " +
                                "where organisation_id = (select id from organisation where user_id = (select id from users where name = ?)) " +
                                "and role_name = ?")
                .setParameter(1, newOrganisationRole.getRoleName())
                .setParameter(2, newOrganisationRole.getRoleName())
                .setParameter(3, newOrganisationRole.getOrganisation().getId())
                .setParameter(4, newOrganisationRole.getOrganisation().getUser().getName())
                .setParameter(5, oldOrganisationRole.getOrganisation().getUser().getName())
                .setParameter(6, oldOrganisationRole.getRoleName())
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
        Long existingCount = (Long) entityManager.createNativeQuery(
                        "select count(*) from organisation_role_set where person_id = ?")
                .setParameter(1, idOfPerson)
                .getSingleResult();

        if (existingCount > 0) {
            throw new DataIntegrityViolationException("User already exists in an organisation role");
        }

        // Insert into organisation_role_set
        entityManager.createNativeQuery(
                        "insert into organisation_role_set (organisation_role_id, person_id) values (?, ?)")
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
        Long existingCount = (Long) entityManager.createNativeQuery(
                        "select count(*) from organisation_role_set ors " +
                                "join organisation_role or on or.id = ors.organisation_role_id " +
                                "where or.organisation_id = ? and or.role_name = ? and ors.person_id = ?")
                .setParameter(1, organisationId)
                .setParameter(2, organisationRoleName)
                .setParameter(3, idOfPerson)
                .getSingleResult();

        if (existingCount == 0) {
            throw new EmptyResultDataAccessException("Person not found in the specified organisation role", 1);
        }

        // Remove the person from the organisation role
        entityManager.createNativeQuery(
                        "delete from organisation_role_set ors " +
                                "where ors.person_id = ? and " +
                                "ors.organisation_role_id = (select id from organisation_role where organisation_id = ? and role_name = ?)")
                .setParameter(1, idOfPerson)
                .setParameter(2, organisationId)
                .setParameter(3, organisationRoleName)
                .executeUpdate();
    }





}
