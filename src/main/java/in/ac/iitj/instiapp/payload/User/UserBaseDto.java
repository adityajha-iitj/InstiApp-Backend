package in.ac.iitj.instiapp.payload.User;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import in.ac.iitj.instiapp.database.entities.User.Organisation.OrganisationRole;
import in.ac.iitj.instiapp.payload.Views;
import lombok.*;

import java.io.Serializable;
import java.util.Map;

/**
 * DTO for {@link in.ac.iitj.instiapp.database.entities.User.User}
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserBaseDto implements Serializable {

    String name;

    String userName;

    String password;

    String email;

    String userTypeName;

    String avatarUrl;

    public UserBaseDto(String userName) {
        this.userName = userName;
        this.name = null;
        this.password = null;
        this.email = null;
        this.avatarUrl = null;
        this.userTypeName = null;
    }

}
