package in.ac.iitj.instiapp.services.impl;

import in.ac.iitj.instiapp.Repository.GrievanceRepository;
import in.ac.iitj.instiapp.Repository.MediaRepository;
import in.ac.iitj.instiapp.Repository.OrganisationRoleRepository;
import in.ac.iitj.instiapp.Repository.User.Organisation.OrganisationRepository;
import in.ac.iitj.instiapp.Repository.UserRepository;
import in.ac.iitj.instiapp.database.entities.Grievance;
import in.ac.iitj.instiapp.database.entities.User.Organisation.OrganisationRole;
import in.ac.iitj.instiapp.database.entities.User.User;
import in.ac.iitj.instiapp.payload.GrievanceDto;
import in.ac.iitj.instiapp.payload.Media.MediaBaseDto;
import in.ac.iitj.instiapp.payload.User.Organisation.OrganisationRoleDto;
import in.ac.iitj.instiapp.payload.User.UserBaseDto;
import in.ac.iitj.instiapp.services.GrievanceService;

import in.ac.iitj.instiapp.mappers.GrievanceDtoMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class GrievanceServiceImpl implements GrievanceService {

    private final GrievanceRepository grievanceRepository;
    private final MediaRepository mediaRepository;
    private final UserRepository userRepository;
    private final GrievanceDtoMapper grievanceDtoMapper;
    private final OrganisationRepository organisationRepository;
    private final OrganisationRoleRepository organisationRoleRepository;


    @Autowired
    public GrievanceServiceImpl(GrievanceRepository grievanceRepository, MediaRepository mediaRepository, UserRepository userRepository, GrievanceDtoMapper grievanceDtoMapper, OrganisationRepository organisationRepository, OrganisationRoleRepository organisationRoleRepository) {
        this.grievanceRepository = grievanceRepository;
        this.mediaRepository = mediaRepository;
        this.userRepository = userRepository;
        this.grievanceDtoMapper = grievanceDtoMapper;
        this.organisationRepository = organisationRepository;
        this.organisationRoleRepository = organisationRoleRepository;
    }

    @Override
    public Long existGrievance(String publicId){
        return grievanceRepository.existGrievance(publicId);
    }

    @Override
    @Transactional
    public String save(GrievanceDto grievanceDto){
        Grievance grievance = grievanceDtoMapper.toGrievance(grievanceDto);
        String orgUsername = grievanceDto.getOrganisationRole().getOrganisationUsername();

        String userName = grievanceDto.getUserFrom().getUserName();
        User user = userRepository.getUserFromUsername(userName);
        grievance.setUserFrom(user);

        grievance.setOrganisationRole(organisationRoleRepository.getOrganisationRoleByUsername(orgUsername));
        return grievanceRepository.save(grievance);
    }

    @Override
    public GrievanceDto getGrievance(String publicId){
        GrievanceDto grievanceDto = grievanceRepository.getGrievance(publicId);
        UserBaseDto userBaseDto = userRepository.getUserLimited(grievanceDto.getUserFrom().getUserName());
        MediaBaseDto mediaBaseDto = mediaRepository.findByPublicId(grievanceDto.getMedia().getPublicUrl());

        grievanceDto.setUserFrom(userBaseDto);
        grievanceDto.setMedia(mediaBaseDto);

        return grievanceDto;

    }

    @Transactional
    @Override
    public void updateGrievance(String publicId, GrievanceDto grievanceDto){
        Grievance grievance = grievanceDtoMapper.toGrievance(grievanceDto);
        String orgUsername = grievanceDto.getOrganisationRole().getOrganisationUsername();

        String userName = grievanceDto.getUserFrom().getUserName();
        User user = userRepository.getUserFromUsername(userName);
        grievance.setUserFrom(user);

        grievance.setOrganisationRole(organisationRoleRepository.getOrganisationRoleByUsername(orgUsername));
        grievanceRepository.updateGrievance(publicId, grievance);
    }

    @Override
    public List<GrievanceDto> getGrievancesByFilter(Optional<String> title, Optional<String> description, Optional<String> organisationName, Optional<Boolean> resolved, Pageable pageable){
        return grievanceRepository.getGrievancesByFilter(title, description, organisationName, resolved, pageable);
    }

    @Transactional
    @Override
    public void deleteGrievance(String publicId){
        grievanceRepository.deleteGrievance(publicId);
    }


    public boolean doesOwn(String publicId, String username)
    {
        return grievanceRepository.doesOwn(publicId,username);
    }


}
