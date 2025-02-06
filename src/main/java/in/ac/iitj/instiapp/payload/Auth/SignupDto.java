package in.ac.iitj.instiapp.payload.Auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link in.ac.iitj.instiapp.database.entities.User.User}
 */
@Value
public class SignupDto implements Serializable {


    String phoneNumber;

    @NotBlank
    String userTypeName;
}