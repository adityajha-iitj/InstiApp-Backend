package in.ac.iitj.instiapp.services;

import in.ac.iitj.instiapp.database.entities.User.User;
import in.ac.iitj.instiapp.database.entities.User.Usertype;
import in.ac.iitj.instiapp.payload.Auth.SignupDto;
import in.ac.iitj.instiapp.payload.User.Organisation.OrganisationRoleDto;
import in.ac.iitj.instiapp.payload.User.UserBaseDto;
import in.ac.iitj.instiapp.payload.User.UserDetailedDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;



public interface UserService {


    public Long save(@Valid SignupDto signupDto);
    public Long updateUserDetails(UserDetailedDto userDetailedDto);

    public String createUsername(String firstName, String lastName, String email);

    public void save(Usertype usertype);

    public List<String> getAllUserTypes(Pageable pageable);

    public Long userTypeExists(String name);

    public void userTypeUpdate(String oldName, String newName);

    public void userTypeDelete(String userTypeName);

    public UserBaseDto getUserLimited(String username);

    public UserDetailedDto getUserDetailed(String username, boolean isPrivate, Pageable pageable);


    public UserDetailedDto getUserDetailed(String username);

    public List<UserBaseDto> getListUserLimitedByUsertype(String usertype, Pageable pageable);

    public Optional<OrganisationRoleDto> getOrganisationPermission(String username, String organisationUsername);

    public Set<OrganisationRoleDto> getOrganisationRoleDTOsByUsername(String username, Pageable pageable);

    public Long usernameExists(String username);

    public Long emailExists(String email);

    public void updateOauth2Info(String newName, String avatarURL, String userName);

    public void setUserType(String username, String newUserType);

    public void updatePhoneNumber(String username, String newPhoneNumber);

    public void delete(String userTypeName);

    public String getUsernameFromEmail(String email);

    User findOrCreateGoogleUser(String googleId, String email, String name, String pictureUrl);
}
