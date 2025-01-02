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
     * @param announcementsDto the DTO containing the details of the announcement to be created.
     */
    void createAnnouncement(AnnouncementsDto announcementsDto);

    /**
     * Edits an existing announcement in the database.
     *
     * @param announcementsDto the DTO containing the updated details of the announcement.
     * @param announcementId   the ID of the announcement to be edited.
     */
    void editAnnouncement(Long announcementId, AnnouncementsDto announcementsDto);

    /**
     * Deletes an announcement from the database.
     *
     * @param announcementId the ID of the announcement to be deleted.
     */
    void deleteAnnouncement(Long announcementId);
    /**
     * Gets all the latest versions of the announcemnts
     */
    List<AnnouncementsDto> getAllAnnouncements(Pageable pageable);
}
