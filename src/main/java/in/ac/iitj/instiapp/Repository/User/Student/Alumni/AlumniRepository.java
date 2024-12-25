package in.ac.iitj.instiapp.Repository.User.Student.Alumni;

import in.ac.iitj.instiapp.database.entities.User.Student.Alumni.Alumni;
import in.ac.iitj.instiapp.database.entities.User.Student.StudentBranch;
import in.ac.iitj.instiapp.database.entities.User.Student.StudentProgram;
import in.ac.iitj.instiapp.database.entities.User.User;
import in.ac.iitj.instiapp.payload.User.Alumni.AlumniBaseDto;
import in.ac.iitj.instiapp.payload.User.Alumni.AlumniDetailedDto;
import org.springframework.data.domain.Pageable;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface AlumniRepository {

//    void save(User user, Alumni alumni, StudentBranch studentBranch , StudentProgram studentProgram , int admissionYear, int passOutYear);
//    Alumni getAlumni(String username);
//    void updateAlumni(Alumni alumni);
//    void deleteAlumni(Alumni alumni);
//    boolean alumniExists(String username);


    /**
     * @assumptions username already exist
     *              StudentBranch already exist
     *              StudentProgram already exist
     *              admissionYear and passoutYear are checked;
     *
     * @implNote could use getReference from BranchId and ProgramId
     * @param alumni username could be null and but userId exist
     *               Branch's Id could not be null
     *               Program's Id could not be null
     */
    void save(Alumni alumni);


    /**
     * @param username
     * @return AlumniBaseDto only name filled in branch.Get Branch at upper level
     *                       Only name filled in Program
     * @throws org.springframework.dao.EmptyResultDataAccessException if username doesn't exist or isn't alumni
     */
    AlumniBaseDto getAlumni(String username);

    /**
     * @param username
     * @return AlumniDetailedDto only name filled in branchDTO
     *                           only name filled in program
     *         Username filled in only AlumniDetailedDto
     * @throws org.springframework.dao.EmptyResultDataAccessException if username doesn't exist
     */
    AlumniDetailedDto getDetailedAlumni(String username);


    /**
     *
     * The OptionalValues shouldn't be null it should be always Optional.empty()
     */
    List<AlumniBaseDto> getAlumniByFilter(Optional<String> programName, Optional<String> branchNameName,
                                          Optional<Integer> admissionYear, Optional<Integer> passOutYear, Pageable pageable);


    /**
     * Doesn't update userdetails
     * @assumptions admissionYear and passOutYear validity checked
     *              StudentProgram Existence checked
     *              StudentBranch existence Checked
     * @param alumni StudentProgram only newId needed <br>
     *               StudentBranch only new Id needed <br>
     *               Doesn't update User Details <br>
     *               userId shouldn't be null <br>
     */
    void updateAlumni(Alumni alumni);


    /**
     * After deleting this delete the user.For DataIntegrity
     * @param username
     * @return the id of the user where student is deleted
     */
    Long deleteAlumni(String username);

}
