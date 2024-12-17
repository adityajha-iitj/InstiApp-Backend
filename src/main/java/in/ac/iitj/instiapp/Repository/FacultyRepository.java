package in.ac.iitj.instiapp.Repository;

import in.ac.iitj.instiapp.database.entities.User.Faculty.Faculty;
import in.ac.iitj.instiapp.database.entities.User.User;
import in.ac.iitj.instiapp.database.entities.User.Usertype;

public interface FacultyRepository {


    void save(User user, Faculty faculty);
    Faculty getFaculty(String username);
    void updateFaculty(Faculty faculty);
    void deleteFaculty(Faculty faculty);
    boolean FacultyExists(String username);



}
