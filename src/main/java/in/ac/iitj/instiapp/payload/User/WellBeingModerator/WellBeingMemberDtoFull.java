package in.ac.iitj.instiapp.payload.User.WellBeingModerator;

import in.ac.iitj.instiapp.database.entities.User.Wellbeingmoderator.WellBeingMember;
import in.ac.iitj.instiapp.payload.User.UserDetailedDto;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link WellBeingMember}
 */
@Value
public class WellBeingMemberDtoFull implements Serializable {
    UserDetailedDto user;
    String qualification;
    String availability;

    public WellBeingMemberDtoFull(String username, String qualification, String availability) {
        this.user = new UserDetailedDto(username);
        this.qualification = qualification;
        this.availability = availability;
    }


}