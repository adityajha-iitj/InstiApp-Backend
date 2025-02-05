package in.ac.iitj.instiapp.payload;

import in.ac.iitj.instiapp.database.entities.User.Organisation.OrganisationPermission;
import in.ac.iitj.instiapp.payload.Media.MediaBaseDto;
import in.ac.iitj.instiapp.payload.User.Organisation.OrganisationRoleDto;
import in.ac.iitj.instiapp.payload.User.UserBaseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link in.ac.iitj.instiapp.database.entities.Grievance}
 */
@Getter
@Setter
public class GrievanceDto implements Serializable {
    String publicId;
    String Title;
    String Description;
    UserBaseDto userFrom;
    OrganisationRoleDto organisationRole;
    Boolean resolved;
    MediaBaseDto media;

    public GrievanceDto(String publicId, String title, String description, String username, String orgUsername, String roleName, OrganisationPermission permission, Boolean resolved, String mediaPublicId) {
        this.publicId = publicId;
        this.Title = title;
        this.Description = description;
        this.userFrom = new UserBaseDto(username);
        this.organisationRole = new OrganisationRoleDto(orgUsername,roleName,permission);
        this.resolved = resolved;
        this.media = new MediaBaseDto(mediaPublicId);
    }
}