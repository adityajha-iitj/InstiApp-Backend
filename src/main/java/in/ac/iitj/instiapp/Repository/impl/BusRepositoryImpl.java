package in.ac.iitj.instiapp.Repository.impl;

import in.ac.iitj.instiapp.Repository.BusRepository;
import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.*;
import in.ac.iitj.instiapp.payload.Scheduling.Buses.BusOverrideDto;
import in.ac.iitj.instiapp.payload.Scheduling.Buses.BusScheduleDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BusRepositoryImpl implements BusRepository {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BusRepositoryImpl.class);
    private final JdbcTemplate jdbcTemplate;
    private final EntityManager entityManager;

    public BusRepositoryImpl(JdbcTemplate jdbcTemplate, EntityManager entityManager) {
        this.jdbcTemplate = jdbcTemplate;
        this.entityManager = entityManager;
    }

    // ------------------- Bus Location Operations -------------------
    @Override
    @Transactional
    public void saveBusLocation(String name) {
        if (isBusLocationExists(name) != -1L) {
            throw new DataIntegrityViolationException("Bus location already exists");
        }
        jdbcTemplate.update("insert into bus_location (name) values(?)", name);
        entityManager.flush();
    }

    @Override
    public List<String> getListOfBusLocations(Pageable pageable) {
        Query query = entityManager.createQuery("select name from BusLocation t", String.class);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        return query.getResultList();
    }

    @Override
    public Long isBusLocationExists(String name) {
        try {
            return jdbcTemplate.queryForObject("SELECT id FROM bus_location WHERE name = ?", Long.class, name);
        } catch (DataAccessException ignored) {
            return -1L;
        }
    }

    @Override
    public void updateBusLocation(String oldName, String newName) {
        if (isBusLocationExists(oldName).equals(-1L)) {
            throw new EmptyResultDataAccessException("Bus location does not exist with name " + oldName, 1);
        }
        if (isBusLocationExists(newName) != -1L) {
            throw new DataIntegrityViolationException("Bus location already exists with name " + newName);
        }
        jdbcTemplate.update("update bus_location set name=? where name=?", newName, oldName);
    }

    @Override
    public void deleteBusLocation(String name) {
        if (isBusLocationExists(name).equals(-1L)) {
            throw new EmptyResultDataAccessException("Bus location does not exist", 1);
        }
        jdbcTemplate.update("delete from bus_location where name=?", name);
    }

    // ------------------- BusSchedule Operations -------------------
    @Override
    @Transactional
    public void saveBusSchedule(String busNumber) {
        if (existsBusSchedule(busNumber) != -1L) {
            throw new DataIntegrityViolationException("Bus Number already exists with name " + busNumber);
        }
        entityManager.persist(new BusSchedule(busNumber));
    }

    @Override
    public BusScheduleDto getBusSchedule(String busNumber) {
        if (existsBusSchedule(busNumber) != -1L) {
            // Return an empty schedule or fetch runs by route if needed
            return new BusScheduleDto(busNumber, null, Optional.empty());
        }
        throw new EmptyResultDataAccessException("Bus Number" + busNumber + "Does not exists", 1);
    }

    @Override
    public List<String> getBusNumbers(Pageable pageable) {
        return entityManager.createQuery("select bs.busNumber from BusSchedule bs", String.class)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    @Override
    public Long existsBusSchedule(String busNumber) {
        try {
            return jdbcTemplate.queryForObject("select id from bus_schedule where bus_number = ?", Long.class, busNumber);
        } catch (DataAccessException e) {
            log.error(e.getMessage());
            return -1L;
        }
    }

    @Override
    public void updateBusSchedule(String oldBusNumber, String newBusNumber) {
        if (!existsBusSchedule(newBusNumber).equals(-1L)) {
            throw new DataIntegrityViolationException("Bus with number" + newBusNumber + " already exists");
        }
        if (existsBusSchedule(oldBusNumber).equals(-1L)) {
            throw new EmptyResultDataAccessException("Bus with number" + oldBusNumber + " not found", 1);
        }
        jdbcTemplate.update("update bus_schedule set bus_number=? where bus_number=?", newBusNumber, oldBusNumber);
    }

    @Override
    @Transactional
    public void deleteBusSchedule(String busNumber) {
        if (existsBusSchedule(busNumber).equals(-1L)) {
            throw new EmptyResultDataAccessException("Bus schedule with bus_number " + busNumber + " not found", 1);
        }
        entityManager.createQuery("DELETE FROM BusSchedule bs WHERE bs.busNumber = :busNumber")
                .setParameter("busNumber", busNumber)
                .executeUpdate();
        log.info("Successfully deleted BusSchedule for busNumber: {}", busNumber);
    }

    // ------------------- BusRun with Route Operations -------------------
    @Override
    @Transactional
    public void saveBusRunWithRoute(BusRun busRun) {
        entityManager.persist(busRun);
    }

    @Override
    public List<BusRun> getBusRunsForRoute(String busNumber, String routeId) {
        return entityManager.createQuery("SELECT br FROM BusRun br WHERE br.busSchedule.busNumber = :busNumber AND br.route.routeId = :routeId", BusRun.class)
                .setParameter("busNumber", busNumber)
                .setParameter("routeId", routeId)
                .getResultList();
    }

    // ------------------- BusRoute and RouteStop Operations -------------------
    @Override
    @Transactional
    public void saveBusRoute(BusRoute route) {
        entityManager.persist(route);
    }

    @Override
    public BusRoute getBusRouteByRouteId(String routeId) {
        return entityManager.createQuery("SELECT r FROM BusRoute r WHERE r.routeId = :routeId", BusRoute.class)
                .setParameter("routeId", routeId)
                .getSingleResult();
    }

    @Override
    public List<BusRoute> getAllBusRoutes() {
        return entityManager.createQuery("SELECT r FROM BusRoute r", BusRoute.class).getResultList();
    }

    @Override
    @Transactional
    public void saveRouteStop(RouteStop stop) {
        entityManager.persist(stop);
    }

    @Override
    public List<RouteStop> getRouteStopsByRouteId(String routeId) {
        return entityManager.createQuery("SELECT s FROM RouteStop s WHERE s.route.routeId = :routeId ORDER BY s.stopOrder ASC", RouteStop.class)
                .setParameter("routeId", routeId)
                .getResultList();
    }

    // ------------------- BusOverride Operations (if still needed) -------------------
    @Override
    public void saveBusOverride(String busNumber, BusOverride busOverride) {
        // Implement as needed for new structure
        entityManager.persist(busOverride);
    }

    @Override
    public boolean existsBusOverrideByPublicId(String publicId) {
        // Implement as needed for new structure
        return false;
    }

    @Override
    public List<BusOverrideDto> getBusOverrideForYearAndMonth(int year, int month) {
        // Implement as needed for new structure
        return null;
    }

    @Override
    public void updateBusOverride(String publicId, BusOverride newBusOverride) {
        // Implement as needed for new structure
    }

    @Override
    public void deleteBusOverride(List<String> busOverrideIds) {
        // Implement as needed for new structure
    }

    // Remove all legacy BusSnippet, fromLocation, toLocation, and point-to-point logic.
}
