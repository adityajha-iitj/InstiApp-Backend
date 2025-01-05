package in.ac.iitj.instiapp.payload.User.Organisation;

import in.ac.iitj.instiapp.database.entities.User.Organisation.Organisation;
import in.ac.iitj.instiapp.payload.Media.MediaBaseDto;
import in.ac.iitj.instiapp.payload.User.UserBaseDto;
import in.ac.iitj.instiapp.payload.User.UserDetailedDto;
import lombok.Data;
import lombok.Setter;
import lombok.Value;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * DTO for {@link Organisation}
 */
@Data
@Setter
public class OrganisationDetailedDto implements Serializable {
    UserDetailedDto user;
    OrganisationBaseDto parentOrganisation;
    String typeName;
    String Description;
    MediaBaseDto media;
    String Website;


    List<Map<UserBaseDto,OrganisationRoleDto>>  usersWithPORs;



    public OrganisationDetailedDto(String username,String parentOrganisationUserName, String organisationTypeName,
                                   String Description, String mediaPublicId, String website) {
        this.user = new UserDetailedDto(username);
        this.parentOrganisation = new OrganisationBaseDto(parentOrganisationUserName);
        this.typeName = organisationTypeName;
        this.Description = Description;
        this.media = new MediaBaseDto(mediaPublicId);
        this.Website = website;
        this.usersWithPORs = new ArrayList<>();
    }
}