package in.ac.iitj.instiapp.Repository.User.Student;

import in.ac.iitj.instiapp.database.entities.User.Student.StudentProgram;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StudentProgramRepository {



    // CRUD Operations for StudentProgram

    /**
     * Before inserting check without case sensitivity
     * @param studentProgram
     * @throws org.springframework.dao.DataIntegrityViolationException if it exists
     */
    void save(StudentProgram studentProgram);

    /**
     * @param name
     * @return -1 if it doesn't exist else Id
     */
    Long existsStudentProgram(String name);


    /**
     * Can be used with all parameters in different cases for alumni and student
     * @param pageable
     * @param all if false only Returns active ones else all
     * @return Returns Names of StudentPrograms
     */
    List<String> getListOfStudentPrograms(Pageable pageable, boolean all);

    /**
     * @param oldName
     * @param newName
     * @implNote throw DataIntegrity if newName != oldName and already exist
     * @throws org.springframework.dao.EmptyResultDataAccessException if oldName doesn't exist
     * @throws org.springframework.dao.DataIntegrityViolationException if newName already exist
     */
    void updateStudentProgram(String oldName, String newName, boolean isActive);


    /**
     * USE With caution as it might have cascading effects and may introduce database blocks
     * @assumptions First delete all the students with specific program including there users
     * @param name
     * @throws  org.springframework.dao.EmptyResultDataAccessException if the name doesn't exist in student program
     */
    void  deleteStudentProgram(String name);
}
