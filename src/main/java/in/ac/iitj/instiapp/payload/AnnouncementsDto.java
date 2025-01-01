package in.ac.iitj.instiapp.payload;

import in.ac.iitj.instiapp.database.entities.Media.Mediatype;
import lombok.Value;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * DTO for {@link in.ac.iitj.instiapp.database.entities.Announcements}
 */
@Value
public class AnnouncementsDto implements Serializable {
    String userName;
    String userUserName;
    String userEmail;
    String userAvatarUrl;
    String Title;
    String Description;
    Date dateOfAnnouncement;
    Set<MediaDto> media;
    Set<String> groupsListNames;
    Set<UserDto> users;

    /**
     * DTO for the Media that will be displayed in Announcements
     */
    @Value
    public static class MediaDto implements Serializable {
        Mediatype type;
        String publicId;
        String assetId;
        String publicUrl;
    }

    /**
     * DTO for the users that the announcements will the displayed
     */
    @Value
    public static class UserDto implements Serializable {
        String userName;
        String avatarUrl;
    }
}