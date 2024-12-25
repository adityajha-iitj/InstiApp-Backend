package in.ac.iitj.instiapp.payload.User.Student;

import in.ac.iitj.instiapp.database.entities.User.Student.Student.Student;
import in.ac.iitj.instiapp.payload.User.UserBaseDto;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link Student}
 */
@Value
public class StudentBaseDto implements Serializable {
    UserBaseDto user;
    String programName;
    StudentBranchDto studentBranch;
    Integer admissionYear;
}