package in.ac.iitj.instiapp.Repository;

import in.ac.iitj.instiapp.database.entities.User.Organisation.Organisation;
import in.ac.iitj.instiapp.database.entities.User.Organisation.OrganisationType;
import in.ac.iitj.instiapp.database.entities.User.User;

import java.util.List;

public interface OrganisationRepository {
    void save(Organisation organisation, long userid , long parentOrganisationId, long  organisationType, Long mediaId );
    boolean organisationExists(String username);
    Organisation findByUsername(String username);
    List<Organisation> findAll();
    List<Organisation> findByOrganisationType(OrganisationType organisationType);
    void updateDescription(String username, String description);
    void updateWebsite(String username, String website);
}
