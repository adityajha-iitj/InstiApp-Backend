package in.ac.iitj.instiapp.payload.LostnFound;

import in.ac.iitj.instiapp.payload.Media.MediaBaseDto;
import in.ac.iitj.instiapp.payload.User.UserBaseDto;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link in.ac.iitj.instiapp.database.entities.LostnFound.LostnFound}
 */
@Value
public class LostnFoundDto implements Serializable {
    String publicId;
    UserBaseDto finder;
    UserBaseDto owner;
    String LandmarkName;
    String extraInfo;
    Boolean status;
    MediaBaseDto media;
}