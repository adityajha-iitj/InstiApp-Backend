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

import java.util.List;
import java.util.Optional;

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
    public void deleteOrganisationType(String name) {
        if (existsOrganisationType(name) == -1){
            throw new EmptyResultDataAccessException("No usertype " + name + "exists",1);
        }

        //TODO
    }

    @Override
    public void save(Organisation organisation) {
        organisation.setUser(entityManager.getReference(User.class,organisation.getUser().getId()));
        organisation.setType(entityManager.getReference(OrganisationType.class,organisation.getType().getId()));


        if(organisation.getMedia().getId() != null)
            organisation.setMedia(entityManager.getReference(Media.class,organisation.getMedia().getId()));
        if(organisation.getParentOrganisation().getId() != null)
            organisation.setParentOrganisation(entityManager.getReference(Organisation.class, organisation.getParentOrganisation().getId()));
        else {
            organisation.setParentOrganisation(null);
        }

        entityManager.persist(organisation);
    }

    @Override
    public OrganisationBaseDto getOrganisation(String username) {
        if(existOrganisation(username) == -1L){
            throw new EmptyResultDataAccessException("No organisation type " + username + "exists",1);
        }
        else {
            return entityManager.createQuery(
                    "select new in.ac.iitj.instiapp.payload.User.Organisation.OrganisationBaseDto( o.user.userName, " +
                            "case when o.parentOrganisation is null then null else o.parentOrganisation.user.userName end, o.type.name, o.Description, o.Website) from Organisation o left join o.parentOrganisation left join  o.parentOrganisation.user where o.user.userName = :username", OrganisationBaseDto.class)
                    .setParameter("username", username)
                    .getSingleResult();

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

        Long organisationId = existOrganisation(username);
        if(organisationId == -1L){
            throw new EmptyResultDataAccessException("No organisation with name " + username + " exists",1);
        }


        OrganisationDetailedDto organisationDetailedDto =  entityManager.createQuery("select new in.ac.iitj.instiapp.payload.User.Organisation.OrganisationDetailedDto(o.user.userName," +
                "CASE when o.parentOrganisation is not null then  o.parentOrganisation.user.userName else NUll end," +
                "o.type.name,o.Description," +
                "CASE when o.media is NOT NULL then o.media.publicId else null end," +
                "o.Website ) from Organisation o left join o.parentOrganisation left join o.media left join o.parentOrganisation.user where o.id = :id",OrganisationDetailedDto.class)
                .setParameter("id",organisationId)
                .getSingleResult();


        if(organisationDetailedDto.getParentOrganisation().getUser().getUserName() != null){
            System.out.println("Hello world" + organisationDetailedDto.getParentOrganisation().getUser().getUserName());
            organisationDetailedDto.setParentOrganisation(getOrganisation(organisationDetailedDto.getParentOrganisation().getUser().getUserName()));
        }
            return organisationDetailedDto;
    }

    @Override
    public Long existOrganisation(String username) {
        return jdbcTemplate.queryForObject("select coalesce(max(o.id), -1::bigint) from organisation o join users u on u.id = o.user_id where u.user_name = ?", Long.class, username);
    }

    @Override
    public Optional<String> updateOrganisation(Organisation organisation) {




            String oldMediaPublicId = null;
            try {
                oldMediaPublicId = entityManager.createQuery("select o.media.publicId from Organisation  o where o.id = :id",String.class)
                        .setParameter("id",organisation.getId()).getSingleResult();
            }catch (NoResultException ignored){}

            jdbcTemplate.update("update  organisation set " +
                    "parent_organisation_id = case when ? is null then parent_organisation_id else ? end," +
                    "media_id = case when ? is null then media_id else ? end, " +
                    "type_id = case  when ? is null then type_id  else ? end ," +
                    "description = case  when ? is null then description else ? end," +
                    "website = case when ? is null then website else ? end where id = ?",

                    Optional.ofNullable(organisation.getParentOrganisation())
                            .map(Organisation::getId).orElse(null),
                    Optional.ofNullable(organisation.getParentOrganisation())
                            .map(Organisation::getId).orElse(null),

                    Optional.ofNullable(organisation.getMedia())
                            .map(Media::getId).orElse(null),
                    Optional.ofNullable(organisation.getMedia())
                            .map(Media::getId).orElse(null),

                    Optional.ofNullable(organisation.getType())
                            .map(OrganisationType::getId).orElse(null),
                    Optional.ofNullable(organisation.getType())
                            .map(OrganisationType::getId).orElse(null),

                    organisation.getDescription(),
                    organisation.getDescription(),

                    organisation.getWebsite(),
                    organisation.getWebsite(),


                    organisation.getId()
                    );

            return Optional.ofNullable(oldMediaPublicId);

    }

    @Override
    public String deleteOrganisation(String username) {
        // TODO
        return "";
    }
}
