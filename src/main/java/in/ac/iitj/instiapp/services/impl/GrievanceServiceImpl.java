package in.ac.iitj.instiapp.services.impl;

import in.ac.iitj.instiapp.Repository.GrievanceRepository;
import in.ac.iitj.instiapp.Repository.MediaRepository;
import in.ac.iitj.instiapp.Repository.UserRepository;
import in.ac.iitj.instiapp.database.entities.Grievance;
import in.ac.iitj.instiapp.database.entities.User.Organisation.OrganisationRole;
import in.ac.iitj.instiapp.payload.GrievanceDto;
import in.ac.iitj.instiapp.payload.Media.MediaBaseDto;
import in.ac.iitj.instiapp.payload.User.Organisation.OrganisationRoleDto;
import in.ac.iitj.instiapp.payload.User.UserBaseDto;
import in.ac.iitj.instiapp.services.GrievanceService;

import in.ac.iitj.instiapp.mappers.GrievanceDtoMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GrievanceServiceImpl implements GrievanceService {

    private final GrievanceRepository grievanceRepository;
    private final MediaRepository mediaRepository;
    private final UserRepository userRepository;
    private final GrievanceDtoMapper grievanceDtoMapper;


    @Autowired
    public GrievanceServiceImpl(GrievanceRepository grievanceRepository, MediaRepository mediaRepository, UserRepository userRepository, GrievanceDtoMapper grievanceDtoMapper) {
        this.grievanceRepository = grievanceRepository;
        this.mediaRepository = mediaRepository;
        this.userRepository = userRepository;
        this.grievanceDtoMapper = grievanceDtoMapper;
    }

    @Override
    public Long existGrievance(String publicId){
        return grievanceRepository.existGrievance(publicId);
    }

    @Override
    public void save(GrievanceDto grievanceDto){
        Grievance grievance = grievanceDtoMapper.toGrievance(grievanceDto);
        grievanceRepository.save(grievance);
    }

    @Override
    public GrievanceDto getGrievance(String publicId){
        GrievanceDto grievanceDto = grievanceRepository.getGrievance(publicId);
        UserBaseDto userBaseDto = userRepository.getUserLimited(grievanceDto.getUserFrom().getUserName());
        MediaBaseDto mediaBaseDto = mediaRepository.findByPublicId(grievanceDto.getMedia().getPublicId());

        grievanceDto.setUserFrom(userBaseDto);
        grievanceDto.setMedia(mediaBaseDto);

        return grievanceDto;

    }

    @Override
    public void updateGrievance(String publicId){
        GrievanceDto grievanceDto = grievanceRepository.getGrievance(publicId);
        Grievance grievance = grievanceDtoMapper.toGrievance(grievanceDto);
        grievanceRepository.updateGrievance(publicId, grievance);
    }

    @Override
    public List<GrievanceDto> getGrievancesByFilter(Optional<String> title, Optional<String> description, Optional<String> organisationName, Optional<Boolean> resolved, Pageable pageable){
        return grievanceRepository.getGrievancesByFilter(title, description, organisationName, resolved, pageable);
    }

    @Override
    public void deleteGrievance(String publicId){
        grievanceRepository.deleteGrievance(publicId);
    }


}
