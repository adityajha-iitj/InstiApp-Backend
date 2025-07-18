package in.ac.iitj.instiapp.payload.LostnFound;

import in.ac.iitj.instiapp.database.entities.LostnFound.LostnFoundType;
import in.ac.iitj.instiapp.payload.Media.MediaBaseDto;
import in.ac.iitj.instiapp.payload.User.UserBaseDto;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;


/* DTO for {@link in.ac.iitj.instiapp.database.entities.LostnFound.LostnFound} */

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LostnFoundDto implements Serializable {
    Long id;
    UserBaseDto finder;
    UserBaseDto owner;
    String landmarkName;
    LostnFoundType type;
    String extraInfo;
    Boolean status;
    MediaBaseDto media;
    LocalDateTime time;

public LostnFoundDto(Long id, String finder_username , String owner_username , String landmark_name,LostnFoundType type, String extra_info, Boolean status, String media_publicUrl) {
    this.id = id;
    this.finder = new UserBaseDto(finder_username);
    this.owner = new UserBaseDto(owner_username);
    this.landmarkName = landmark_name;
    this.type = type;
    this.extraInfo = extra_info;
    this.status = status;
    this.media = new MediaBaseDto(media_publicUrl);

}
}
