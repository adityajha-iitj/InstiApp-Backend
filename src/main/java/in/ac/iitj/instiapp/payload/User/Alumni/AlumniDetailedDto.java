package in.ac.iitj.instiapp.payload.User.Alumni;

import com.fasterxml.jackson.annotation.JsonView;
import in.ac.iitj.instiapp.database.entities.User.Student.Alumni.Alumni;
import in.ac.iitj.instiapp.payload.User.Organisation.OrganisationBaseDto;
import in.ac.iitj.instiapp.payload.User.UserDetailedDto;
import in.ac.iitj.instiapp.payload.Views;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link Alumni}
 */
@Value
public class AlumniDetailedDto implements Serializable {

    UserDetailedDto user;
    String programName;
    String branchName;
    OrganisationBaseDto branchOrganisation;
    Integer branchOpeningYear;
    Integer branchClosingYear;
    Integer admissionYear;


    @JsonView(Views.Private.class)
    Integer passOutYear;
}