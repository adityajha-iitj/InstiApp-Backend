package in.ac.iitj.instiapp.services;

import in.ac.iitj.instiapp.database.entities.User.Student.Student.Student;
import in.ac.iitj.instiapp.database.entities.User.Student.StudentBranch;
import in.ac.iitj.instiapp.payload.User.Student.StudentBaseDto;
import in.ac.iitj.instiapp.payload.User.Student.StudentBranchDto;
import in.ac.iitj.instiapp.payload.User.Student.StudentDetailedDto;
import org. springframework. data. domain. Pageable;
import java.util.List;
import java.util.Optional;

public interface StudentService {


/*---------------------------------------------STUDENT PROGRAM--------------------------------------------------------*/
    /**
     * @param program
     * @param isActive
     * @implNote  no need of dto as only two properties exists
     */
    void saveProgram(String program , Boolean isActive);

    /**
     * @param program
     * @return returns id of that program if exists or else return -1L
     */
    Long existStudentProgram(String program);

    /**
     * @param pageable
     * @param all true if it has to return all the active and inactive programs
     * @return List of string of all the programs that are available
     */
    List<String> getListOfStudentPrograms(Pageable pageable , Boolean all);

    /**
     * @param oldProgram
     * @param newProgram
     * @param isActive
     * @implNote no need to take dto from controller as only 2 fields are in program table
     */
    void updateStudentProgram( String oldProgram,String newProgram , Boolean isActive);

    /**
     * @param program
     */
    void deleteStudentProgram(String program);
    //TODO

/*----------------------------------------------STUDENT BRANCH--------------------------------------------------------*/

    /**
     * @param studentBranchDto
     * @assumptions organisation_id is not null and opening closing year check is done in controller
     * @implNote have to convert the dto to entity in order to pass it to the repository layer
     */
    void saveStudentBranch(StudentBranchDto studentBranchDto);

    /**
     * @param pageable
     * @return returns the list of all the student branch
     */
    List<StudentBranchDto> getListofStudentBranch(Pageable pageable);

    /**
     * @param branch
     * @return returns the dto for that student branch name
     */
    StudentBranchDto getStudentBranch(String branch);

    /**
     * @param branch
     * @param studentBranchDto
     * @implNote have to convert the dto to  the entity
     */
    void updateStudentBranch(String branch ,StudentBranchDto studentBranchDto);

    /**
     * @param branch
     * @implNote cautious of cascading
     */
    void deleteStudentBranch(String branch);
        //TODO

/*-------------------------------------------------STUDENT------------------------------------------------------------*/
    /**@assumptions username already exist
     *              StudentBranch alrady exist
     *              StudentProgram already exists
     *              admission year check is done
     * @implNote StudentDetailedDto should be converted to the entity
     * @param studentDto username already exists
     *                     branchId not null
     *                     program id not null
     */
    void saveStudent(StudentDetailedDto studentDto);

    /**
     * @param username
     * @return returns the studentBaseDto
     * @implNote have to set BranchDto and userDetailedDto
     */
    StudentBaseDto getStudent(String username);

    /**
     * @param programName can be null
     * @param branchName can be null
     * @param admissionYear can be null
     * @param pageable
     * @return returns the list of StudentBaseDto based on the filter
     * @implNote have to set BranchDto and userDetailedDto
     */
    List<StudentBaseDto> getStudentByFilter(Optional<String> programName , Optional<String> branchName ,Optional<Integer> admissionYear , Pageable pageable );

    /**
     * @param username
     * @return returns id of the student if it exists otherwise -1L
     */
    Long existStudent(String username);

    /**
     * @param username
     * @return StudentDetailedDto
     */
    StudentDetailedDto getDetailedStudent(String username);

    /**
     * @param username
     * @param studentDetailedDto
     * @implNote need to change the dto to entity
     */
    void updateStudent(String username , StudentDetailedDto studentDetailedDto);

    /**
     * @param username
     * @return id of the user associated with it
     * @implNote also have to delete the corresponding user
     */
    Long deleteStudent(String username);
            //TODO
}
