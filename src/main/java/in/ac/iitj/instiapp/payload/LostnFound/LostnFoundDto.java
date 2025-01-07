package in.ac.iitj.instiapp.payload.LostnFound;

import in.ac.iitj.instiapp.payload.Media.MediaBaseDto;
import in.ac.iitj.instiapp.payload.User.UserBaseDto;
import lombok.Value;

import java.io.Serializable;


/* DTO for {@link in.ac.iitj.instiapp.database.entities.LostnFound.LostnFound} */

@Value
public class LostnFoundDto implements Serializable {
    String publicId;
    UserBaseDto finder;
    UserBaseDto owner;
    String LandmarkName;
    String extraInfo;
    Boolean status;
    MediaBaseDto media;

public LostnFoundDto(String publicId , String finder_username , String owner_username , String landmark_name, String extra_info, Boolean status, String media_publicId) {
    this.publicId = publicId;
    this.finder = new UserBaseDto(finder_username);
    this.owner = new UserBaseDto(owner_username);
    this.LandmarkName = landmark_name;
    this.extraInfo = extra_info;
    this.status = status;
    this.media = new MediaBaseDto(media_publicId);

}
}
