package in.ac.iitj.instiapp.services.impl;

import in.ac.iitj.instiapp.Repository.LostnFoundRepository;
import in.ac.iitj.instiapp.database.entities.LostnFound.Locations;
import in.ac.iitj.instiapp.database.entities.LostnFound.LostnFound;
import in.ac.iitj.instiapp.mappers.LostnFoundDtoMapper;
import in.ac.iitj.instiapp.payload.LostnFound.LostnFoundDto;
import in.ac.iitj.instiapp.services.LostnFoundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LostnFoundServiceImpl implements LostnFoundService {

    private final LostnFoundRepository lostnFoundRepository;

    @Autowired
    public LostnFoundServiceImpl(LostnFoundRepository lostnFoundRepository) {
        this.lostnFoundRepository = lostnFoundRepository;
    }

    @Override
    public List<String> getListOfLocationsName(Pageable pageable) {
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

    @Override
    public void updateLocation(String oldLocationName, Locations location) {
        lostnFoundRepository.updateLocation(oldLocationName, location);
    }

    @Override
    public void saveLostAndFound(LostnFoundDto lostnFoundDto) {
        LostnFound lostnFound = LostnFoundDtoMapper.INSTANCE.toEntity(lostnFoundDto);
        lostnFoundRepository.saveLostnFoundDetails(lostnFound);
    }

    @Override
    public void updateLostAndFound(LostnFoundDto lostnFoundDto) {
        LostnFound lostnFound = LostnFoundDtoMapper.INSTANCE.toEntity(lostnFoundDto);
        lostnFoundRepository.updateLostnFound(lostnFound , lostnFound.getPublicId());
    }

    @Override
    public void deleteLostAndFound(String publicId) {
        lostnFoundRepository.deleteLostnFound(publicId);
    }

    @Override
    public List<LostnFoundDto> getLostAndFoundByFilter(Optional<Boolean> status, Optional<String> owner, Optional<String> finder, Optional<String> landmark, Pageable pageable) {
        return lostnFoundRepository.getLostnFoundByFilter(status, owner, finder, landmark, pageable);
    }
}
