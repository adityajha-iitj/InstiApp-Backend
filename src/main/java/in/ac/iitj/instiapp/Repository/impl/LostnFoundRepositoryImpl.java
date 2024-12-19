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


}




