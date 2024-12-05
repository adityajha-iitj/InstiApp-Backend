package in.ac.iitj.instiapp.Repository.impl;

import in.ac.iitj.instiapp.Repository.BusRepository;
import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.BusLocation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BusRepositoryImpl implements BusRepository {

    private  final JdbcTemplate jdbcTemplate;
    private  final EntityManager entityManager;

    public  BusRepositoryImpl(JdbcTemplate jdbcTemplate, EntityManager entityManager) {
        this.jdbcTemplate = jdbcTemplate;
        this.entityManager = entityManager;
    }

    @Override
    public void saveBusLocation(String name) {
        jdbcTemplate.update("insert into bus_location (name) values(?)",name);
    }

    @Override
    public void deleteBusLocation(String name) {
        jdbcTemplate.update("delete from bus_location where name=?",name);
    }

    @Override
    public List<String> getListOfBusLocations(Pageable pageable) {
     Query query =  entityManager.createQuery("select name from BusLocation  t", String.class);
     query.setFirstResult((int) pageable.getOffset());
     query.setMaxResults(pageable.getPageSize());
     return  query.getResultList();
    }

    @Override
    public void saveBusSchedule(String busNumber) {
      jdbcTemplate.update("insert into bus_schedule (busNumber) values(?)",busNumber);
    }

    @Override
    public void deleteBusSchedule(String busNumber) {
        jdbcTemplate.update("delete from bus_schedule where busNumber=?",busNumber);
    }

    @Override
    public void updateBusSchedule(String oldBusNumber, String newBusNumber) {
        jdbcTemplate.update("update bus_schedule set busNumber=? where busNumber=?",newBusNumber,oldBusNumber);
    }
}
