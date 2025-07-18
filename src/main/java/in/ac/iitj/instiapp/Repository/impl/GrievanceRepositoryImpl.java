/*package in.ac.iitj.instiapp.Repository.impl;


import in.ac.iitj.instiapp.database.entities.Grievance;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.transaction.annotation.Transactional;
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
}*/

package in.ac.iitj.instiapp.Repository.impl;
import in.ac.iitj.instiapp.Repository.GrievanceRepository;
import in.ac.iitj.instiapp.Repository.MediaRepository;
import in.ac.iitj.instiapp.database.entities.Grievance;
import in.ac.iitj.instiapp.database.entities.User.Organisation.OrganisationRole;
import in.ac.iitj.instiapp.payload.GrievanceDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;
import in.ac.iitj.instiapp.database.entities.User.User;
import in.ac.iitj.instiapp.database.entities.Media.Media;
import in.ac.iitj.instiapp.Repository.UserRepository;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
@Slf4j
public class GrievanceRepositoryImpl implements GrievanceRepository {

    private final JdbcTemplate jdbcTemplate;
    private final EntityManager entityManager;
    private final MediaRepository mediaRepository;

    GrievanceRepositoryImpl(JdbcTemplate jdbcTemplate, EntityManager entityManager, MediaRepository mediaRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.entityManager = entityManager;
        this.mediaRepository = mediaRepository;
    }

    @Transactional
    public String save(Grievance grievance) {
        //grievance.setUserFrom(entityManager.getReference(User.class,grievance.getUserFrom().getId()));
        //grievance.setOrganisationRole(entityManager.getReference(OrganisationRole.class,grievance.getOrganisationRole().getId()));
        //grievance.setMedia(entityManager.getReference(Media.class,grievance.getMedia().getId()));
        Media media = new Media();
        media.setPublicUrl(grievance.getMedia().getPublicUrl());
        mediaRepository.save(media);

        grievance.setMedia(media);

        grievance.setTitle(grievance.getTitle());
        grievance.setDescription(grievance.getDescription());
        grievance.setResolved(false);

        entityManager.persist(grievance);
        return grievance.getPublicId();
    }

    public List<GrievanceDto> getGrievancesByFilter(Optional<String> title, Optional<String> description,Optional<String> organisationName, Optional<Boolean> resolved,Pageable pageable){

            return entityManager.createQuery("select new in.ac.iitj.instiapp.payload.GrievanceDto(gr.publicId,gr.Title,gr.Description,gr.userFrom.userName,gr.organisationRole.organisation.user.userName,gr.organisationRole.roleName,gr.organisationRole.permission,gr.resolved,gr.media.publicId) from Grievance gr where"+
                                    "(:title is null or gr.Title = :title) and " +
                                    "(:description is null or gr.Description = :description) and " +
                                    "(:organisationName is null or gr.organisationRole.organisation.user.userName = :organisationName) and "+
                                    "(:resolved is null or gr.resolved = :resolved)",
                            GrievanceDto.class)
                    .setParameter("title", title.orElse(null))
                    .setParameter("description", description.orElse(null))
                    .setParameter("organisationName", organisationName.orElse(null))
                    .setParameter("resolved",resolved.orElse(null))
                    .setFirstResult((int) pageable.getOffset())
                    .setMaxResults(pageable.getPageSize())
                    .getResultList();
    }

    @Override
    @Transactional
    public void updateGrievance(String publicId, Grievance updated) {
        Grievance existing = entityManager.createQuery(
                        "SELECT g FROM Grievance g WHERE g.publicId = :pubId",
                        Grievance.class)
                .setParameter("pubId", publicId)
                .getSingleResult();

        // 2) Update simple fields if non-null
        if (updated.getTitle() != null) {
            existing.setTitle(updated.getTitle());
        }
        if (updated.getDescription() != null) {
            existing.setDescription(updated.getDescription());
        }
        if (updated.getResolved() != null) {
            existing.setResolved(updated.getResolved());
        }


        // 4) Update Media if provided

//        // Create & save a fresh Media entity
//        Media incoming = existing.getMedia();
//        Media m = new Media();
//        m.setType(incoming.getType());
//        m.setPublicUrl(incoming.getPublicUrl());
//        m.setAssetId(UUID.randomUUID().toString());
//        entityManager.merge(m);
//
//
//        existing.setMedia(m);

        // 5) Merge the changes
        entityManager.merge(existing);
    }



    public Long existGrievance(String publicId) {
        String queryStr = "SELECT COUNT(gr) FROM Grievance gr WHERE gr.publicId = :publicId";
        Long count = entityManager.createQuery(queryStr, Long.class)
                .setParameter("publicId", publicId)
                .getSingleResult();
        if(count == 0)
            return -1L;
        else
            return count;
    }


    public GrievanceDto getGrievance(String publicId){

        return entityManager.createQuery("select new in.ac.iitj.instiapp.payload.GrievanceDto(gr.publicId,gr.Title,gr.Description,gr.userFrom.userName,gr.organisationRole.organisation.user.userName,gr.organisationRole.roleName,gr.organisationRole.permission,gr.resolved,gr.media.publicId)"+
                        " from Grievance gr " +
                        "where gr.publicId = :publicId",GrievanceDto.class)
                .setParameter("publicId",publicId)
                .getSingleResult();
    }


    @Override
    @Transactional
    public void deleteGrievance(String publicId) {
        int deleted = entityManager.createQuery(
                        "DELETE FROM Grievance g WHERE g.publicId = :publicId")
                .setParameter("publicId", publicId)
                .executeUpdate();

        if (deleted == 0) {
            throw new EntityNotFoundException(
                    "No grievance found with publicId=" + publicId);
        }
    }

    public boolean doesOwn(String publicId, String username){
        String usernameOfGrievance = entityManager.createQuery(
                        "SELECT g.userFrom.userName FROM Grievance g WHERE g.publicId = :publicId",
                        String.class
                ).setParameter("publicId", publicId)
                .getSingleResult();

        log.debug("doesOwn: checking publicId='{}' for user='{}'", publicId, username);

        return usernameOfGrievance.equals(username);
    }






}