package in.ac.iitj.instiapp.Repository.impl;

import in.ac.iitj.instiapp.Repository.BusRepository;
import in.ac.iitj.instiapp.database.entities.LostnFound.Locations;
import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.BusLocation;
import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.BusRun;
import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.BusSchedule;
import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.ScheduleType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityNotFoundException;

import java.sql.Time;
import java.util.List;

@Repository
public class BusRepositoryImpl implements BusRepository {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BusRepositoryImpl.class);
    private final JdbcTemplate jdbcTemplate;
    private final EntityManager entityManager;

    public BusRepositoryImpl(JdbcTemplate jdbcTemplate, EntityManager entityManager) {
        this.jdbcTemplate = jdbcTemplate;
        this.entityManager = entityManager;
    }

    @Override
    public void saveBusLocation(String name) {
        jdbcTemplate.update("insert into bus_location (name) values(?)", name);
    }

    @Override
    public void deleteBusLocation(String name) {
        jdbcTemplate.update("delete from bus_location where name=?", name);
    }

    @Override
    public BusLocation getBusLocation(String name) {
        BusLocation busLocation;
        try {
            Long fromLocationId = jdbcTemplate.queryForObject("select id from bus_location where name=?", Long.class, name);
            return entityManager.getReference(BusLocation.class, fromLocationId);
        } catch (EmptyResultDataAccessException e) {
            log.error("Bus location with name {} not found", name);
            throw new EmptyResultDataAccessException( "Bus location with name "+ name + " not found",1);
        }
    }


    @Override
    public List<String> getListOfBusLocations(Pageable pageable) {
        Query query = entityManager.createQuery("select name from BusLocation  t", String.class);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        return query.getResultList();
    }

    @Override
    public boolean isBusLocationExists(String name) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject("SELECT EXISTS(SELECT 1 FROM bus_location WHERE name = ?)", Boolean.class, name));
    }

    @Override
    @Transactional
    public void saveBusSchedule(BusSchedule busSchedule) {
        entityManager.persist(busSchedule);
    }

    @Override
    public BusSchedule getBusSchedule(String busNumber) {
        try {
           Long id =  jdbcTemplate.queryForObject("select id from bus_schedule where bus_number=?", Long.class, busNumber);
          return entityManager.getReference(BusSchedule.class, id);
        }catch (Exception e) {
            log.error("Bus schedule with bus_number {} not found", busNumber);
            throw new EmptyResultDataAccessException("Bus schedule with bus_number "+ busNumber + " not found",1);
        }
    }

    @Transactional
    @Override
    public void saveBusRun(BusRun busRun) {
        entityManager.persist(busRun);

    }

    @Transactional
    @Override
    public List<BusSchedule> getBusSchedules(Pageable pageable) {
        return entityManager.createQuery("select bs from BusSchedule bs", BusSchedule.class).setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
    }

    /*
     Doesn't Set ScheduleType.ScheduleType once set cannot be updated can only be deleted
    */
    @Override
    public void updateBusScheduleRun(String busNumber, ScheduleType scheduleType,Time timeOfDeparture, BusRun busRun) {

        Boolean b = entityManager.createQuery("SELECT EXISTS(SELECT id from BusRun  br where br.scheduleType = :scheduleType and br.busSchedule.busNumber = :busNumber and br.timeOfDeparture = :timeOfDeparture)",Boolean.class)
                .setParameter("scheduleType", scheduleType)
                .setParameter("busNumber",busNumber)
                .setParameter("timeOfDeparture", busRun.getTimeOfDeparture())
                .getSingleResult();

        if(b){
            throw new DataIntegrityViolationException("Bus run with id " + busNumber + "and Time of Departure" + timeOfDeparture+ "and Schedule Type" + scheduleType+ " already exists");
        }

        Long Id = entityManager.createQuery("select br.id from BusRun br where br.scheduleType = :scheduleType and br.busSchedule.busNumber = :busNumber and br.timeOfDeparture = :timeOfDeparture",Long.class)
                .setParameter("scheduleType",scheduleType)
                .setParameter("timeOfDeparture",timeOfDeparture)
                .setParameter("busNumber", busNumber).getSingleResult();

        BusRun busRun1 = entityManager.getReference(BusRun.class, Id);
        busRun1.setToLocation(busRun.getToLocation());
        busRun1.setFromLocation(busRun.getFromLocation());
        busRun1.setTimeOfDeparture(busRun.getTimeOfDeparture());

        entityManager.merge(busRun1);
    }

    @Override
    public boolean existsBusSchedule(String busNumber) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject("SELECT EXISTS(SELECT 1 FROM bus_schedule WHERE bus_number = ?)", Boolean.class, busNumber));
    }

    @Override
    @Transactional
    public void deleteBusSchedule(String busNumber) {
        try {
            // Step 1: Fetch the BusSchedule by busNumber
            BusSchedule busSchedule = entityManager.createQuery(
                            "SELECT bs FROM BusSchedule bs WHERE bs.busNumber = :busNumber", BusSchedule.class)
                    .setParameter("busNumber", busNumber)
                    .getSingleResult();

            if (busSchedule == null) {
                throw new EntityNotFoundException("BusSchedule with busNumber " + busNumber + " not found.");
            }

            // Step 2: Fetch and delete associated BusOverride records
            List<Long> busRunIds = entityManager.createQuery(
                            "SELECT br.Id FROM BusRun br WHERE br.busSchedule = :busSchedule", Long.class)
                    .setParameter("busSchedule", busSchedule)
                    .getResultList();

            if (!busRunIds.isEmpty()) {
                entityManager.createQuery("DELETE FROM BusOverride bo WHERE bo.busRun.Id IN :busRunIds")
                        .setParameter("busRunIds", busRunIds)
                        .executeUpdate();
            }

            // Step 3: Delete associated BusRun records
            entityManager.createQuery("DELETE FROM BusRun br WHERE br.busSchedule = :busSchedule")
                    .setParameter("busSchedule", busSchedule)
                    .executeUpdate();

            // Step 4: Delete the BusSchedule
            entityManager.createQuery("DELETE FROM BusSchedule bs WHERE bs.busNumber = :busNumber")
                    .setParameter("busNumber", busNumber)
                    .executeUpdate();

            log.info("Successfully deleted BusSchedule, BusRun, and BusOverride records for busNumber: {}", busNumber);
        } catch (EntityNotFoundException e) {
            log.error("Error: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Failed to delete BusSchedule with busNumber: {}. Exception: {}", busNumber, e.getMessage());
            throw new RuntimeException("Failed to delete BusSchedule with cascading deletions.", e);
        }
    }


    //    TODO
    @Override
    public void updateBusSchedule(String oldBusNumber, String newBusNumber) {
        if(existsBusSchedule(newBusNumber)){
            throw new DataIntegrityViolationException("Bus with number" + newBusNumber + " already exists");
        }
        jdbcTemplate.update("update bus_schedule set bus_number=? where bus_number=?", newBusNumber, oldBusNumber);
    }
}
