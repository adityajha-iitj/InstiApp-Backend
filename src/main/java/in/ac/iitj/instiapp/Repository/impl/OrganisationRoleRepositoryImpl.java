package in.ac.iitj.instiapp.Repository.impl;

import in.ac.iitj.instiapp.database.entities.User.Organisation.OrganisationRole;
import in.ac.iitj.instiapp.database.entities.User.Organisation.OrganisationType;
import in.ac.iitj.instiapp.payload.User.Organisation.OrganisationBaseDto;
import in.ac.iitj.instiapp.payload.User.Organisation.OrganisationRoleDto;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrganisationRoleRepositoryImpl {

    private final EntityManager entityManager;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public OrganisationRoleRepositoryImpl(EntityManager entityManager, JdbcTemplate jdbcTemplate) {
        this.entityManager = entityManager;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(OrganisationRole organisationRole) {
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
        return jdbcTemplate.queryForObject("select COALESCE(MAX(r.id), -1) from organisation_role r " + "join organisation o on o.id = r.organisation_id " + "where r.role_name = ? and o.name = ?", Long.class, roleName, organisationName);
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
    public void insertIntoOrganisationRole(String organisationUsername, String organisationRoleName, Long idOfPerson){
        Long id = existOrganisationRole(organisationUsername, organisationRoleName);
        insert into organisation_role_set(id, idOfPerson);


    }





}
