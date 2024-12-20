package in.ac.iitj.instiapp.payload.User.WellBeingModerator;

import in.ac.iitj.instiapp.database.entities.User.Wellbeingmoderator.WellBeingMember;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link WellBeingMember}
 */
@Value
public class WellBeingMemberDtoFull implements Serializable {
    String userName;
    String userUserName;
    String userEmail;
    String userPhoneNumber;
    String userCalendarPublic_id;
    String userAvatarPublicId;
    String qualification;
    String availability;
}