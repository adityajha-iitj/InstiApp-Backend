package in.ac.iitj.instiapp.payload.User;

import in.ac.iitj.instiapp.database.entities.User.Group;
import in.ac.iitj.instiapp.database.entities.User.Organisation.Organisation;
import lombok.Value;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DTO for {@link Group}
 */
@Value
public class GroupDto implements Serializable {
    String name;

    String publicId;

    List<String> userNames;

    List<String> branchNames;

    List<String> programNames;

    List<Organisation> organisationRoles;

    List<String> userTypeNames;

    Map<String, Map<String, String>> userTypeAttributes;


    public GroupDto(String publicId){
        this.name = null;
        this.publicId = publicId;
        this.userNames = null;
        this.branchNames = null;
        this.programNames = null;
        this.organisationRoles = null;
        this.userTypeNames = null;
        this.userTypeAttributes = null;
    }


}