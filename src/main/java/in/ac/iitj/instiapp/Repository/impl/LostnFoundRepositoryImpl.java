package in.ac.iitj.instiapp.Repository.impl;

import in.ac.iitj.instiapp.database.entities.LostnFound.Locations;
import in.ac.iitj.instiapp.payload.LostnFound.LostnFoundDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import in.ac.iitj.instiapp.database.entities.LostnFound.LostnFound;
import in.ac.iitj.instiapp.database.entities.User.User;
import in.ac.iitj.instiapp.database.entities.Media.Media;

import jakarta.transaction.Transactional;

import java.util.List;

@Repository
public class LostnFoundRepositoryImpl implements in.ac.iitj.instiapp.Repository.LostnFoundRepository {

    private final JdbcTemplate jdbcTemplate;
    private final EntityManager entityManager;


    @Autowired
    public LostnFoundRepositoryImpl(JdbcTemplate jdbcTemplate, EntityManager entityManager) {
        this.jdbcTemplate = jdbcTemplate;
        this.entityManager = entityManager;
    }

    @Override
    public List<LostnFoundDto> getListOfLocationsName(Pageable pageable) {
        Query query = entityManager.createQuery(
                "SELECT new in.ac.iitj.instiapp.payload.LostnFound.LostnFoundDto( " +
                        "    null, null, null, null, " +       // Finder details as null
                        "    null, null, null, null, " +       // Owner details as null
                        "    t.Landmark.name, null, null, null " + // Only populate landmarkName
                        ") FROM LostnFound t",
                LostnFoundDto.class
        );
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        return query.getResultList();
    }

    @Transactional
    @Override
    public void saveLocation(Locations locations) {
        entityManager.persist(locations);
    }

    @Override
    public void deleteLocationByName(String locationName) {
        jdbcTemplate.update("delete from locations where name = ?", locationName);
    }


    @Override
    public List<LostnFoundDto> getLostnFoundByStatus(Boolean status, Pageable pageable) {
        Query query = entityManager.createQuery(
                "SELECT new in.ac.iitj.instiapp.payload.LostnFound.LostnFoundDto( " +
                        "    t.finder.name, t.finder.userName, t.finder.email, t.finder.phoneNumber, " +
                        "    t.owner.name, t.owner.userName, t.owner.email, t.owner.phoneNumber, " +
                        "    t.Landmark.name, t.extraInfo, t.status, t.media.publicUrl " +  // Fill only relevant fields
                        ") FROM LostnFound t " +
                        "WHERE t.status = :status",
                LostnFoundDto.class
        );
        query.setParameter("status", status);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        return query.getResultList();
    }

    @Override
    public List<LostnFoundDto> getLostnFoundWithDetails(Pageable pageable) {
        Query query = entityManager.createQuery(
                "SELECT new in.ac.iitj.instiapp.payload.LostnFound.LostnFoundDto( " +
                        "    t.finder.name, t.finder.userName, t.finder.email, t.finder.phoneNumber, " +
                        "    t.owner.name, t.owner.userName, t.owner.email, t.owner.phoneNumber, " +
                        "    t.Landmark.name, t.extraInfo, t.status, t.media.publicUrl " +  // Fill all fields
                        ") FROM LostnFound t",
                LostnFoundDto.class
        );
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        return query.getResultList();
    }

    @Override
    public List<LostnFoundDto> getLostnFoundByLandmark(String landmarkName, Pageable pageable) {
        Query query = entityManager.createQuery(
                "SELECT new in.ac.iitj.instiapp.payload.LostnFound.LostnFoundDto( " +
                        "    t.finder.name, t.finder.userName, t.finder.email, t.finder.phoneNumber, " +
                        "    t.owner.name, t.owner.userName, t.owner.email, t.owner.phoneNumber, " +
                        "    t.Landmark.name, t.extraInfo, t.status, t.media.publicUrl " +  // Fill all fields
                        ") FROM LostnFound t " +
                        "WHERE t.Landmark.name = :landmarkName",
                LostnFoundDto.class
        );
        query.setParameter("landmarkName", landmarkName);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        return query.getResultList();
    }

    @Transactional
    @Override
    public void saveLostnFoundDetails(LostnFoundDto lostnFoundDto) {
        // Fetch related entities (User, Location, Media) based on the DTO data
        User finder = entityManager.find(User.class, lostnFoundDto.getFinderUserName());
        User owner = entityManager.find(User.class, lostnFoundDto.getOwnerUserName());
        Locations landmark = entityManager.find(Locations.class, lostnFoundDto.getLandmarkName());
        Media media = entityManager.find(Media.class, lostnFoundDto.getMediaPublicUrl());

        // Create a new LostnFound entity with the details from the DTO
        LostnFound lostnFound = new LostnFound();
        lostnFound.setFinder(finder);
        lostnFound.setOwner(owner);
        lostnFound.setLandmark(landmark);
        lostnFound.setExtraInfo(lostnFoundDto.getExtraInfo());
        lostnFound.setStatus(false); // Default status is false (not found)
        lostnFound.setMedia(media);

        // Persist the entity
        entityManager.persist(lostnFound);
    }





}




