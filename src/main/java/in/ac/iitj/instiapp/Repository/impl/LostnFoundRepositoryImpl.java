package in.ac.iitj.instiapp.Repository.impl;

import in.ac.iitj.instiapp.database.entities.LostnFound.Locations;
import in.ac.iitj.instiapp.database.entities.LostnFound.LostnFound;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

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
    public List<Map<String, Object>> getListOfLocations(Pageable pageable) {
        return jdbcTemplate.queryForList("SELECT  * FROM locations");
    }

    @Override
    public Long save(LostnFound lostnFound) {
        return 0L;
    }

    @Override
    @Transactional
    public Long save(Locations locations) {
        entityManager.persist(locations);
        return  0L;
    }


}




