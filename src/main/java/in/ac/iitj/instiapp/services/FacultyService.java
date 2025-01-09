package in.ac.iitj.instiapp.services;

import in.ac.iitj.instiapp.database.entities.User.Faculty.Faculty;
import in.ac.iitj.instiapp.payload.User.Faculty.FacultyDetailedDto;
import in.ac.iitj.instiapp.payload.User.Faculty.FacultyBaseDto;
import in.ac.iitj.instiapp.Repository.FacultyRepository;
import in.ac.iitj.instiapp.mappers.User.Faculty.FacultyDtoMapper;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


public interface FacultyService {

    public void facultyExists(String userName);
    public void save(FacultyDetailedDto facultyDetailedDto);
    public FacultyBaseDto getFaculty(String userName);
    public FacultyDetailedDto getDetailedFaculty(String userName, boolean isPrivate);
    public void updateFaculty(FacultyBaseDto facultyBaseDto);
    public List<FacultyBaseDto> getFacultyByFilter(Optional<String> organisationName, Optional<String> description, Optional<String> websiteUrl, Pageable pageable);
    public void deleteFaculty(String userName);


}
