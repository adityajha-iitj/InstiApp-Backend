package in.ac.iitj.instiapp.Repository;

import in.ac.iitj.instiapp.database.entities.User.Usertype;
import org.hibernate.usertype.UserType;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserTypeRepository {



    void save(Usertype userType);



    void update(String oldName, Usertype userType);

    void delete(String userTypeName);

    boolean exists(String name);

    List<String> getAllUserTypes(Pageable pageable);
}
