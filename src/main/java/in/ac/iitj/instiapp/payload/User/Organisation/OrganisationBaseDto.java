package in.ac.iitj.instiapp.payload.User.Organisation;

import com.fasterxml.jackson.annotation.JsonProperty;
import in.ac.iitj.instiapp.payload.User.UserBaseDto;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link in.ac.iitj.instiapp.database.entities.User.Organisation.Organisation}
 */
@Value
public class OrganisationBaseDto implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    UserBaseDto user;
    String parentOrganisationUserUserName;
    String typeName;
    String Description;
    String Website;
}