package in.ac.iitj.instiapp.Repository;

import in.ac.iitj.instiapp.database.entities.LostnFound.Locations;
import org.springframework.data.domain.Pageable;
import java.util.List;



public interface LostnFoundRepository {

   List<LostnFoundDto> getListOfLocationsName(Pageable pageable);
   void saveLocation(Locations locations);
   void deleteLocationByName(String locationName);
   public List<LostnFoundDto> getLostnFoundByStatus(Boolean status, Pageable pageable);
   public List<LostnFoundDto> getLostnFoundWithDetails(Pageable pageable);
   public List<LostnFoundDto> getLostnFoundByLandmark(String landmarkName, Pageable pageable);
   public void saveLostnFoundDetails(LostnFoundDto lostnFoundDto);

}


