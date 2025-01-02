package in.ac.iitj.instiapp.Repository.impl;

import in.ac.iitj.instiapp.Repository.AnnouncementsRepository;
import in.ac.iitj.instiapp.database.entities.Announcements;
import in.ac.iitj.instiapp.payload.AnnouncementsDto;
import in.ac.iitj.instiapp.database.entities.Media.Media;
import in.ac.iitj.instiapp.database.entities.User.User;
import in.ac.iitj.instiapp.payload.AnnouncementsDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class AnnouncementsRepositoryImpl implements in.ac.iitj.instiapp.Repository.AnnouncementsRepository {

    private final EntityManager entityManager;

    @Autowired
    public AnnouncementsRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public void createAnnouncement(AnnouncementsDto announcementsDto) {
        // Fetch related User entity based on user details
        User user = entityManager.find(User.class, announcementsDto.getUserUserName());

        // Create and set the Media entities
        Set<Media> mediaSet = new HashSet<>();
        if (announcementsDto.getMedia() != null) {
            announcementsDto.getMedia().forEach(mediaDto -> {
                Media media = entityManager.find(Media.class, mediaDto.getPublicUrl());
                mediaSet.add(media);
            });
        }

        // Create and populate the Announcements entity
        Announcements announcement = new Announcements();
        announcement.setUser(user);
        announcement.setTitle(announcementsDto.getTitle());
        announcement.setDescription(announcementsDto.getDescription());
        announcement.setDateOfAnnouncement(new Date());
        announcement.setMedia(mediaSet);

        // Persist the announcement entity
        entityManager.persist(announcement);
    }

    @Transactional
    public void editAnnouncement(Long announcementId, AnnouncementsDto announcementsDto) {
        // Fetch the existing Announcements entity
        Announcements announcement = entityManager.find(Announcements.class, announcementId);
        if (announcement == null) {
            throw new IllegalArgumentException("Announcement not found with ID: " + announcementId);
        }

        // Update fields from DTO
        announcement.setTitle(announcementsDto.getTitle());
        announcement.setDescription(announcementsDto.getDescription());
        announcement.setDateOfAnnouncement(new Date());

        // Update Media entities
        Set<Media> mediaSet = new HashSet<>();
        if (announcementsDto.getMedia() != null) {
            announcementsDto.getMedia().forEach(mediaDto -> {
                Media media = entityManager.find(Media.class, mediaDto.getPublicUrl());
                mediaSet.add(media);
            });
        }
        announcement.setMedia(mediaSet);

        // Merge updated entity
        entityManager.merge(announcement);
    }
    @Transactional
    public void deleteAnnouncement(Long announcementId) {
        // Fetch the existing Announcements entity
        Announcements announcement = entityManager.find(Announcements.class, announcementId);
        if (announcement == null) {
            throw new IllegalArgumentException("Announcement not found with ID: " + announcementId);
        }

        // Remove the entity
        entityManager.remove(announcement);
    }

    @Override
    public List<AnnouncementsDto> getAllAnnouncements(Pageable pageable) {
        Query query = entityManager.createQuery(
                "SELECT new in.ac.iitj.instiapp.payload.AnnouncementsDto( " +
                        "    a.user.userName, a.user.userName, a.user.email, a.user.avatarUrl, " +  // User details
                        "    a.Title, a.Description, a.dateOfAnnouncement, " +  // Announcement details
                        "    (SELECT m.publicUrl FROM Media m), " +  // Media details (accessing the publicUrl)
                        "    a.groupsList, " +  // Groups names
                        "    a.users " +  // Users details
                        ") FROM Announcements a",
                AnnouncementsDto.class
        );
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        return query.getResultList();
    }





}
