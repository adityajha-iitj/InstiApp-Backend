package in.ac.iitj.instiapp.services.impl;

import in.ac.iitj.instiapp.Repository.LostnFoundRepository;
import in.ac.iitj.instiapp.database.entities.LostnFound.Locations;
import in.ac.iitj.instiapp.payload.LostnFound.LostnFoundDto;
import in.ac.iitj.instiapp.services.LostnFoundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import in.ac.iitj.instiapp.payload.LostnFound.LostnFoundDto;

import java.util.List;

@Service
public class LostnFoundServiceImpl implements LostnFoundService {

    private final LostnFoundRepository lostnFoundRepository;

    @Autowired
    public LostnFoundServiceImpl(LostnFoundRepository lostnFoundRepository) {
        this.lostnFoundRepository = lostnFoundRepository;
    }

    @Override
    public List<LostnFoundDto> getListOfLocationsName(Pageable pageable) {
        return lostnFoundRepository.getListOfLocationsName(pageable);
    }

    @Override
    public void saveLocation(Locations location) {
        lostnFoundRepository.saveLocation(location);
    }

    @Override
    public void deleteLocationByName(String locationName) {
        lostnFoundRepository.deleteLocationByName(locationName);
    }
}