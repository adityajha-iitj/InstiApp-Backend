package in.ac.iitj.instiapp.payload.User.Organisation;

import com.fasterxml.jackson.annotation.JsonView;
import in.ac.iitj.instiapp.database.entities.User.Organisation.OrganisationPermission;
import in.ac.iitj.instiapp.payload.Views;
import lombok.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

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
    
    @NotNull(message = "Role name is required")
    @Size(min = 1, max = 100, message = "Role name must be between 1 and 100 characters")
    String roleName;
    
    @NotNull(message = "Permission is required")
    OrganisationPermission permission;
}

