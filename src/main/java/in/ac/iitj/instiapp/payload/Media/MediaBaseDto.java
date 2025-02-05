package in.ac.iitj.instiapp.payload.Media;

import com.fasterxml.jackson.annotation.JsonView;
import in.ac.iitj.instiapp.database.entities.Media.Mediatype;
import in.ac.iitj.instiapp.payload.Views;
import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link in.ac.iitj.instiapp.database.entities.Media.Media}
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MediaBaseDto implements Serializable {

    Mediatype type;

    String publicId;

    String publicUrl;

    public MediaBaseDto(String publicId){
        this.type = null;
        this.publicId = publicId;
        this.publicUrl = null;
    }

}