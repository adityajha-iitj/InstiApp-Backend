package in.ac.iitj.instiapp.services.impl;

import in.ac.iitj.instiapp.Repository.BusRepository;
import in.ac.iitj.instiapp.services.BusService;
import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.BusLocation;
import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.BusRun;
import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.BusSchedule;
import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.ScheduleType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.util.List;

@Service
public class BusServiceImpl implements BusService {

    private static final Logger log = LoggerFactory.getLogger(BusServiceImpl.class);
    private final BusRepository busRepository;

    @Autowired
    public BusServiceImpl(BusRepository busRepository) {
        this.busRepository = busRepository;
    }

    @Override
    @Transactional
    public void createBusLocation(String name) {
        log.info("Creating bus location: {}", name);
        busRepository.saveBusLocation(name);
    }

    @Override
    @Transactional
    public void removeBusLocation(String name) {
        log.info("Removing bus location: {}", name);
        busRepository.deleteBusLocation(name);
    }

    @Override
    public BusLocation findBusLocation(String name) {
        log.info("Finding bus location: {}", name);
        return busRepository.getBusLocation(name);
    }

    @Override
    public List<String> listBusLocations(Pageable pageable) {
        log.info("Listing bus locations");
        return busRepository.getListOfBusLocations(pageable);
    }

    @Override
    public boolean checkBusLocationExists(String name) {
        return busRepository.isBusLocationExists(name);
    }

    @Override
    @Transactional
    public void createBusSchedule(BusSchedule busSchedule) {
        log.info("Creating bus schedule: {}", busSchedule.getBusNumber());
        busRepository.saveBusSchedule(busSchedule);
    }

    @Override
    public BusSchedule findBusSchedule(String busNumber) {
        log.info("Finding bus schedule: {}", busNumber);
        return busRepository.getBusSchedule(busNumber);
    }

    @Override
    public List<BusSchedule> listBusSchedules(Pageable pageable) {
        log.info("Listing bus schedules");
        return busRepository.getBusSchedules(pageable);
    }

    @Override
    @Transactional
    public void createBusRun(BusRun busRun) {
        log.info("Creating bus run for schedule: {}", busRun.getBusSchedule().getBusNumber());
        busRepository.saveBusRun(busRun);
    }

    @Override
    @Transactional
    public void updateBusScheduleRun(String busNumber, ScheduleType scheduleType, Time timeOfDeparture, BusRun busRun) {
        log.info("Updating bus schedule run: {}, {}, {}", busNumber, scheduleType, timeOfDeparture);
        busRepository.updateBusScheduleRun(busNumber, scheduleType, timeOfDeparture, busRun);
    }

    @Override
    public boolean checkBusScheduleExists(String busNumber) {
        return busRepository.existsBusSchedule(busNumber);
    }

    @Override
    @Transactional
    public void removeBusSchedule(String busNumber) {
        log.info("Removing bus schedule: {}", busNumber);
        busRepository.deleteBusSchedule(busNumber);
    }

    @Override
    @Transactional
    public void updateBusScheduleNumber(String oldBusNumber, String newBusNumber) {
        log.info("Updating bus schedule number from {} to {}", oldBusNumber, newBusNumber);
        busRepository.updateBusSchedule(oldBusNumber, newBusNumber);
    }
}