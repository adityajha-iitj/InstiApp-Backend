package in.ac.iitj.instiapp.payload.User.Alumni;

import com.fasterxml.jackson.annotation.JsonView;
import in.ac.iitj.instiapp.database.entities.User.Student.Alumni.Alumni;
import in.ac.iitj.instiapp.payload.User.Organisation.OrganisationBaseDto;
import in.ac.iitj.instiapp.payload.User.Student.StudentBranchDto;
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
    StudentBranchDto studentBranchDto;
    Integer admissionYear;
    Integer passOutYear;


    public AlumniDetailedDto(String username, String programName, String branchName, Integer admissionYear,Integer passOutYear ){
        this.programName = programName;
        this.studentBranchDto = new StudentBranchDto(branchName);
        this.admissionYear = admissionYear;
        this.passOutYear = passOutYear;
        this.user = new UserDetailedDto(username);
    }
}