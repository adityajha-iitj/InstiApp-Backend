package in.ac.iitj.instiapp.payload.User.WellBeingModerator;

import in.ac.iitj.instiapp.database.entities.User.Wellbeingmoderator.WellBeingMember;
import in.ac.iitj.instiapp.payload.User.UserBaseDto;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link WellBeingMember}
 */
@Value
@AllArgsConstructor
public class WellBeingMemberDtoLimited implements Serializable {
    UserBaseDto user;
    String qualification;
    String availability;


    public WellBeingMemberDtoLimited(String username, String qualification, String availability) {
        this.user = new UserBaseDto(username);
        this.qualification = qualification;
        this.availability = availability;
    }
}

