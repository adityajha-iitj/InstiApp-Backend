package in.ac.iitj.instiapp.Repository.impl;

import in.ac.iitj.instiapp.Repository.UserRepository;
import in.ac.iitj.instiapp.database.entities.LostnFound.Locations;
import in.ac.iitj.instiapp.database.entities.LostnFound.LostnFoundType;
import in.ac.iitj.instiapp.database.entities.User.Organisation.Organisation;
import in.ac.iitj.instiapp.payload.LostnFound.LostnFoundDto;
import in.ac.iitj.instiapp.payload.User.Student.StudentBaseDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    private final UserRepository userRepository;


    @Autowired
    public LostnFoundRepositoryImpl(JdbcTemplate jdbcTemplate, EntityManager entityManager, UserRepository userRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.entityManager = entityManager;
        this.userRepository = userRepository;
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
            throw new EmptyResultDataAccessException("old location does not exist with name " + oldLocationName,1);
        }
        else if(existLocation(locations.getName()) != -1L){
            throw new DataIntegrityViolationException("location already exists with name " + locations.getName());
        }
        else{
            String sql = "update locations set name = ? where name = ?";
            jdbcTemplate.update(sql, locations.getName(), oldLocationName);
        }

    }

    @Override
    @Transactional
    public void deleteLocationByName(String locationName) {
        if(existLocation(locationName) == -1){
            throw new DataIntegrityViolationException("location doesnt exist with name " + locationName);
        }
        else {
            // step 1 :-first set the locations null in lost and found if present
            entityManager.createQuery(
                    "UPDATE LostnFound lf SET lf.Landmark = null WHERE lf.Landmark.name = :name"
            ).setParameter("name", locationName).executeUpdate();

            // Step 2: Delete the Location
            entityManager.createQuery(
                    "DELETE FROM Locations l WHERE l.name = :name"
            ).setParameter("name", locationName).executeUpdate();
        }
    }

    @Override
    public Long existLostnFound(Long id) {
        return jdbcTemplate.queryForObject("select coalesce(count(*), 0) from lostnfound where id = ?", Long.class, id);
    }

    @Override
    public void saveLostnFoundDetails(LostnFound lostnFound){
        entityManager.persist(lostnFound);
    }

    @Override
    public List<LostnFoundDto> getLostnFoundByFilter(
            LostnFoundType type,
            Optional<Boolean> status,
            Optional<String> owner,
            Optional<String> finder,
            Optional<String> landmark,
            Pageable pageable) {

        String ql =
                "select new in.ac.iitj.instiapp.payload.LostnFound.LostnFoundDto(" +
                        "l.id, " +
                        "f.userName, " +            // alias for finder
                        "o.userName, " +            // alias for owner
                        "lm.name, " +               // alias for landmark
                        "l.type, " +
                        "l.extraInfo, " +
                        "l.status, " +
                        "m.publicUrl" +              // alias for media
                        ") " +
                        "from LostnFound l " +
                        "left join l.finder f " +
                        "left join l.owner o " +
                        "left join l.Landmark lm " +   // or l.landmark if your field is lowercase
                        "left join l.media m " +
                        "where l.type = :type " +
                        "AND (:status is null or l.status = :status) " +
                        "AND (:owner is null or o.userName = :owner) " +
                        "AND (:finder is null or f.userName = :finder) " +
                        "AND (:landmark is null or lm.name = :landmark)";

        TypedQuery<LostnFoundDto> query = entityManager.createQuery(ql, LostnFoundDto.class);
        query.setParameter("type", type);
        query.setParameter("status", status.orElse(null));
        query.setParameter("owner", owner.orElse(null));
        query.setParameter("finder", finder.orElse(null));
        query.setParameter("landmark", landmark.orElse(null));

        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        return query.getResultList();
    }



    @Override
    public void updateLostnFound(LostnFound lostnFound, Long id) {
        LostnFound existing = entityManager.find(LostnFound.class, id);
        if (existing == null) {
            throw new EmptyResultDataAccessException("the lost and found with id " + id + " does not exist", 1);
        }
        jdbcTemplate.update(
                "UPDATE lostnfound SET " +
                        "finder_id = COALESCE(?, finder_id), " +
                        "owner_id = COALESCE(?, owner_id), " +
                        "landmark_id = COALESCE(?, landmark_id), " +
                        "extra_info = COALESCE(?, extra_info), " +
                        "status = COALESCE(?, status), " +
                        "media_id = COALESCE(?, media_id), " +
                        "time = COALESCE(?, time) " + // if you want to update time as well
                        "WHERE id = ?",
                Optional.ofNullable(lostnFound.getFinder()).map(User::getId).orElse(null),
                Optional.ofNullable(lostnFound.getOwner()).map(User::getId).orElse(null),
                Optional.ofNullable(lostnFound.getLandmark()).map(Locations::getId).orElse(null),
                lostnFound.getExtraInfo(),
                lostnFound.getStatus(),
                Optional.ofNullable(lostnFound.getMedia()).map(Media::getId).orElse(null),
                lostnFound.getTime(), // if you want to update time
                id
        );
    }

    @Override
    public Optional<String> deleteLostnFound(Long id) {
        LostnFound lostnFound = entityManager.find(LostnFound.class, id);
        if (lostnFound == null) {
            throw new EmptyResultDataAccessException("No lost and found item with id " + id, 1);
        }
        String mediaUrl = lostnFound.getMedia() != null ? lostnFound.getMedia().getPublicUrl() : null;
        entityManager.remove(lostnFound);
        return Optional.ofNullable(mediaUrl);
    }

    @Override
    public boolean isOwner(String userName, Long id) {
        LostnFound lostnFound = entityManager.find(LostnFound.class, id);
        return lostnFound != null && lostnFound.getOwner() != null && userName.equals(lostnFound.getOwner().getUserName());
    }

    @Override
    public boolean isFinder(String userName, Long id) {
        LostnFound lostnFound = entityManager.find(LostnFound.class, id);
        return lostnFound != null && lostnFound.getFinder() != null && userName.equals(lostnFound.getFinder().getUserName());
    }

    @Override
    public LostnFoundType findTypeById(Long id) {
        LostnFound lostnFound = entityManager.find(LostnFound.class, id);
        if (lostnFound == null) {
            throw new EmptyResultDataAccessException("No lost and found item with id " + id, 1);
        }
        return lostnFound.getType();
    }


}




