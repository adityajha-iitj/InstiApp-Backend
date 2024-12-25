package in.ac.iitj.instiapp.Repository.User.Student;

import in.ac.iitj.instiapp.database.entities.User.Student.StudentBranch;
import in.ac.iitj.instiapp.payload.User.Student.StudentBranchDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StudentBranchRepository {




// CRUD Operations for --------------------------------- Student Branch --------------------------


    /**
     * @implNote You need to get reference of Organisation while saving
     * @param studentBranch organisation id shouldn't be null everything else could be null
     * @assumptions openingYear and closingYear validity are already checked
     *              Existence of studentBranch Organisation checked
     *
     * @throws org.springframework.dao.DataIntegrityViolationException if StudentProgramRepository with such name already exist
     */
    void saveStudentBranch(StudentBranch studentBranch);


    /**
     * @param pageable should be valid
     * @return
     */
    List<StudentBranchDto> geListOfStudentBranch(Pageable pageable);


    /**
     * @param id
     * @return
     * @throws  org.springframework.dao.EmptyResultDataAccessException if it doesn't exist
     */
    StudentBranchDto getStudentBranch(Integer id);


    /**
     * @param name
     * @return -1 if the branch doesn't exist
     */
    Long  existsStudentBranch(String name);


    /**
     * @param name
     * @param studentBranch If organisation username is null it wouldn't be updated
     * @assumptions year validates are checked
     * @throws org.springframework.dao.EmptyResultDataAccessException if name doesn't exist
     * @throws org.springframework.dao.DataIntegrityViolationException if newBranchName is in database and not equals to current branch name
     */
    void updateStudentBranch(String name, StudentBranch studentBranch);


    /**
     * USE With caution as it might have cascading effects and may introduce database blocks
     * @assumptions First delete all the students with specific program including there users
     */
    void deleteStudentBranch(String name);


}
