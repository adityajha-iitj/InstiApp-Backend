package in.ac.iitj.instiapp.payload.User;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import in.ac.iitj.instiapp.database.entities.User.Organisation.OrganisationRole;
import in.ac.iitj.instiapp.payload.Views;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.util.Map;

/**
 * DTO for {@link in.ac.iitj.instiapp.database.entities.User.User}
 */
@Value
@AllArgsConstructor
public class UserBaseDto implements Serializable {

    String name;

    String userName;

    String email;

    String userTypeName;

    String avatarUrl;

    public UserBaseDto(String userName) {
        this.userName = userName;
        this.name = null;
        this.email = null;
        this.avatarUrl = null;
        this.userTypeName = null;
    }

}
