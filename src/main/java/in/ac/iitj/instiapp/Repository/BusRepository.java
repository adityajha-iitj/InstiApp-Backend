package in.ac.iitj.instiapp.Repository;

import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.BusLocation;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.Repository;

public interface BusLocationRepository extends Repository<BusLocation, Long>, JpaSpecificationExecutor<BusLocation> {
}