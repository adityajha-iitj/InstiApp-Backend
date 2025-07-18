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
@NoArgsConstructor
public class MediaBaseDto implements Serializable {

    String publicUrl;

    public MediaBaseDto(String publicUrl){
        this.publicUrl = publicUrl;
    }

}