package in.ac.iitj.instiapp.services.impl;

import in.ac.iitj.instiapp.Repository.User.Organisation.OrganisationRepository;
import in.ac.iitj.instiapp.database.entities.User.Faculty.Faculty;
import in.ac.iitj.instiapp.payload.User.Faculty.FacultyDetailedDto;
import in.ac.iitj.instiapp.payload.User.Faculty.FacultyBaseDto;
import in.ac.iitj.instiapp.Repository.FacultyRepository;
import in.ac.iitj.instiapp.mappers.User.Faculty.FacultyDtoMapper;
import in.ac.iitj.instiapp.payload.User.Organisation.OrganisationBaseDto;
import in.ac.iitj.instiapp.payload.User.UserBaseDto;
import in.ac.iitj.instiapp.payload.User.UserDetailedDto;
import in.ac.iitj.instiapp.services.FacultyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import in.ac.iitj.instiapp.Repository.User.Organisation.OrganisationRepository;
import in.ac.iitj.instiapp.Repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FacultyServiceImpl implements FacultyService {


    private final FacultyRepository facultyRepository;
    private final FacultyDtoMapper facultyDtoMapper;
    private final OrganisationRepository organisationRepository;
    private final UserRepository userRepository;

    // private final OrganisationRepository organisationRepository;

    @Autowired
    public FacultyServiceImpl(FacultyRepository facultyRepository, FacultyDtoMapper facultyDtoMapper, OrganisationRepository organisationRepository, UserRepository userRepository) {
        this.facultyRepository = facultyRepository;
        this.facultyDtoMapper = facultyDtoMapper;
        this.organisationRepository = organisationRepository;
        this.userRepository = userRepository;
    }


    @Override
    public void save(FacultyDetailedDto facultyDetailedDto) {
        Faculty faculty = facultyDtoMapper.toFaculty(facultyDetailedDto);
        facultyRepository.save(faculty);
    }

    @Override
    public void save(FacultyBaseDto facultyBaseDto) {
        Faculty faculty = facultyDtoMapper.toFaculty(facultyBaseDto);
        facultyRepository.save(faculty);
    }

    @Override
    public Long facultyExists(String userName){
        return facultyRepository.facultyExists(userName);
    }

    @Override
    public FacultyBaseDto getFaculty(String userName){
        FacultyBaseDto facultyBaseDto = facultyRepository.getFaculty(userName);
        OrganisationBaseDto organisationBaseDto = organisationRepository.getOrganisation(facultyBaseDto.getOrganisation().getUser().getUserName());
        UserBaseDto userBaseDto = userRepository.getUserLimited(userName);

        organisationBaseDto.setUser(facultyBaseDto.getOrganisation().getUser());


        facultyBaseDto.setOrganisation(organisationBaseDto);
        facultyBaseDto.setUser(userBaseDto);

        return facultyBaseDto;
    }

    @Override
    public FacultyDetailedDto getDetailedFaculty(String userName, boolean isPrivate){
        FacultyDetailedDto facultyDetailedDto = facultyRepository.getDetailedFaculty(userName);
        UserDetailedDto userDetailedDto = userRepository.getUserDetailed(userName, isPrivate);
        OrganisationBaseDto organisationBaseDto = organisationRepository.getOrganisation(facultyDetailedDto.getOrganisation().getUser().getUserName());

        organisationBaseDto.setUser(facultyDetailedDto.getOrganisation().getUser());

        facultyDetailedDto.setUser(userDetailedDto);
        facultyDetailedDto.setOrganisation(organisationBaseDto);

        return facultyDetailedDto;

    }

    @Override
    public void updateFaculty(FacultyDetailedDto facultyDetailedDto){
        Faculty faculty = facultyDtoMapper.toFaculty(facultyDetailedDto);
        facultyRepository.updateFaculty(faculty);
    }

    @Override
    public List<FacultyBaseDto> getFacultyByFilter(Optional<String> organisationName, Optional<String> description, Optional<String> websiteUrl, Pageable pageable){
        return facultyRepository.getFacultyByFilter(organisationName, description, websiteUrl, pageable);

    }

    @Override
    public void deleteFaculty(String userName){
        facultyRepository.deleteFaculty(userName);
    }

}
