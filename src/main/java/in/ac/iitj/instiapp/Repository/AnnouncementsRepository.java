package in.ac.iitj.instiapp.Repository;

import in.ac.iitj.instiapp.database.entities.Announcements;
import in.ac.iitj.instiapp.database.entities.Scheduling.Calendar.Events;
import in.ac.iitj.instiapp.payload.AnnouncementsDto;
import org.springframework.data.domain.Pageable;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Repository interface for Announcements-related operations.
 */
@Repository
public interface AnnouncementsRepository extends JpaRepository<Announcements, Integer> {

    @Query("SELECT a FROM Announcements a WHERE a.publicId = :publicId")
    public Announcements findByAnnouncementByPublicId(Long publicId);

    @Query("SELECT m.publicUrl FROM Announcements a JOIN a.media m WHERE a.publicId = :publicId")
    public List<String> getAnnouncementMediaUrl(Long publicId);



}
