package in.ac.iitj.instiapp.Repository;

import in.ac.iitj.instiapp.database.entities.User.Student.Student;
import in.ac.iitj.instiapp.database.entities.User.Student.StudentBranch;
import in.ac.iitj.instiapp.database.entities.User.Student.StudentProgram;
import in.ac.iitj.instiapp.database.entities.User.User;

public interface StudentRepository {

    void save(User user,Student student, StudentBranch studentBranch , StudentProgram studentProgram , int admission);
    Student getStudent(String username);
    void updateStudent(Student student);
    void deleteStudent(Student student);
    boolean StudentExists(String username);
    void updateStudentBranch(StudentBranch studentBranch , String username);



}
