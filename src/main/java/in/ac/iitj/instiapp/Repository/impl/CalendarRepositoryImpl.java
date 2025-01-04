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
    public Long save(Calendar calendar) {
      return jdbcTemplate.queryForObject("INSERT into calendar (public_id) values(?) RETURNING id",Long.class,calendar.getPublicId());
    }



    @Override
    public Boolean calendarExists(String public_id) {
       return  jdbcTemplate.queryForObject("SELECT EXISTS(SELECT 1 FROM calendar where public_id = ?)", Boolean.class,public_id);
    }
}
