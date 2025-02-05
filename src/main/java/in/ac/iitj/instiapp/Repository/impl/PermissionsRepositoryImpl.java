package in.ac.iitj.instiapp.Repository.impl;

import in.ac.iitj.instiapp.Repository.PermissionsRepository;
import in.ac.iitj.instiapp.database.entities.User.Permissions;
import in.ac.iitj.instiapp.database.entities.User.PermissionsData;
import jakarta.persistence.EntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PermissionsRepositoryImpl implements PermissionsRepository {

    private final EntityManager entityManager;
    private final JdbcTemplate jdbcTemplate;

    public PermissionsRepositoryImpl(EntityManager entityManager, JdbcTemplate jdbcTemplate) {
        this.entityManager = entityManager;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void savePermission(Permissions permissions) {
        if(existsPermission(permissions.getPermissionsData()) != -1L){
            throw new DataIntegrityViolationException("Permissions already exists with name " + permissions.getPermissionsData());
        }
        entityManager.persist(permissions);
    }

    @Override
    public Long existsPermission(PermissionsData permission) {
        return jdbcTemplate.queryForObject(
                "SELECT COALESCE(MAX(id), -1) FROM permissions WHERE permissions_data = ?",
                Long.class,
                permission.toString()
        );
    }

    @Override
    public List<Permissions> getPermissions(Pageable pageable){
        return entityManager.createQuery("select p from Permissions p", Permissions.class)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    @Override
    public void updatePermission(Permissions oldPermissions, Permissions newPermissions) {
        Long oldId = existsPermission(oldPermissions.getPermissionsData());
        if(oldId == -1){
            throw new EmptyResultDataAccessException("Permission " + oldPermissions.getPermissionsData() + " doesn't exists", 1);
        }
        if(existsPermission(newPermissions.getPermissionsData()) != -1L){
            throw new DataIntegrityViolationException("Permissions " + newPermissions.getPermissionsData() + " already exists");
        }

        Permissions permission = entityManager.find(Permissions.class, oldId);
        permission.setPermissionsData(newPermissions.getPermissionsData());
        entityManager.flush();
    }

    @Override
    public void deletePermission(Permissions permissions){
        //todo
    }

}
