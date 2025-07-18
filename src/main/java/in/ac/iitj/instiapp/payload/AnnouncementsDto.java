package in.ac.iitj.instiapp.payload;

import lombok.*;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * DTO for {@link in.ac.iitj.instiapp.database.entities.Announcements}
 */
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementsDto implements Serializable {
    Long publicId;
    String organisationUserUserName;
    String title;
    String description;
    Date dateOfAnnouncement;
    Time timeOfAccouncement;
    List<String> mediaPublicUrls;
}