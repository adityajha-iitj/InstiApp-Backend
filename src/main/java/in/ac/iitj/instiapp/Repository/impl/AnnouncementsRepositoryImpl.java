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



    @Override
    public void createAnnouncement(Announcements announcements) {

        entityManager.persist(announcements);
    }

    @Override
    public List<AnnouncementsDto> getByGroupIds(List<Long> groupIds) {
        return entityManager.createQuery("select new in.ac.iitj.instiapp.payload.AnnouncementsDto(" +
                "an.user.userName, " +
                "an.Title, " +
                "an.Description, " +
                "an.dateOfAnnouncement," +
                "case when anm is not null then anm.publicId else null end, " +
                "an.group.publicId," +
                "an.publicId " +
                ") from Announcements  an left join an.media anm",AnnouncementsDto.class)
                .getResultList();
    }

    @Override
    public void updateAnnouncement(String publicId, Announcements announcements) {

    }

    @Override
    public void deleteAnnouncement(String publicId) {

    }

    @Override
    public List<AnnouncementsDto> getAllAnnouncements(Pageable pageable) {
        return List.of();
    }


}
