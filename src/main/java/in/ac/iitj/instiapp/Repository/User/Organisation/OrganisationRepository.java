package in.ac.iitj.instiapp.Repository.User.Organisation;

import in.ac.iitj.instiapp.database.entities.User.Organisation.Organisation;
import in.ac.iitj.instiapp.database.entities.User.Organisation.OrganisationType;
import in.ac.iitj.instiapp.payload.User.Organisation.OrganisationBaseDto;
import in.ac.iitj.instiapp.payload.User.Organisation.OrganisationDetailedDto;

import java.util.List;

public interface OrganisationRepository {
    void save(Organisation organisation, long userid , long parentOrganisationId, long  organisationType, Long mediaId );
    boolean organisationExists(String username);
    Organisation findByUsername(String username);
    List<Organisation> findAll();
    List<Organisation> findByOrganisationType(OrganisationType organisationType);
    void updateDescription(String username, String description);
    void updateWebsite(String username, String website);


 // TODO - METHODS For organisation TYPE



//------------Methods for Organisation




    /**
     * @assumptions All the objects are there even if their ids are null
     * @param organisation userId shoudn't be null
     *                     If want to add organisation it's id shoudn't be null
     *                     Organisation Type Id shoudn't be null
     *                     Media Id can be null
     * @throws org.springframework.dao.DataIntegrityViolationException If username with organisation already exist
     */
    void save(Organisation organisation);


    /**
     * @param username
     * @return OrganisationBaseDto - Only username filled in UsernameBaseDto
     * @throws org.springframework.dao.EmptyResultDataAccessException If username doesn't exist with the organisation
     */
    OrganisationBaseDto getOrganisation(String username);


    List<OrganisationBaseDto> getOrganisationByType(OrganisationType organisationType);

    /**
     * @param username
     * @return OrganisationDetailedDto - Only username filled in UserDetailedDto
     *                                   If parent Organisation exist full OrganisationBaseDto filled else null
     *                                   If Media exist then full MediaBaseDto else null
     *                                   usersWithPors not filled and is nulled.Get it filled from OrganisationRoleDto
     * @throws org.springframework.dao.EmptyResultDataAccessException if username doesn't exist with organisation
     */
    OrganisationDetailedDto organisationDetailed(String username);


    /**
     * @param username
     * @return -1 If organisation doesn't exist
     */
    Long existOrganisation(String username);


    /**
     *
     * @assumptions If parentOrganisationId not null its value exist in database
     *              Organisation's User's username is taken from oauth and doesn't depend on user
     * @implNote If Object not null put checks in query for update
     * @param organisation UserId shouldn't be null
     *                     If parent organisation to be updated Its id shouldn't be null else Id should be null but object should exist
     *                     If organisationType should be updated Its Id shouldn't be null else Id should be null but object should exist
     *                     If Description to be updated it shouldn't be null else null
     *                     If Media to be updated Media Id should exist else mediaId should be null but object should exist
     *                     If website to be updated it shouldn't be null else null
     *
     * @return oldMediaId
     */
    String updateOrganisation( Organisation organisation);


    /**
     * Cascading effect removes all persons from role
     *
     * @assumptions All the organisations roles are pointed to the nullified role
     *              Removed from announcements the organisation
     *              Set to nullified organisation in griveance
     * @param username
     * @return Media PublicId which was removed
     * @throws org.springframework.dao.EmptyResultDataAccessException If organisation with the username doesn't exist
     */
    String deleteOrganisation(String username);













}
