package in.ac.iitj.instiapp.Repository;

import in.ac.iitj.instiapp.database.entities.Announcements;
import in.ac.iitj.instiapp.payload.AnnouncementsDto;
import org.springframework.data.domain.Pageable;
import java.util.List;

import org.springframework.data.domain.Pageable;
import java.util.List;


/**
 * Repository interface for Announcements-related operations.
 */
public interface AnnouncementsRepository {

    /**
     * Creates a new announcement in the database.
     *
     * @param announcements userId shouldn't be null
     *                      groupId shouldn't be null
     *                      If Media media Id's shouldn't be null else it should be empty list
     */
    void createAnnouncement(Announcements announcements);


    List<AnnouncementsDto> getByGroupIds(List<Long> groupIds);

    /**
     * Edits an existing announcement in the database.
    */
    void updateAnnouncement(String publicId, Announcements announcements);

    /**
     * Deletes an announcement from the database.
     */
    void deleteAnnouncement(String publicId);
    /**
     * Gets all the latest versions of the announcemnts
     */
    List<AnnouncementsDto> getAllAnnouncements(Pageable pageable);
}
