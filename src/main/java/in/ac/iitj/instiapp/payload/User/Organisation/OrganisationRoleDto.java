package in.ac.iitj.instiapp.payload.User.Organisation;

import com.fasterxml.jackson.annotation.JsonView;
import in.ac.iitj.instiapp.database.entities.User.Organisation.OrganisationPermission;
import in.ac.iitj.instiapp.payload.Views;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link in.ac.iitj.instiapp.database.entities.User.Organisation.OrganisationRole}
 */
@Value
public class OrganisationRoleDto implements Serializable {

    String organisationUsername;
    String roleName;
    OrganisationPermission permission;

    public OrganisationRoleDto(String organisationUsername, String roleName, OrganisationPermission permission) {
        this.organisationUsername = organisationUsername;
        this.roleName = roleName;
        this.permission = permission;
    }
}

