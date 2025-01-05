package in.ac.iitj.instiapp.payload.User.Alumni;

import com.fasterxml.jackson.annotation.JsonProperty;
import in.ac.iitj.instiapp.database.entities.User.Student.Alumni.Alumni;
import in.ac.iitj.instiapp.payload.User.Organisation.OrganisationBaseDto;
import in.ac.iitj.instiapp.payload.User.Student.StudentBranchDto;
import in.ac.iitj.instiapp.payload.User.UserBaseDto;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link Alumni}
 */
@Value
public class AlumniBaseDto implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    UserBaseDto user;

    String programName;
    StudentBranchDto studentBranchDto;
    Integer admissionYear;



    public AlumniBaseDto(String username, String programName, String branchName, Integer admissionYear) {
        this.user = new UserBaseDto(username);
        this.programName = programName;
        this.studentBranchDto = new StudentBranchDto(branchName);
        this.admissionYear = admissionYear;
    }
}