package in.ac.iitj.instiapp.payload.User.Faculty;

import in.ac.iitj.instiapp.database.entities.User.Faculty.Faculty;
import in.ac.iitj.instiapp.payload.User.Organisation.OrganisationBaseDto;
import in.ac.iitj.instiapp.payload.User.UserBaseDto;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link Faculty}
 */
@Value
public class FacultyBaseDto implements Serializable {
    UserBaseDto user;
    OrganisationBaseDto organisation;
    String description;
    String websiteUrl;

    public FacultyBaseDto(String username, String organisationUsername, String description, String websiteUrl) {
        this.user = new UserBaseDto(username);
        this.organisation = new OrganisationBaseDto(organisationUsername);
        this.description = description;
        this.websiteUrl = websiteUrl;
    }
}