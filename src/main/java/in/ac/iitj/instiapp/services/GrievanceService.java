package in.ac.iitj.instiapp.services;

import in.ac.iitj.instiapp.payload.GrievanceDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface GrievanceService {

    public Long existGrievance(String publicId);
    public String save(GrievanceDto grievanceDto);
    public GrievanceDto getGrievance(String publicId);
    public void updateGrievance(String publicId, GrievanceDto grievanceDto);
    public List<GrievanceDto> getGrievancesByFilter(Optional<String> title, Optional<String> description, Optional<String> organisationName, Optional<Boolean> resolved, Pageable pageable);
    public void deleteGrievance(String publicId);

    public boolean doesOwn(String publicId, String username);

}
