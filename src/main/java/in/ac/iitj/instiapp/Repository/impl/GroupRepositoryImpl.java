package in.ac.iitj.instiapp.Repository.impl;

import in.ac.iitj.instiapp.Repository.GroupRepository;
import in.ac.iitj.instiapp.database.entities.User.Group;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class GroupRepositoryImpl implements GroupRepository {

    private final JdbcTemplate jdbcTemplate;
    private final EntityManager entityManager;


    @Autowired
    public GroupRepositoryImpl(EntityManager entityManager, JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.entityManager = entityManager;
    }


    @Override
    public Long save(Group group) {
        entityManager.persist(group);
        return group.getId();
    }



    @Override
    public List getGroupIdsByGroup(Long userId, Long branchId, Long programId, List<Long> organisationRoleIds, Long userTypeId, Map<String, Map<String, String >> userTypeAttributes, Pageable pageable) {
    return   entityManager.createNativeQuery("select g.id from groups g  where " +
              "case when :userId is not null then :userId = any(g.user_ids) else false end or " +
              "case when :branchId is not null then :branchId = any(g.branch_ids) else false end or " +
              "case when :programId is not null then :programId = any(g.program_ids) else false end or " +
              ":organisationRoleIds && g.organisation_role_ids or " +
              "case when :userTypeId is not null then "+
                              ":userTypeId = any(g.user_type_ids) and "+
                              "case when g.user_type_attributes is not null then " +
                                        "case when :userTypeAttributes is not null then " +
                                        "slice(g.user_type_attributes, akeys(:userTypeAttributes)) = :userTypeAttributes else false end " +
                              "else true end "+
              "else false end limit :pageSize offset :offset", Long.class
              )
              .setParameter("userId", userId)
              .setParameter("branchId", branchId)
              .setParameter("programId", programId)
              .setParameter("organisationRoleIds", organisationRoleIds)
              .setParameter("userTypeId", userTypeId)
              .setParameter("userTypeAttributes", userTypeAttributes)
              .setParameter("pageSize", pageable.getPageSize())
              .setParameter("offset", pageable.getOffset())
              .getResultList();

    }

    @Override
    public Long existByPublicId(String publicId) {
        return jdbcTemplate.queryForObject("select coalesce(max(g.id), -1) from groups g  where g.public_id= ? ", Long.class, publicId);
    }


    @Override
    public void update(String publicId, Group group) {

        if(existByPublicId(publicId) == -1L){
            throw new EmptyResultDataAccessException("The group doesn't exist ",1);
        }

        entityManager.createQuery("update Group g set " +
                "g.name = case when :name is null then g.name else :name end, " +
                "g.userIds = case when :userIds is null then g.userIds else :userIds end, " +
                "g.branchIds = case when :branchIds is null then g.branchIds else :branchIds end, " +
                "g.programIds = case when :programIds is null then g.programIds else :programIds end, " +
                "g.organisationRoleIds = case when :organisationRoleIds is null then g.organisationRoleIds else :organisationRoleIds end, " +
                "g.userTypeIds = case when :userTypeIds is null then g.userTypeIds else :userTypeIds end, " +
                "g.userTypeAttributes = case when :userTypeAttributes is null then g.userTypeAttributes else :userTypeAttributes end where g.publicId = :publicId")
                .setParameter("name", group.getName())
                .setParameter("userIds", group.getUserIds())
                .setParameter("branchIds", group.getBranchIds())
                .setParameter("programIds", group.getProgramIds())
                .setParameter("organisationRoleIds", group.getOrganisationRoleIds())
                .setParameter("userTypeIds", group.getUserTypeIds())
                .setParameter("userTypeAttributes", group.getUserTypeAttributes())
                .setParameter("publicId", publicId)
                .executeUpdate();
    }

    @Override
    public void deleteByPublicId(String publicId) {
        entityManager.createQuery("delete from Group where publicId = :publicId").executeUpdate();
    }
}
