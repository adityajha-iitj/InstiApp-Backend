package in.ac.iitj.instiapp.Repository.impl;

import in.ac.iitj.instiapp.Repository.BusRepository;
import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.*;
import in.ac.iitj.instiapp.payload.Scheduling.Buses.BusOverrideDto;
import in.ac.iitj.instiapp.payload.Scheduling.Buses.BusRunDto;
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
        Query query = entityManager.createQuery("select name from BusLocation  t", String.class);
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
        List<String> idOfBusRuns = entityManager.createQuery("select publicId from BusRun  where busSnippet.fromLocation.name = :name or busSnippet.toLocation.name = :name", String.class)
                .setParameter("name", name).getResultList();
        deleteBusRuns(idOfBusRuns);
        List<String> idOfBusOverrides = entityManager.createQuery("select publicId from BusOverride  where busSnippet.fromLocation.name = :name or busSnippet.toLocation.name = :name", String.class)
                .setParameter("name", name).getResultList();
        deleteBusOverride(idOfBusOverrides);

        jdbcTemplate.update("delete from bus_location where name=?", name);
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
        if (existsBusSchedule(busNumber) != -1L) {
            List<BusRunDto> listOfBusRuns = entityManager.createQuery("select  " +
                            "new in.ac.iitj.instiapp.payload.Scheduling.Buses.BusRunDto" +
                            "(br.publicId,br.busSnippet.timeOfDeparture,br.busSnippet.fromLocation.name,br.busSnippet.toLocation.name,br.scheduleType) " +
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
        // Step 1: Fetch the BusRuns by busNumber
        List<String> busRunsId = entityManager.createQuery(
                        "SELECT br.publicId FROM BusRun br WHERE br.busSchedule.busNumber = :busNumber", String.class)
                .setParameter("busNumber", busNumber)
                .getResultList();

        deleteBusRuns(busRunsId);

//       Step 2: Fetch the BusOverrides
        List<String> busOverrides = entityManager.createQuery("select bo.publicId from BusOverride bo where bo.busSchedule.busNumber = :busNumber", String.class)
                .setParameter("busNumber", busNumber).getResultList();

        deleteBusOverride(busOverrides);

        // Step 3: Delete the BusSchedule
        entityManager.createQuery("DELETE FROM BusSchedule bs WHERE bs.busNumber = :busNumber")
                .setParameter("busNumber", busNumber)
                .executeUpdate();

        log.info("Successfully deleted BusSchedule, BusRun, and BusOverride records for busNumber: {}", busNumber);

    }

    @Transactional
    @Override
    public void saveBusRun(BusRun busRun, String busNumber) {
        Long id = existsBusSchedule(busNumber);
        if (id.equals(-1L)) {
            throw new EmptyResultDataAccessException("Bus number with name " + busNumber + " does not exist", 1);
        }

        busRun.setBusSchedule(new BusSchedule(id, busNumber, null, null));
        if (existsBusRun(busRun).isPresent()) {
            throw new DataIntegrityViolationException("Bus Run already exists");
        }
        Long fromLocationId = isBusLocationExists(busRun.getBusSnippet().getFromLocation().getName());
        Long toLocationId = isBusLocationExists(busRun.getBusSnippet().getToLocation().getName());
        if (fromLocationId.equals(-1L) || toLocationId.equals(-1L)) {
            throw new EmptyResultDataAccessException("From location or To location do not exist", 1);
        }
        busRun.setBusSnippet(new BusSnippet(busRun.getBusSnippet().getTimeOfDeparture(),
                entityManager.getReference(BusLocation.class, fromLocationId),
                entityManager.getReference(BusLocation.class, toLocationId)
        ));
        entityManager.persist(busRun);
    }

    @Override
    public Boolean existsBusRunByPublicId(String publicId) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject("select exists(select 1 from bus_run where public_id = ?)", Boolean.class, publicId));
    }

    /**
     * BusNumber should not be null.
     *
     * @param busRun does take value of busNumber from BusSchedule.
     * @return -1 if busRun  doesn't exists for particular Schedule
     * @assumptions busNumber in busSchedule exists in database
     */
    @Transactional
    protected Optional<String> existsBusRun(BusRun busRun) {
        try {
            return Optional.of(entityManager.createQuery("Select publicId from BusRun   where busSnippet.fromLocation.name = :fromLocation and busSnippet.toLocation.name = :toLocation and busSnippet.timeOfDeparture = :timeOfDeparture and busSchedule.busNumber = :busNumber and scheduleType = :scheduleType", String.class)
                    .setParameter("fromLocation", busRun.getBusSnippet().getFromLocation().getName())
                    .setParameter("toLocation", busRun.getBusSnippet().getToLocation().getName())
                    .setParameter("timeOfDeparture", busRun.getBusSnippet().getTimeOfDeparture())
                    .setParameter("busNumber", busRun.getBusSchedule().getBusNumber())
                    .setParameter("scheduleType", busRun.getScheduleType())
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }


    /*
     Doesn't Set ScheduleType.ScheduleType once set cannot be updated can only be deleted
    */
    @Override
    public void updateBusScheduleRun(String publicId, BusRun newBusRun) {


        if (!existsBusRunByPublicId(publicId)) {
            throw new EmptyResultDataAccessException("Bus Run does not exist", 1);
        }


        //From data integrity in database busNumber would exist
        String busNumber = entityManager.createQuery("select br.busSchedule.busNumber from BusRun br where br.publicId = :publicId", String.class)
                .setParameter("publicId", publicId).getSingleResult();

        newBusRun.setBusSchedule(new BusSchedule(busNumber));

        Optional<String> otherPublicId = existsBusRun(newBusRun);
        if (otherPublicId.isPresent() && !otherPublicId.get().equals(publicId)) {
            throw new DataIntegrityViolationException("Bus Run already exists");
        }

        Long fromLocation = isBusLocationExists(newBusRun.getBusSnippet().getFromLocation().getName());
        Long toLocation = isBusLocationExists(newBusRun.getBusSnippet().getToLocation().getName());
        if (fromLocation.equals(-1L) || toLocation.equals(-1L)) {
            throw new EmptyResultDataAccessException("From location or To location do not exist", 1);
        }


        entityManager.createQuery("update BusRun  br set br.busSnippet = :busSnippet,br.scheduleType  = :scheduleType where br.publicId = :publicId")
                .setParameter("busSnippet",
                        new BusSnippet(newBusRun.getBusSnippet().getTimeOfDeparture(),
                                entityManager.getReference(BusLocation.class, fromLocation),
                                entityManager.getReference(BusLocation.class, toLocation)
                        )
                )
                .setParameter("scheduleType", newBusRun.getScheduleType())
                .setParameter("publicId", publicId)
                .executeUpdate();

    }


    @Override
    public void deleteBusRuns(List<String> busRunIds) {
        if (!busRunIds.isEmpty()) {
            entityManager.createQuery("DELETE FROM BusRun  br where br.publicId IN :busRunIds")
                    .setParameter("busRunIds", busRunIds)
                    .executeUpdate();
        }
    }

    @Override
    @Transactional
    public void saveBusOverride(String busNumber, BusOverride busOverride) {
        Long id = existsBusSchedule(busNumber);
        if (id.equals(-1L)) {
            throw new EmptyResultDataAccessException("Bus number with name " + busNumber + " does not exist", 1);
        }
        busOverride.setBusSchedule(new BusSchedule(busNumber));
        if (existsBusOverride(busOverride).isPresent()) {
            throw new DataIntegrityViolationException("Bus Override record for bus_number " + busNumber + "already exist with same configuration");
        }
        BusSchedule busSchedule = entityManager.getReference(BusSchedule.class, id);
        busOverride.setBusSchedule(busSchedule);
        Long fromLocationId = isBusLocationExists(busOverride.getBusSnippet().getFromLocation().getName());
        Long toLocationId = isBusLocationExists(busOverride.getBusSnippet().getToLocation().getName());
        if (fromLocationId.equals(-1L) || toLocationId.equals(-1L)) {
            throw new EmptyResultDataAccessException("From location or To location do not exist", 1);
        }
        busOverride.setBusSnippet(new BusSnippet(busOverride.getBusSnippet().getTimeOfDeparture(),
                entityManager.getReference(BusLocation.class, fromLocationId),
                entityManager.getReference(BusLocation.class, toLocationId)
        ));

        entityManager.persist(busOverride);
    }


    /**
     * Doesn't check for publicId instead return publicId
     *
     * @param busOverride busNumber shouldNot be null
     * @return publicId which matches the constraints
     */
    private Optional<String> existsBusOverride(BusOverride busOverride) {
        try {
            return Optional.of(entityManager.createQuery("select bo.publicId from BusOverride bo where bo.busSnippet.fromLocation.name = :fromLocation and busSnippet.toLocation.name = :toLocation and busSnippet.timeOfDeparture = :timeOfDeparture and bo.description = :description and bo.overrideDate = :overrideDate and bo.busSchedule.busNumber = :busNumber", String.class)
                    .setParameter("fromLocation", busOverride.getBusSnippet().getFromLocation().getName())
                    .setParameter("toLocation", busOverride.getBusSnippet().getToLocation().getName())
                    .setParameter("timeOfDeparture", busOverride.getBusSnippet().getTimeOfDeparture())
                    .setParameter("description", busOverride.getDescription())
                    .setParameter("overrideDate", busOverride.getOverrideDate())
                    .setParameter("busNumber", busOverride.getBusSchedule().getBusNumber())
                    .getSingleResult());

        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean existsBusOverrideByPublicId(String publicId) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject("select exists(select 1 from bus_override where public_id = ?)", Boolean.class, publicId));
    }

    @Override
    public List<BusOverrideDto> getBusOverrideForYearAndMonth(int year, int month) {
        return entityManager.createQuery("select new in.ac.iitj.instiapp.payload.Scheduling.Buses.BusOverrideDto(bo.publicId,bo.busSchedule.busNumber, bo.busSnippet.timeOfDeparture, bo.busSnippet.fromLocation.name, bo.busSnippet.toLocation.name, bo.overrideDate, bo.description) from BusOverride bo where YEAR (bo.overrideDate) = :year and MONTH (bo.overrideDate) = :month", BusOverrideDto.class)
                .setParameter("year", year)
                .setParameter("month", month)
                .getResultList();
    }

    @Override
    public void updateBusOverride(String publicId, BusOverride newBusOverride) {
        if (!existsBusOverrideByPublicId(publicId)) {
            throw new EmptyResultDataAccessException("Bus Override does not exist", 1);
        }


        String busNumber = entityManager.createQuery("select bo.busSchedule.busNumber from BusOverride  bo where bo.publicId = :publicId", String.class)
                .setParameter("publicId", publicId)
                .getSingleResult();

        newBusOverride.setBusSchedule(new BusSchedule(busNumber));


        Optional<String> otherBusOverridePublicId = existsBusOverride(newBusOverride);
        // If some other entry equals to the value we want to change
        if (otherBusOverridePublicId.isPresent() && !otherBusOverridePublicId.get().equals(publicId)) {
            throw new DataIntegrityViolationException("Bus Override already exists");
        }

        Long fromLocationId = isBusLocationExists(newBusOverride.getBusSnippet().getFromLocation().getName());
        Long toLocationId = isBusLocationExists(newBusOverride.getBusSnippet().getToLocation().getName());
        if (fromLocationId.equals(-1L) || toLocationId.equals(-1L)) {
            throw new EmptyResultDataAccessException("From location or To location do not exist", 1);
        }

        // Could be optimised
        entityManager.createQuery("update  BusOverride  bo set bo.description = :description, bo.busSnippet = :busSnippet, bo.overrideDate = :overrideDate where bo.publicId = :id")
                .setParameter("description", newBusOverride.getDescription())
                .setParameter("busSnippet",
                        new BusSnippet(newBusOverride.getBusSnippet().getTimeOfDeparture(),
                                entityManager.getReference(BusLocation.class, fromLocationId),
                                entityManager.getReference(BusLocation.class, toLocationId)
                        )
                )
                .setParameter("overrideDate", newBusOverride.getOverrideDate())
                .setParameter("id", publicId)
                .executeUpdate();

    }


    @Override
    public void deleteBusOverride(List<String> busOverridePublicIds) {
        if (!busOverridePublicIds.isEmpty()) {
            entityManager.createQuery("DELETE FROM BusOverride br where br.publicId IN :busOverrideIds")
                    .setParameter("busOverrideIds", busOverridePublicIds)
                    .executeUpdate();
        }

    }


}
