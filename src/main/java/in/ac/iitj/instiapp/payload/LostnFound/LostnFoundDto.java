package in.ac.iitj.instiapp.payload.LostnFound;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link in.ac.iitj.instiapp.database.entities.LostnFound.LostnFound}
 */

@Value
public class LostnFoundDto implements Serializable {
    String finderName;
    String finderUserName;
    String finderEmail;
    String finderPhoneNumber;
    String ownerName;
    String ownerUserName;
    String ownerEmail;
    String ownerPhoneNumber;
    String LandmarkName;
    String extraInfo;
    Boolean status;
    String mediaPublicUrl;
}