package in.ac.iitj.instiapp.services.impl;


import in.ac.iitj.instiapp.Repository.BusRepository;
import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.BusLocation;
import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.BusOverride;
import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.BusRun;
import in.ac.iitj.instiapp.payload.Scheduling.Buses.BusOverrideDto;
import in.ac.iitj.instiapp.payload.Scheduling.Buses.BusScheduleDto;
import in.ac.iitj.instiapp.services.BusService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class BusServiceImpl implements BusService {

    private final BusRepository busRepository;

    @Autowired
    public BusServiceImpl(BusRepository busRepository) {
        this.busRepository = busRepository;
    }

    @Override
    @Transactional
    public void saveBusLocation(String name) {
        if(name == null || name.isEmpty()) {
            throw new DataIntegrityViolationException("name cannot be null or empty");
        }
        else {
            busRepository.saveBusLocation(name);
        }
    }

    @Override
    public List<String> getBusLocations(Pageable pageable) {
        return busRepository.getListOfBusLocations(pageable);
    }

    @Override
    public Long isBusLocationExist(String name) {
        return busRepository.isBusLocationExists(name);
    }

    @Override
    public void updateBusLocation(String oldName, String newName) {
        if(oldName == null || oldName.isEmpty() || newName == null || newName.isEmpty()) {
            throw new DataIntegrityViolationException("old name and new name cannot be  null or empty");
        }
        busRepository.updateBusLocation(oldName, newName);
    }

    @Override
    @Transactional
    public void deleteBusLocation(String name) {
        busRepository.deleteBusLocation(name);

    }

    @Override
    @Transactional
    public void saveBusSchedule(String busNumber) {
        busRepository.saveBusSchedule(busNumber);

    }

    @Override
    public BusScheduleDto getBusSchedule(String busNumber) {
        return busRepository.getBusSchedule(busNumber);
    }

    @Override
    public List<String> getBusNumbers(Pageable pageable) {
        return busRepository.getBusNumbers(pageable);
    }

    @Override
    public Long existsBusSchedule(String busNumber) {
        return busRepository.existsBusSchedule(busNumber);
    }

    @Override
    public void updateBusSchedule(String oldBusNumber, String newBusNumber) {
        busRepository.updateBusSchedule(oldBusNumber, newBusNumber);
    }

    @Override
    public void deleteBusSchedule(String busNumber) {
        busRepository.deleteBusSchedule(busNumber);
    }

    @Override
    @Transactional
    public void saveBusRun(BusRun busRun, String busNumber) {
        busRepository.saveBusRun(busRun, busNumber);
    }

    @Override
    public Boolean existsBusRunByPublicId(String publicId) {
        return busRepository.existsBusRunByPublicId(publicId);
    }

    @Override
    @Transactional
    public void updateBusScheduleRun(String publicId, BusRun newBusRun) {
        busRepository.updateBusScheduleRun(publicId, newBusRun);
    }

    @Override
    @Transactional
    public void deleteBusRuns(List<String> busRunPublicIds) {
        busRepository.deleteBusRuns(busRunPublicIds);
    }

    @Override
    @Transactional
    public void saveBusOverride(String busNumber, BusOverride busOverride) {
        busRepository.saveBusOverride(busNumber, busOverride);
    }

    @Override
    public boolean existsBusOverrideByPublicId(String publicId) {
        return busRepository.existsBusOverrideByPublicId(publicId);
    }

    @Override
    public List<BusOverrideDto> getBusOverrideForYearAndMonth(int year, int month) {
        return busRepository.getBusOverrideForYearAndMonth(year, month);
    }

    @Override
    @Transactional
    public void updateBusOverride(String publicId, BusOverride newBusOverride) {
        busRepository.updateBusOverride(publicId, newBusOverride);
    }

    @Override
    @Transactional
    public void deleteBusOverride(List<String> busOverrideIds) {
        busRepository.deleteBusOverride(busOverrideIds);
    }
}