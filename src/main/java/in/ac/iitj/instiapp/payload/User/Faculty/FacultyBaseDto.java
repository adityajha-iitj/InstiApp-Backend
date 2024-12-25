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
}