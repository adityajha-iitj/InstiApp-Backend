package in.ac.iitj.instiapp.Repository;

import in.ac.iitj.instiapp.database.entities.LostnFound.Locations;
import in.ac.iitj.instiapp.payload.LostnFound.LostnFoundDto;
import org.springframework.data.domain.Pageable;


import java.util.List;


public interface LostnFoundRepository {

   List<LostnFoundDto> getListOfLocationsName(Pageable pageable);
   void saveLocation(Locations locations);
   void deleteLocationByName(String locationName);

}


