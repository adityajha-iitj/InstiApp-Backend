package in.ac.iitj.instiapp.payload.User.Faculty;

import in.ac.iitj.instiapp.database.entities.User.Faculty.Faculty;
import in.ac.iitj.instiapp.payload.User.Organisation.OrganisationBaseDto;
import in.ac.iitj.instiapp.payload.User.Organisation.OrganisationDetailedDto;
import in.ac.iitj.instiapp.payload.User.UserDetailedDto;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link Faculty}
 */
@Value
public class FacultyDetailedDto implements Serializable {
    UserDetailedDto user;
    OrganisationBaseDto organisation;
    String description;
    String websiteUrl;


    public FacultyDetailedDto(String username, String organisationUserName, String description, String websiteUrl) {
        this.user = new UserDetailedDto(username);
        this.organisation = new OrganisationBaseDto(organisationUserName);
        this.description = description;
        this.websiteUrl = websiteUrl;
    }
}