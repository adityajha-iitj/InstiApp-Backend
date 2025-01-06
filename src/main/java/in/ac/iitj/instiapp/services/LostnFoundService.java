package in.ac.iitj.instiapp.services;

import in.ac.iitj.instiapp.database.entities.LostnFound.Locations;
import in.ac.iitj.instiapp.payload.LostnFound.LostnFoundDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LostnFoundService {

    List<LostnFoundDto> getListOfLocationsName(Pageable pageable);
    void saveLocation(Locations location);
    void deleteLocationByName(String locationName);
}