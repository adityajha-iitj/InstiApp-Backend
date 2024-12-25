package in.ac.iitj.instiapp.payload.User.Student;

import in.ac.iitj.instiapp.database.entities.User.Student.Student.Student;
import in.ac.iitj.instiapp.payload.User.Organisation.OrganisationBaseDto;
import in.ac.iitj.instiapp.payload.User.UserDetailedDto;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link Student}
 */
@Value
public class StudentDetailedDto implements Serializable {
    UserDetailedDto user;
    String programName;
    StudentBranchDto studentBranch;
    Integer admissionYear;
}