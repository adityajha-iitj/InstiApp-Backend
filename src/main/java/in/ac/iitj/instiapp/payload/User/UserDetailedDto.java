package in.ac.iitj.instiapp.payload.User;

import com.fasterxml.jackson.annotation.JsonView;
import in.ac.iitj.instiapp.database.entities.User.Organisation.OrganisationPermission;
import in.ac.iitj.instiapp.payload.User.Organisation.OrganisationBaseDto;
import in.ac.iitj.instiapp.payload.User.Organisation.OrganisationRoleDto;
import in.ac.iitj.instiapp.payload.Views;
import lombok.Value;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link in.ac.iitj.instiapp.database.entities.User.User}
 */
@Value
public class UserDetailedDto implements Serializable {
    String name;
    String userName;
    String email;


    // Public Constructor
    public UserDetailedDto(String name, String userName, String email, String userTypeName, String avatarUrl, Set<OrganisationRoleDto> organisationRoleSet) {
        this.name = name;
        this.userName = userName;
        this.email = email;
        this.userTypeName = userTypeName;
        this.avatarUrl = avatarUrl;
        this.organisationRoleSet = organisationRoleSet;
        this.phoneNumber = null;
        this.calendarPublicId = null;
    }

    // Private Constructor
    public UserDetailedDto(String name, String userName, String email, String phoneNumber, String userTypeName, String calendarPublicId, String avatarUrl, Set<OrganisationRoleDto> organisationRoleSet) {
        this.name = name;
        this.userName = userName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userTypeName = userTypeName;
        this.calendarPublicId = calendarPublicId;
        this.avatarUrl = avatarUrl;
        this.organisationRoleSet = organisationRoleSet;
    }

    public UserDetailedDto(String username) {
        this.userName = username;
        this.name = null;
        this.email = null;
        this.userTypeName = null;
        this.avatarUrl = null;
        this.organisationRoleSet = null;
        this.phoneNumber = null;
        this.calendarPublicId = null;
    }

    @JsonView(Views.Private.class)
    String phoneNumber;
    String userTypeName;

    @JsonView(Views.Private.class)
    String calendarPublicId;

    String avatarUrl;
    Set<OrganisationRoleDto> organisationRoleSet;






}