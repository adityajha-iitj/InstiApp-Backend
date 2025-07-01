package in.ac.iitj.instiapp.payload.User.Organisation;

import com.fasterxml.jackson.annotation.JsonView;
import in.ac.iitj.instiapp.database.entities.User.Organisation.OrganisationPermission;
import in.ac.iitj.instiapp.payload.Views;
import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link in.ac.iitj.instiapp.database.entities.User.Organisation.OrganisationRole}
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrganisationRoleDto implements Serializable {

    String organisationUsername;
    String roleName;
    OrganisationPermission permission;
}

