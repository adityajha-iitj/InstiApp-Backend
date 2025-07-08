package in.ac.iitj.instiapp.payload.User.Organisation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddUserToRoleRequest {

    @NotNull(message = "Organisation username cannot be null")
    @NotBlank(message = "Organisation username cannot be blank")
    private String organisationUsername;

    @NotNull(message = "Role name cannot be null")
    @NotBlank(message = "Role name cannot be blank")
    private String roleName;

    @NotNull(message = "User username cannot be null")
    @NotBlank(message = "User username cannot be blank")
        private String userUsername;
}
