package in.ac.iitj.instiapp.Repository.User.Organisation;

import in.ac.iitj.instiapp.database.entities.User.Organisation.Organisation;
import in.ac.iitj.instiapp.database.entities.User.Organisation.OrganisationType;
import in.ac.iitj.instiapp.payload.User.Organisation.OrganisationBaseDto;
import in.ac.iitj.instiapp.payload.User.Organisation.OrganisationDetailedDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface OrganisationRepository {
//    void save(Organisation organisation, long userid , long parentOrganisationId, long  organisationType, Long mediaId );
//    boolean organisationExists(String username);
//    Organisation findByUsername(String username);
//    List<Organisation> findAll();
//    List<Organisation> findByOrganisationType(OrganisationType organisationType);
//    void updateDescription(String username, String description);
//    void updateWebsite(String username, String website);


 // TODO - METHODS For organisation TYPE

    /**
     * @param organisationType
     * @throws org.springframework.dao.DataIntegrityViolationException if OrganisationType with same Name exists
     */
void saveOrganisationType(OrganisationType organisationType);


List<String> getAllOrganisationTypes(Pageable pageable);


    /**
     * @param name
     * @return -1 if the name doesn't exist else Long ID
     */
Long existsOrganisationType(String name);


    /**
     * @param oldName
     * @param newName
     * @throws org.springframework.dao.EmptyResultDataAccessException if oldName doesn't Exist
     * @throws org.springframework.dao.DataIntegrityViolationException if newName already exists in Database
     */
void updateOrganisationType(String oldName, String newName);

    /**
     * @param name
     * @throws org.springframework.dao.EmptyResultDataAccessException if the organisationType doesn't exist
     */
void deleteOrganisationType(String name);



//------------Methods for Organisation




    /**
     * @assumptions All the objects are there even if their ids are null
     * @param organisation userId shoudn't be null
     *                     If want to add organisation it's id shoudn't be null
     *                     Organisation Type Id shoudn't be null
     *                     Media Id can be null
     *                     ParentOrganisationId can be null but it shouldn't be null
     * @throws org.springframework.dao.DataIntegrityViolationException If username with organisation already exist
     */
    void save(Organisation organisation);


    /**
     * @param username
     * @return OrganisationBaseDto - Only username filled in UsernameBaseDto
     * @throws org.springframework.dao.EmptyResultDataAccessException If username doesn't exist with the organisation
     */
    OrganisationBaseDto getOrganisation(String username);


    List<OrganisationBaseDto> getOrganisationByType(OrganisationType organisationType, Pageable pageable);

    /**
     * @param username
     * @return OrganisationDetailedDto - Only username filled in UserDetailedDto
     *                                   If parent Organisation exist full OrganisationBaseDto filled except the userBaseDto
     *                                   If Media exist then only media publicId filled else id is null
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
     * @assumptions Organisation's User's username is taken from oauth and doesn't depend on user
     * @implNote If Object not null put checks in query for update
     * @param organisation Organisation Id shouldn't be null
     *                     If parent organisation to be updated Its id shouldn't be null else Id should be null but object should exist
     *                     If organisationType should be updated Its Id shouldn't be null else Id should be null but object should exist
     *                     If Description to be updated it shouldn't be null else null
     *                     If Media to be updated Media Id should exist else mediaId should be null but object should exist
     *                     If website to be updated it shouldn't be null else null
     *
     * @return oldMediaPublicId
     */
    Optional<List<String>> updateOrganisation(Organisation organisation);


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
