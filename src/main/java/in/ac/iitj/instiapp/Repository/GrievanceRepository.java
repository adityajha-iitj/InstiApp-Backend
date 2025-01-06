package in.ac.iitj.instiapp.Repository;

//post grievance with form data
//get grievance with the parameter username
//delete grievance
//update grievance
// username title object
import in.ac.iitj.instiapp.database.entities.Grievance;
import in.ac.iitj.instiapp.payload.GrievanceDto;
import org.springframework.data.domain.Pageable;


import java.util.List;
import java.util.Optional;


public interface GrievanceRepository {

    public Long existGrievance(String publicId);
    public void save(Grievance grievance);
    public List<GrievanceDto> getGrievancesByFilter(Optional<String> title, Optional<String> description,Optional<String> organisationName, Optional<Boolean> resolved,Pageable pageable);
    public void deleteGrievance(String publicId);
    public void updateGrievance(String publicId, Grievance grievanceDto);
    public GrievanceDto getGrievance(String publicId);
}
