package in.ac.iitj.instiapp.Repository.User.Student.Student;

import in.ac.iitj.instiapp.database.entities.User.Student.Student.Student;
import in.ac.iitj.instiapp.database.entities.User.Student.StudentBranch;
import in.ac.iitj.instiapp.payload.User.Student.StudentBaseDto;
import in.ac.iitj.instiapp.payload.User.Student.StudentDetailedDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface StudentRepository {

//    void save(User user,Student student, StudentProgramRepository studentBranch , StudentProgram studentProgram , int admission);
//    Student getStudent(String username);
//    void updateStudent(Student student);
//    void deleteStudent(Student student);
//    boolean studentExists(String username);
//    void updateStudentBranch(StudentProgramRepository studentBranch , String username);
//


// ----------------------------- CRUD operation for Student


    /**
     * @assumptions username already exist
     *              StudentBranch already exist
     *              StudentProgram already exist
     *              admissionYear are checked
     *
     * @implNote could use getReference from BranchId and Program's Id
     * @param student username could be null and but userId exist
     *                Branch's Id could not be null
     *                Program's Id could not be null
     */
    void save(Student student);


    /**
     * @param username
     * @return StudentBaseDto only name filled in branch.Get Branch at upper level
     *                        only name filled in program
     * @throws org.springframework.dao.EmptyResultDataAccessException if no student exist with the given username
     */
    StudentBaseDto getStudent(String username);


    /**
     * The OptionalValues shouldn't be null it should be always Optional.empty()
     */
    List<StudentBaseDto> getStudentByFilter(Optional<String> programName, Optional<String> branchNameName, Optional<Integer> admissionYear,Pageable pageable);

    /**
     * @param username
     * @return false if it doesn't exist
     */
    boolean existStudent(String username);

    /**
     * @param username
     * @return StudentDetailedDto only name filled in branchDTO
     *         Username filled in only UserDetailedDto
     * @throws org.springframework.dao.EmptyResultDataAccessException if username doesn't exist
     */
    StudentDetailedDto getDetailedStudent(String username);

    /**
     * @assumptions admissionYear validity checked <br>
     * @param student StudentProgram only new Id needed <br>
     *                StudentBranch only new Id needed <br>
     *                Doesn't update User Details <br>
     */
    void updateStudent(Student student);


    /**
     * After deleting this delete the user.For DataIntegrity
     * @param username
     * @returns the id of the user where student is deleted.
     */
    Long deleteStudent(String username);


}
