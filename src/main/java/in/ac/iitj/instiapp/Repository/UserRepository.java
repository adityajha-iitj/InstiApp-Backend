package in.ac.iitj.instiapp.Repository;

import in.ac.iitj.instiapp.database.entities.User.User;
import in.ac.iitj.instiapp.database.entities.User.Usertype;
import in.ac.iitj.instiapp.payload.User.Organisation.OrganisationRoleDto;
import in.ac.iitj.instiapp.payload.User.UserBaseDto;
import in.ac.iitj.instiapp.payload.User.UserDetailedDto;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository {


//    void save(User user, Long AvatarId, Long CalendarId,String MediaTypeName);
//
//    boolean existsByEmail(String email);
//    boolean existsByUsername(String username);
//    boolean existsByPhoneNumber(String phoneNumber);
//    boolean deleteByUsername(String username);
//    boolean userExists(String username);
//    void updateUserType(String username , String newusertype);
//    long getUserId(String username);



    // CRUD Operations for -------------------------------------- USERTYPE -----------------------------

    /**
     * @param userType
     * @throws org.springframework.dao.DataIntegrityViolationException If UserType with the same Name exists
     */
    void save(Usertype userType);


    /**
     */
    List<String> getAllUserTypes(Pageable pageable);



    /**
     * @param name
     * @return -1 If the name doesn't exist else the Long ID
     */
    Long userTypeExists(String name);



    /**
     * @param oldName
     * @param newName
     * @throws org.springframework.dao.EmptyResultDataAccessException If OldName doesn't exists
     * @throws org.springframework.dao.DataIntegrityViolationException If newName already exists in Database
     */
    void update(String oldName,  String newName);


    /**
     * @param userTypeName
     * @throws org.springframework.dao.EmptyResultDataAccessException if the UserType doesn't Exist
     */
    void delete(String userTypeName);




    // CRUD Operations for  --------------------------------------- User -------------------------------------------------------

    /**
     * @param user Username should be set and shouldn't be null <br>
     *             CalendarId should not be null <br>
     *             Set<\OrganisationRole> could be empty list but shouldn't be null
     * @assumptions userType already exist.Only Id will be used.So No use of Name;
     */
    Long save(User user);


    /**
     * @param username
     * @return UserBaseDTO with all properties filled up
     * @throws org.springframework.dao.EmptyResultDataAccessException If username doesn't exist
     */
    UserBaseDto getUserLimited(String username);


    /**
     * @param username
     * @param isPrivate Should Private properties be filled.
     * @assumptions The detailed DTO has 2 constructors one for public and one for private
     * @throws org.springframework.dao.EmptyResultDataAccessException if username doesn't exist
     */
    UserDetailedDto getUserDetailed(String username, boolean isPrivate);


    /**
     * Should be used with caution as it may introduce long database calls
     * @param usertype
     * @param pageable
     * @return List of UsersBaseDto with a specific call
     */
    List<UserBaseDto> getListUserLimitedByUsertype(String usertype,Pageable pageable);


    /**
     * @param username
     * @param organisationUsername
     * @throws org.springframework.dao.EmptyResultDataAccessException If either of them doesn't exist
     * @return if the user is not part of the organisation return Optional.Empty()
     */
    Optional<OrganisationRoleDto> getOrganisationPermission(String username, String organisationUsername);


    /**
     * to be used in combination with getUserDetailedDto
     *
     * @param username
     * @return Empty list if user is not in any organisation
     * @throws org.springframework.dao.EmptyResultDataAccessException if username doesn't exist
     */
    Set<OrganisationRoleDto> getOrganisationRoleDTOsByUsername(String username, Pageable pageable);


    /**
     * @return -1 if it doesn't exists
     */
    Long usernameExists(String username);

    /**
     * @apiNote The api using this functions if refreshes on userInteraction should have strict rateLimits To prevent exceeding quotas
     * for oauth2
     * @throws 
     */
    void updateOauth2Info(String newName, String avatarURL, String userName);


    /**
     * @param newUserType -
     * @throws org.springframework.dao.EmptyResultDataAccessException if  newUserType or username doesn't exist
     */
    void setUserType(String username, String newUserType);


    /**
     * @apiNote should be checked by OTP before entering in database
     * @param username
     * @param newPhoneNumber
     * @throws org.springframework.dao.EmptyResultDataAccessException if username doesn't exist in database
     */
    void updatePhoneNumber(String username, String newPhoneNumber);

}
