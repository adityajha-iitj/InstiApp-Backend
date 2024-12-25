package in.ac.iitj.instiapp.Repository.impl;

import in.ac.iitj.instiapp.Repository.User.Organisation.OrganisationRepository;
import in.ac.iitj.instiapp.database.entities.Media.Media;
import in.ac.iitj.instiapp.database.entities.User.Organisation.Organisation;
import in.ac.iitj.instiapp.database.entities.User.Organisation.OrganisationType;
import in.ac.iitj.instiapp.database.entities.User.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.NoSuchElementException;

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
    public void save(Organisation organisation, long userid , long parentOrganisationId, long  organisationTypeId, Long mediaId ) {
        Organisation parentorg= entityManager.getReference(Organisation.class, parentOrganisationId);
        OrganisationType orgType  = entityManager.getReference(OrganisationType.class, organisationTypeId);
        Media media = entityManager.getReference(Media.class, mediaId);
        User user = entityManager.getReference(User.class, userid);

        organisation.setParentOrganisation(parentorg);
        organisation.setUser(user);
        organisation.setType(orgType);
        organisation.setMedia(media);

        entityManager.persist(organisation);

    }

    @Override
    public boolean organisationExists(String username) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject("SELECT EXISTS (SELECT 1 FROM organisation WHERE name = ?)", Boolean.class, username));
    }

    @Override
    public Organisation findByUsername(String username) {
        if(organisationExists(username)) {
            Query query = entityManager.createQuery("SELECT o FROM Organisation o WHERE o.name = :username", Organisation.class);
            query.setParameter(username, username);
            return (Organisation) query.getSingleResult();
        }
        else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public List<Organisation> findAll() {
        Query query = entityManager.createQuery("SELECT o FROM Organisation o");
        return query.getResultList();
    }

    @Override
    public List<Organisation> findByOrganisationType(OrganisationType organisationType) {
        Query query = entityManager.createQuery("SELECT o FROM Organisation o WHERE o.type = :organisationType");
        query.setParameter("organisationType", organisationType);
        return query.getResultList();
    }

    @Override
    public void updateDescription(String username, String description) {
        try{
            Organisation organisation = entityManager.createQuery("SELECT o FROM Organisation o WHERE o.user.userName = :username", Organisation.class).setParameter("username", username).getSingleResult();
            organisation.setDescription(description);
            entityManager.merge(organisation);
        } catch(NoSuchElementException e){
            throw new NoSuchElementException("Organisation with username '" + username + "' not found.");
        }
    }

    @Override
    public void updateWebsite(String username, String website) {
        try{
            Organisation organisation = entityManager.createQuery("SELECT o FROM Organisation o WHERE o.user.userName = :username", Organisation.class).setParameter("username", username).getSingleResult();
            organisation.setWebsite(website);
            entityManager.merge(organisation);
        } catch(NoSuchElementException e)
        {
            throw new NoSuchElementException("Organisation with username '" + username + "'not found");
        }
    }
}
