package in.ac.iitj.instiapp.Repository.impl;

import in.ac.iitj.instiapp.Repository.User.Organisation.OrganisationRepository;
import in.ac.iitj.instiapp.database.entities.Media.Media;
import in.ac.iitj.instiapp.database.entities.User.Organisation.Organisation;
import in.ac.iitj.instiapp.database.entities.User.Organisation.OrganisationType;
import in.ac.iitj.instiapp.database.entities.User.User;
import in.ac.iitj.instiapp.payload.User.Organisation.OrganisationBaseDto;
import in.ac.iitj.instiapp.payload.User.Organisation.OrganisationDetailedDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class OrganisationRepositoryImpl implements OrganisationRepository {

    private final EntityManager entityManager;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public OrganisationRepositoryImpl(EntityManager entityManager, JdbcTemplate jdbcTemplate) {
        this.entityManager = entityManager;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void saveOrganisationType(OrganisationType organisationType) {
        if(existsOrganisationType(organisationType.getName()) != -1L){
            throw new DataIntegrityViolationException("Organisation type already exists with name " + organisationType.getName());
        }

        entityManager.persist(organisationType);
    }

    @Override
    public List<String> getAllOrganisationTypes(Pageable pageable) {
        return entityManager.createQuery("select o.name from OrganisationType o", String.class)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    @Override
    public Long existsOrganisationType(String name) {
        return jdbcTemplate.queryForObject("select COALESCE(MAX (id), -1) from organisation_type where name = ?  ",Long.class,name);
    }

    @Override
    @Transactional
    public void updateOrganisationType(String oldName, String newName) {
        if(existsOrganisationType(oldName) == -1L){
            throw new EmptyResultDataAccessException("No organisation type " + oldName + "exists",1);
        }
        if(existsOrganisationType(newName) != -1L){
            throw new DataIntegrityViolationException("Organisation type with name " + newName + " already exists");
        }

        entityManager.createQuery("update OrganisationType ot set ot.name = :newName where ot.name = :oldName")
                .setParameter("newName", newName)
                .setParameter("oldName", oldName)
                .executeUpdate();
    }

    @Override
    @Transactional
    public void deleteOrganisationType(String name) {
        if (existsOrganisationType(name) == -1){
            throw new EmptyResultDataAccessException("No usertype " + name + "exists",1);
        }

        entityManager.createQuery("DELETE FROM OrganisationType ot WHERE ot.name = :name")
                .setParameter("name", name)
                .executeUpdate();
    }

    @Override
    @Transactional
    public void save(Organisation organisation) {
        organisation.setUser(entityManager.getReference(User.class,organisation.getUser().getId()));
        organisation.setType(entityManager.getReference(OrganisationType.class,organisation.getType().getId()));


        if(organisation.getMedia() != null && !organisation.getMedia().isEmpty()){
            List<Media> mediaList = organisation.getMedia().stream()
                    .map(media -> {
                        if (media.getId() != null) {
                            return entityManager.getReference(Media.class, media.getId());
                        }
                        return media;
                    })
                    .collect(Collectors.toList());
            organisation.setMedia(mediaList);
        }
        if(organisation.getParentOrganisation() != null && organisation.getParentOrganisation().getId() != null)
            organisation.setParentOrganisation(entityManager.getReference(Organisation.class, organisation.getParentOrganisation().getId()));
        else {
            organisation.setParentOrganisation(null);
        }

        entityManager.persist(organisation);
    }

//    @Override
//    public OrganisationBaseDto getOrganisation(String username) {
//        if(existOrganisation(username) == -1L){
//            throw new EmptyResultDataAccessException("No organisation type " + username + "exists",1);
//        }
//        else {
//            return entityManager.createQuery(
//                    "select new in.ac.iitj.instiapp.payload.User.Organisation.OrganisationBaseDto( o.user.userName, " +
//                            "case when o.parentOrganisation is null then null else o.parentOrganisation.user.userName end, o.type.name, o.Description, o.Website) from Organisation o left join o.parentOrganisation left join  o.parentOrganisation.user where o.user.userName = :username", OrganisationBaseDto.class)
//                    .setParameter("username", username)
//                    .getSingleResult();
//
//        }
//    }

    @Override
    public OrganisationBaseDto getOrganisation(String username) {
        try {
            return entityManager.createQuery(
                            "select new in.ac.iitj.instiapp.payload.User.Organisation.OrganisationBaseDto(" +
                                    "  o.user.userName, " +
                                    "  case when po is null then null else pu.userName end, " +
                                    "  o.type.name, " +
                                    "  o.Description, " +
                                    "  o.Website" +
                                    ") " +
                                    "from Organisation o " +
                                    "  left join o.parentOrganisation po " +
                                    "  left join po.user pu " +
                                    "where o.user.userName = :username",
                            OrganisationBaseDto.class
                    )
                    .setParameter("username", username)
                    .getSingleResult();
        }
        catch (NoResultException ex) {
            throw new EmptyResultDataAccessException(
                    "No organisation found for username " + username, 1);
        }
    }


    @Override
    public List<OrganisationBaseDto> getOrganisationByType(OrganisationType organisationType, Pageable pageable) {
        return entityManager.createQuery("select new in.ac.iitj.instiapp.payload.User.Organisation.OrganisationBaseDto(o.user.userName,o.parentOrganisation.user.userName,o.type.name,o.Description,o.Website) from Organisation o left join o.parentOrganisation left join o.parentOrganisation.user where o.type.name = :typeName",OrganisationBaseDto.class)
                .setParameter("typeName",organisationType.getName())
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    @Override
    public OrganisationDetailedDto organisationDetailed(String username) {

        System.out.println("▶ Checking if organisation exists for username: '" + username + "'");
        Long organisationId = existOrganisation(username);
        if(organisationId == -1L){
            throw new EmptyResultDataAccessException("No organisation with name " + username + " exists",1);
        }

        Object[] result = entityManager.createQuery(
                        "select o.user.userName, " +
                                "CASE when o.parentOrganisation is not null then o.parentOrganisation.user.userName else NULL end, " +
                                "o.type.name, " +
                                "o.Description, " +
                                "o.Website " +
                                "from Organisation o " +
                                "left join o.parentOrganisation " +
                                "left join o.parentOrganisation.user " +
                                "where o.id = :id",
                        Object[].class
                )
                .setParameter("id", organisationId)
                .getSingleResult();

        List<String> mediaPublicIds = entityManager.createQuery("select   m.publicId " + "from Organisation o left join o.media m where o.id = :id", String.class).setParameter("id", organisationId).getResultList();

        OrganisationDetailedDto organisationDetailedDto = new OrganisationDetailedDto(
                (String) result[0],              // username
                (String) result[1],              // parentOrganisationUserName
                (String) result[2],              // organisationTypeName
                (String) result[3],              // Description
                mediaPublicIds,                  // mediaPublicIds
                (String) result[4]               // Website
        );


        if(organisationDetailedDto.getParentOrganisation().getUser().getUserName() != null){
            System.out.println("Hello world" + organisationDetailedDto.getParentOrganisation().getUser().getUserName());
            organisationDetailedDto.setParentOrganisation(getOrganisation(organisationDetailedDto.getParentOrganisation().getUser().getUserName()));
        } else
            organisationDetailedDto.setParentOrganisation(null);

        return organisationDetailedDto;
    }

    @Override
    public Long existOrganisation(String username) {
        return jdbcTemplate.queryForObject(
                "select coalesce(max(o.id), -1::bigint) " +
                        "from organisation o join users u on u.id = o.user_id " +
                        "where u.user_name = ?",
                Long.class,
                username
        );
    }



    @Override
    public Optional<List<String>> updateOrganisation(Organisation organisation) {

        List<String> oldMediaPublicIds = new ArrayList<>();
        try{
            oldMediaPublicIds = entityManager.createQuery("select m.publicId from Organisation o join o.media m where o.id = :id", String.class).setParameter("id", organisation.getId()).getResultList();
        } catch(NoResultException ignored){}

        List<Long> newMediaIds = Optional.ofNullable(organisation.getMedia())
                .map(mediaList -> mediaList.stream()
                        .map(Media::getId)
                        .collect(Collectors.toList()))
                .orElse(null);

        // Update the organisation_media join table
        if (newMediaIds != null) {
            // First delete existing relationships
            jdbcTemplate.update(
                    "delete from organisation_media where organisation_id = ?",
                    organisation.getId()
            );

            // Then insert new relationships
            for (Long mediaId : newMediaIds) {
                jdbcTemplate.update(
                        "insert into organisation_media (organisation_id, media_id) values (?, ?)",
                        organisation.getId(),
                        mediaId
                );
            }
        }

        jdbcTemplate.update("UPDATE organisation SET " +
                        "parent_organisation_id = COALESCE(?, parent_organisation_id), " +
                        "type_id = COALESCE(?, type_id), " +
                        "description = COALESCE(?, description), " +
                        "website = COALESCE(?, website) " +
                        "WHERE id = ?",

                Optional.ofNullable(organisation.getParentOrganisation())
                        .map(Organisation::getId).orElse(null),
                Optional.ofNullable(organisation.getType())
                        .map(OrganisationType::getId).orElse(null),

                organisation.getDescription(),
                organisation.getWebsite(),
                organisation.getId()
        );

        return Optional.of(oldMediaPublicIds);

    }

    public Organisation getOrganisationByUsername(String username){
        return entityManager.createQuery("SELECT o FROM Organisation o WHERE o.user.userName = :username",Organisation.class)
                .setParameter("username",username)
                .getSingleResult();
    }

    @Override
    public String deleteOrganisation(String username) {
        // TODO
        return "";
    }
}
