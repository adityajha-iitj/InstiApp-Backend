package in.ac.iitj.instiapp.payload.Auth;

import in.ac.iitj.instiapp.database.entities.User.Usertype;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link in.ac.iitj.instiapp.database.entities.User.User}
 */
@Value
@Getter
@Setter
@AllArgsConstructor
public class SignupDto {

    @NotBlank
    private String name;

    @Email
    private String email;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String avatarUrl;

    @NotBlank
    private Usertype userType;
}
