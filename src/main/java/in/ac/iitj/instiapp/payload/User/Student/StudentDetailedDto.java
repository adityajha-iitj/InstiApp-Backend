package in.ac.iitj.instiapp.payload.User.Student;

import in.ac.iitj.instiapp.database.entities.User.Student.Student.Student;
import in.ac.iitj.instiapp.payload.User.Organisation.OrganisationBaseDto;
import in.ac.iitj.instiapp.payload.User.UserDetailedDto;
import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link Student}
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentDetailedDto implements Serializable {
    UserDetailedDto user;
    String programName;
    StudentBranchDto studentBranch;
    Integer admissionYear;



    public StudentDetailedDto(String username, String programName, String studentBranchName, Integer admissionYear) {
        this.user = new UserDetailedDto(username);
        this.programName = programName;
        this.studentBranch = new StudentBranchDto(studentBranchName);
        this.admissionYear = admissionYear;
    }
}