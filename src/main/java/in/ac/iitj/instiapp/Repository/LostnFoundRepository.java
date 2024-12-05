package in.ac.iitj.instiapp.Repository;

import in.ac.iitj.instiapp.database.entities.LostnFound.Locations;
import in.ac.iitj.instiapp.database.entities.LostnFound.LostnFound;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


public interface LostnFoundRepository {

   List<Map<String, Object>> getListOfLocations(Pageable pageable);
   Long save(LostnFound lostnFound);
   Long save(Locations locations);
}


