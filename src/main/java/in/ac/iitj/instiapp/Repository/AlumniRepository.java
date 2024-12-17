package in.ac.iitj.instiapp.Repository;

import in.ac.iitj.instiapp.database.entities.User.Alumni.Alumni;
import in.ac.iitj.instiapp.database.entities.User.Student.Student;
import in.ac.iitj.instiapp.database.entities.User.Student.StudentBranch;
import in.ac.iitj.instiapp.database.entities.User.Student.StudentProgram;
import in.ac.iitj.instiapp.database.entities.User.User;

public interface AlumniRepository {

    void save(User user, Alumni alumni, StudentBranch studentBranch , StudentProgram studentProgram , int admissionYear, int passOutYear);
    Alumni getAlumni(String username);
    void updateAlumni(Alumni alumni);
    void deleteAlumni(Alumni alumni);
    boolean alumniExists(String username);
}
