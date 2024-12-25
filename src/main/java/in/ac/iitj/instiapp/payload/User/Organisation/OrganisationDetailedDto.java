package in.ac.iitj.instiapp.payload.User.Organisation;

import in.ac.iitj.instiapp.database.entities.User.Organisation.Organisation;
import in.ac.iitj.instiapp.payload.Media.MediaBaseDto;
import in.ac.iitj.instiapp.payload.User.UserBaseDto;
import in.ac.iitj.instiapp.payload.User.UserDetailedDto;
import lombok.Value;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * DTO for {@link Organisation}
 */
@Value
public class OrganisationDetailedDto implements Serializable {
    UserDetailedDto user;
    OrganisationBaseDto parentOrganisation;
    String typeName;
    String Description;
    MediaBaseDto media;
    String Website;


    List<Map<UserBaseDto,OrganisationRoleDto>>  usersWithPORs;
}