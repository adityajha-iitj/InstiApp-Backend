package in.ac.iitj.instiapp.Repository.User.Organisation;

import in.ac.iitj.instiapp.database.entities.User.Organisation.OrganisationRole;
import in.ac.iitj.instiapp.payload.User.Organisation.OrganisationRoleDto;
import in.ac.iitj.instiapp.payload.User.UserBaseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface OrganisationRoleRepository {


    /**
     * @param organisationRole organisationId cannot be null
     */
    void saveOrganisationRole(OrganisationRole organisationRole);


    List<OrganisationRoleDto> getOrganisationRoles(String organisationName, Pageable pageable);


    Long existOrganisationRole(String organisationName, String roleName);


    /**
     * Note Length of both lists should be same
     * @param organisationName
     * @param organisationRoleName
     * @return
     */
    List<Long> getOrganisationRoleIds(List<String> organisationName, List<String> organisationRoleName, Pageable pageable);


    /**
     * Only changes the roleName and rolePermission
     * @param oldOrganisationRole OrganisationId cannot be null
     *                         Organisation name cannot be null
     *                         roleName cannot be null
     *                            permission can be null
     * @throws org.springframework.dao.EmptyResultDataAccessException the role doesn't exist
     * @throws org.springframework.dao.DataIntegrityViolationException if the newRole already exist with the same name and permission in the organisation
     */
    void updateOrganisationRole(OrganisationRole oldOrganisationRole,OrganisationRole newOrganisationRole);


    /**
     * Removes all the persons from the organisationRole
     * @assumptions organisation name exist
     * @param organisationRoleName
     * @param OrganisationName
     * @param deletedRoleName the roleName to which all the data points after its deleted from database
     *                        All organisation should have it
     */
    void deleteOrganisationRole(String organisationRoleName, String OrganisationName,String deletedRoleName);


    // ---------------------------CRUD OPERations on joint table---------------------------------
    /**
     * @param idOfPerson
     * @throws org.springframework.dao.DataIntegrityViolationException If the user already exists on any other position of organisation
     */
    void insertIntoOrganisationRole(String organisationUsername, String organisationRoleName,Long idOfPerson);






    /**
     * @assumptions Organisation exist
     * @param usernameOfOrganisation
     * @param pageable
     * @return UserBaseDto only username filled
     *                     OrganisationRoleDto fully filled
     */
    List<Map<UserBaseDto, OrganisationRoleDto>> getAllOrganisationRoles(String usernameOfOrganisation,Pageable pageable);


    /**
     * @assumptions Organisation Exists
     *              Person Exists
     * @param organisationId
     * @param organisationRoleName
     * @param idOfPerson
     */
    void removePersonFromOrganisationRole(Long organisationId, String organisationRoleName,Long idOfPerson);

}
