package in.ac.iitj.instiapp.Repository.impl;

import in.ac.iitj.instiapp.Repository.BusRepository;
import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.BusOverride;
import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.BusRun;
import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.BusSchedule;
import in.ac.iitj.instiapp.payload.Scheduling.Buses.BusOverrideDto;
import in.ac.iitj.instiapp.payload.Scheduling.Buses.BusRunDto;
import in.ac.iitj.instiapp.payload.Scheduling.Buses.BusScheduleDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
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

    @Override
    public void saveBusLocation(String name) {
        if (isBusLocationExists(name)) {
            throw new DataIntegrityViolationException("Bus location already exists");
        }
        jdbcTemplate.update("insert into bus_location (name) values(?)", name);
    }

    @Override
    public void deleteBusLocation(String name) {
        if (!isBusLocationExists(name)) {
            throw new EmptyResultDataAccessException("Bus location does not exist", 1);
        }
        List<Long> idOfBusRuns = entityManager.createQuery("select id from BusRun  where busSnippet.fromLocation.name = :name or busSnippet.toLocation.name = :name", Long.class)
                .setParameter("name", name).getResultList();
        deleteBusRuns(idOfBusRuns);
        List<Long> idOfBusOverrides = entityManager.createQuery("select id from BusOverride  where busSnippet.fromLocation = :name or busSnippet.toLocation.name = :name", Long.class)
                .setParameter("name", name).getResultList();
        deleteBusOverride(idOfBusOverrides);

        jdbcTemplate.update("delete from bus_location where name=?", name);
    }


    @Override
    public List<String> getListOfBusLocations(Pageable pageable) {
        Query query = entityManager.createQuery("select name from BusLocation  t", String.class);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        return query.getResultList();
    }

    @Override
    public void updateBusLocation(String oldName, String newName) {
        if (!isBusLocationExists(oldName)) {
            throw new EmptyResultDataAccessException("Bus location does not exist with name " + oldName, 1);
        }
        if (isBusLocationExists(newName)) {
            throw new DataIntegrityViolationException("Bus location already exists with name " + newName);
        }

        jdbcTemplate.update("update bus_location set name=? where name=?", newName, oldName);
    }

    @Override
    public boolean isBusLocationExists(String name) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject("SELECT EXISTS(SELECT 1 FROM bus_location WHERE name = ?)", Boolean.class, name));
    }

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
        if (existsBusSchedule(busNumber) == -1L) {
            List<BusRunDto> listOfBusRuns = entityManager.createQuery("select  " +
                            "new in.ac.iitj.instiapp.payload.Scheduling.Buses.BusRunDto" +
                            "(br.busSnippet.timeOfDeparture,br.busSnippet.fromLocation.name,br.busSnippet.toLocation.name,br.scheduleType) " +
                            "from BusRun br where br.busSchedule.busNumber = :name", BusRunDto.class)
                    .setParameter("name", busNumber)
                    .getResultList();

            return new BusScheduleDto(busNumber, new HashSet<>(listOfBusRuns), Optional.empty());
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

    @Transactional
    @Override
    public void saveBusRun(BusRun busRun, String busNumber) {
        Long id = existsBusSchedule(busNumber);
        if (id.equals(-1L)) {
            throw new EmptyResultDataAccessException("Bus number with name " + busNumber + " does not exist", 1);
        }

        if (existsBusRun(busRun) != -1L) {
            throw new DataIntegrityViolationException("Bus Run already exists");
        }
        BusSchedule busSchedule = entityManager.getReference(BusSchedule.class, id);

        busRun.setBusSchedule(busSchedule);

        entityManager.persist(busRun);

    }

    @Override
    public Long existsBusRun(BusRun busRun) {
        try {
            return entityManager.createQuery("Select id from BusRun   where busSnippet = :busSnippet and busSchedule.busNumber = :busNumber and scheduleType = :scheduleType", Long.class)
                    .setParameter("busSnippet", busRun.getBusSnippet())
                    .setParameter("busNumber", busRun.getBusSchedule().getBusNumber())
                    .setParameter("scheduleType", busRun.getScheduleType())
                    .getSingleResult();
        } catch (NoResultException e) {
            return -1L;
        }
    }

    @Transactional
    @Override
    public List<BusSchedule> getBusSchedules(Pageable pageable) {

        return entityManager.createQuery("select in.ac.iitj.instiapp.payload.Scheduling.Buses.BusScheduleDto from BusSchedule bs", BusSchedule.class).setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
    }

    /*
     Doesn't Set ScheduleType.ScheduleType once set cannot be updated can only be deleted
    */
    @Override
    public void updateBusScheduleRun(String busNumber, BusRun oldBusRun, BusRun newBusRun) {

        Long id = existsBusSchedule(busNumber);
        if (id.equals(-1L)) {
            throw new EmptyResultDataAccessException("Bus number with name " + busNumber + " does not exist", 1);
        }

        BusSchedule busSchedule = entityManager.getReference(BusSchedule.class, id);
        oldBusRun.setBusSchedule(busSchedule);
        newBusRun.setBusSchedule(busSchedule);

        Long busRunId = existsBusRun(oldBusRun);
        if (busRunId.equals(-1L)) {
            throw new EmptyResultDataAccessException("bus run with name " + busNumber + " does not exist", 1);
        }
        entityManager.createQuery("update BusRun br set br.busSnippet= :busSnippet ,br.scheduleType  = :scheduleType where br.id = :id")
                .setParameter("busSnippet", newBusRun.getBusSnippet())
                .setParameter("scheduleType", newBusRun.getScheduleType())
                .setParameter("id", busRunId)
                .executeUpdate();

    }

    @Override
    public Long existsBusSchedule(String busNumber) {
        return jdbcTemplate.queryForObject("SELECT COALESCE(select id from bus_schedule where bus_number = ?,-1)", Long.class, busNumber);
    }

    @Override
    @Transactional
    public void deleteBusSchedule(String busNumber) {

        if (existsBusSchedule(busNumber).equals(-1L)) {
            throw new EmptyResultDataAccessException("Bus schedule with bus_number " + busNumber + " not found", 1);
        }
        // Step 1: Fetch the BusRuns by busNumber
        List<Long> busRunsId = entityManager.createQuery(
                        "SELECT br.id FROM BusRun br WHERE br.busSchedule.busNumber = :busNumber", Long.class)
                .setParameter("busNumber", busNumber)
                .getResultList();

        deleteBusRuns(busRunsId);

//       Step 2: Fetch the BusOverrides
        List<Long> busOverrides = entityManager.createQuery("select bo.id from BusOverride bo where bo.busSchedule.busNumber = :busNumber", Long.class)
                .setParameter("busNumber", busNumber).getResultList();

        deleteBusOverride(busOverrides);

        // Step 4: Delete the BusSchedule
        entityManager.createQuery("DELETE FROM BusSchedule bs WHERE bs.busNumber = :busNumber")
                .setParameter("busNumber", busNumber)
                .executeUpdate();

        log.info("Successfully deleted BusSchedule, BusRun, and BusOverride records for busNumber: {}", busNumber);

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
    public void deleteBusRuns(List<Long> busRunIds) {
        if (!busRunIds.isEmpty()) {
            entityManager.createQuery("DELETE FROM BusRun  br where br.id IN :busRunIds")
                    .setParameter("busRunIds", busRunIds)
                    .executeUpdate();
        }
    }

    @Override
    public void saveBusOverride(String busNumber, BusOverride busOverride) {
        Long id = existsBusSchedule(busNumber);
        if (id.equals(-1L)) {
            throw new EmptyResultDataAccessException("Bus number with name " + busNumber + " does not exist", 1);
        }
        BusSchedule busSchedule = entityManager.getReference(BusSchedule.class, id);
        busOverride.setBusSchedule(busSchedule);
        if (existsBusOverride(busOverride) == -1L) {
            throw new DataIntegrityViolationException("Bus Override record for bus_number " + busNumber + "already exist with same configuration");
        }
        entityManager.persist(busOverride);
    }

    @Override
    public Long existsBusOverride(BusOverride busOverride) {
        try {
            return entityManager.createQuery("select bo.id from BusOverride bo where bo.busSnippet = :busSnippet and bo.description = :description and bo.overrideDate = :overrideDate and bo.busSchedule.busNumber = :busNumber", Long.class)
                    .setParameter("busSnippet", busOverride.getBusSnippet())
                    .setParameter("description", busOverride.getDescription())
                    .setParameter("overrideDate", busOverride.getOverrideDate())
                    .setParameter("busNumber", busOverride.getBusSchedule().getBusNumber())
                    .getSingleResult();

        } catch (NoResultException e) {
            return -1L;
        }
    }

    @Override
    public List<BusOverrideDto> getBusOverrideForYearAndMonth(int year, int month) {
        return entityManager.createQuery("select new in.ac.iitj.instiapp.payload.Scheduling.Buses.BusOverrideDto(bo.busSchedule.busNumber, bo.busSnippet.timeOfDeparture, bo.busSnippet.fromLocation.name, bo.busSnippet.toLocation.name, bo.overrideDate, bo.description) from BusOverride bo where YEAR (bo.overrideDate) = :year and MONTH (bo.overrideDate) = :month", BusOverrideDto.class)
                .setParameter("year", year)
                .setParameter("month", month)
                .getResultList();
    }

    @Override
    public void updateBusOverride(String busNumber, BusOverride oldBusOverride, BusOverride newBusOverride) {
        Long id = existsBusSchedule(busNumber);
        if (id.equals(-1L)) {
            throw new EmptyResultDataAccessException("Bus number with name " + busNumber + " does not exist", 1);
        }
        BusSchedule busSchedule = entityManager.getReference(BusSchedule.class, id);
        oldBusOverride.setBusSchedule(busSchedule);
        Long oldBusId = existsBusOverride(oldBusOverride);
        if (oldBusId.equals(-1L)) {
            throw new EmptyResultDataAccessException("The bus Override doesn't exist", 1);
        }

        entityManager.createQuery("update  BusOverride  bo set bo.description = :description, bo.busSnippet = :busSnippet, bo.overrideDate = :overrideDate where bo.id = :id")
                .setParameter("description", newBusOverride.getDescription())
                .setParameter("busSnippet", newBusOverride.getBusSnippet())
                .setParameter("overrideDate", newBusOverride.getOverrideDate())
                .setParameter("id", oldBusId)
                .executeUpdate();

    }


    @Override
    public void deleteBusOverride(List<Long> busOverrideIds) {
        if (!busOverrideIds.isEmpty()) {
            entityManager.createQuery("DELETE FROM BusOverride br where br.id IN :busOverrideIds")
                    .setParameter("busOverrideIds", busOverrideIds)
                    .executeUpdate();
        }
    }


}
