package in.ac.iitj.instiapp.Repository;

import in.ac.iitj.instiapp.database.entities.User.Permissions;
import in.ac.iitj.instiapp.database.entities.User.PermissionsData;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PermissionsRepository {

    void savePermission(in.ac.iitj.instiapp.database.entities.User.Permissions permissions);

    List<in.ac.iitj.instiapp.database.entities.User.Permissions> getPermissions(Pageable pageable);

    void updatePermission(in.ac.iitj.instiapp.database.entities.User.Permissions oldPermission, in.ac.iitj.instiapp.database.entities.User.Permissions newPermission);

    Long existsPermission(PermissionsData permissions);

    void deletePermission(Permissions permissions);
}
