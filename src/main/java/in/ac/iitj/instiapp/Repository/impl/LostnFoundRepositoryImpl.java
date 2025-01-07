package in.ac.iitj.instiapp.Repository.impl;

import in.ac.iitj.instiapp.database.entities.LostnFound.Locations;
import in.ac.iitj.instiapp.payload.LostnFound.LostnFoundDto;
import in.ac.iitj.instiapp.payload.User.Student.StudentBaseDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import in.ac.iitj.instiapp.database.entities.LostnFound.LostnFound;
import in.ac.iitj.instiapp.database.entities.User.User;
import in.ac.iitj.instiapp.database.entities.Media.Media;

import java.util.List;
import java.util.Optional;

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
    public Long existLocation(String locationName) {
        return jdbcTemplate.queryForObject("select coalesce(max(id), -1) from locations where name = ?", Long.class, locationName);
    }

    @Override
    public void saveLocation(Locations locations) {
        if(existLocation(locations.getName()) != -1L){
            throw new DataIntegrityViolationException("location already exists with name " + locations.getName());
        }

        this.entityManager.persist(locations);

    }

    @Override
    public List<String> getListOfLocationsName(Pageable pageable) {
        return entityManager.createQuery("select lo.name from Locations lo",String.class)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    @Override
    public void updateLocation(String oldLocationName, Locations locations) {
        if(existLocation(oldLocationName) == -1L){
            throw new EmptyResultDataAccessException("location already exists with name " + oldLocationName,1);
        }
        else if(existLocation(locations.getName()) != -1L){
            throw new DataIntegrityViolationException("location already exists with name " + locations.getName());
        }
        else{
            String sql = "update locations set locationName = ? where locationName = ?";
            jdbcTemplate.update(sql, locations.getName(), oldLocationName);
        }

    }

    @Override
    public void deleteLocationByName(String locationName) {
        String sql = "delete from locations where locationName = ?";
        jdbcTemplate.update(sql, locationName);
    }

    @Override
    public Long exsitLostnFound(String publicId) {
        return jdbcTemplate.queryForObject("select coalesce(max(id), -1) from lostnfound where publicId= ?", Long.class, publicId);
    }

    @Override
    public void saveLostnFoundDetails(LostnFound lostnFound) {

        User finder = null;
        if (lostnFound.getFinder() != null && lostnFound.getFinder().getId() != -1) {
            finder = entityManager.getReference(User.class, lostnFound.getFinder().getId());
        }
        User owner = null;
        if (lostnFound.getOwner() != null && lostnFound.getOwner().getId() != -1) {
            owner = entityManager.getReference(User.class, lostnFound.getOwner().getId());
        }
        Locations landmark = null;
        if (lostnFound.getLandmark() != null && lostnFound.getLandmark().getId() != -1) {
            landmark = entityManager.getReference(Locations.class, lostnFound.getLandmark().getId());
        }
        Media media = null;
        if (lostnFound.getMedia() != null && lostnFound.getMedia().getId() != -1) {
            media = entityManager.getReference(Media.class, lostnFound.getMedia().getId());
        }

        if (finder != null) {
            lostnFound.setFinder(finder);
        }
        if (owner != null) {
            lostnFound.setOwner(owner);
        }
        if (landmark != null) {
            lostnFound.setLandmark(landmark);
        }
        if (media != null) {
            lostnFound.setMedia(media);
        }

        entityManager.persist(lostnFound);
    }

    @Override
    public List<LostnFoundDto> getLostnFoundByFilter(Optional<Boolean> status, Optional<String> owner, Optional<String> finder, Optional<String> landmark, Pageable pageable) {
        return  entityManager.createQuery("select new in.ac.iitj.instiapp.payload.LostnFound.LostnFoundDto(l.publicId , l.finder , l.owner , l.Lankmark , l.extraInfo , l.status , l.media ) from LostnFound l where " +
                        "(:status is NULL or l.status = :status) and " +
                        "(:owner is NULL or l.owner = :owner) and " +
                        "(:finder is NULL or l.finder = :finder) and "+
                        "(:landmark is NULL or l.landmark = :landmark)", LostnFoundDto .class)
                .setParameter("status",status.orElse(null))
                .setParameter("owner",owner.orElse(null))
                .setParameter("finder",finder.orElse(null))
                .setParameter("landmark" , landmark.orElse(null))
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    @Override
    public void updateLostnFound(LostnFound lostnFound, String publicId) {
        if(exsitLostnFound(publicId) == -1L){
            throw new EmptyResultDataAccessException("the lost and found with public id " + publicId +"does not exists" , 1);
        }
        else {
            jdbcTemplate.update("update  lostnfound set " +
                            "finder = case when ? is null then finder else ? end," +
                            "owner = case when ? is null then owner else ? end, " +
                            "Landmark = case  when ? is null then Landmark else ? end," +
                            "extraInfo = case when ? is null then extraInfo else ? end " +
                            "status = case when ? is null then status else ? end" +
                            "media = case when ? is null then media else ? end" +
                            " where publicId = ?",
                    lostnFound.getFinder().getId(), lostnFound.getOwner().getId(),
                    lostnFound.getLandmark().getId(), lostnFound.getExtraInfo(),
                    lostnFound.getStatus(), lostnFound.getMedia().getId(), publicId);
        }
    }

    @Override
    public Optional<String> deleteLostnFound(String publicId) {
        String sql = """
            SELECT m.publicId
            FROM media m
            JOIN lostnfound l ON m.id = l.media_id
            WHERE l.publicId = ?
        """;
        String mediaPublicId = jdbcTemplate.queryForObject(sql, String.class, publicId);

        String deleteSql = """
                DELETE FROM lostnfound
                WHERE publicId = ?
            """;
        jdbcTemplate.update(deleteSql, publicId);

        return Optional.ofNullable(mediaPublicId);

    }


}




