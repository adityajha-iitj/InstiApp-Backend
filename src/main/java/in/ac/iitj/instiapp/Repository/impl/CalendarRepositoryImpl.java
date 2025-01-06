package in.ac.iitj.instiapp.Repository.impl;

import in.ac.iitj.instiapp.Repository.CalendarRepository;
import in.ac.iitj.instiapp.database.entities.Scheduling.Calendar.Calendar;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


@Repository
public class CalendarRepositoryImpl implements CalendarRepository {

    private final  JdbcTemplate jdbcTemplate;
    private final  EntityManager entityManager;


    @Autowired
    public CalendarRepositoryImpl(JdbcTemplate jdbcTemplate, EntityManager entityManager) {
    this.jdbcTemplate = jdbcTemplate;
    this.entityManager = entityManager;
    }


    @Override
    public void save(Calendar calendar) {
      entityManager.persist(calendar);
    }



    @Override
    public Long calendarExists(String public_id) {
        return jdbcTemplate.queryForObject("select coalesce(max(id), -1) from calendar where public_id= ?", Long.class, public_id);
    }
}
