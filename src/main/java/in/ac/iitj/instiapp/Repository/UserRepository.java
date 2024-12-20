package in.ac.iitj.instiapp.Repository;

import in.ac.iitj.instiapp.database.entities.User.User;
import in.ac.iitj.instiapp.database.entities.User.Usertype;

public interface UserRepository {


    void save(User user, Long AvatarId, Long CalendarId,String MediaTypeName);

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean deleteByUsername(String username);
    boolean userExists(String username);
    void updatePhoneNumber(String phoneNumber, String username);
    void updateUserType(String username , String newusertype);
    long getUserId(String username);



}
