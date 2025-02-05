package in.ac.iitj.instiapp.services.impl;

import in.ac.iitj.instiapp.database.entities.User.Organisation.OrganisationRole;
import in.ac.iitj.instiapp.database.entities.User.User;
import in.ac.iitj.instiapp.payload.User.Organisation.OrganisationRoleDto;
import in.ac.iitj.instiapp.payload.User.UserBaseDto;
import in.ac.iitj.instiapp.payload.User.UserDetailedDto;
import in.ac.iitj.instiapp.Repository.UserRepository;
import in.ac.iitj.instiapp.mappers.User.UserBaseDtoMapper;
import in.ac.iitj.instiapp.mappers.User.UserDetailedDtoMapper;
import in.ac.iitj.instiapp.database.entities.User.Usertype;
import in.ac.iitj.instiapp.Repository.User.Organisation.OrganisationRoleRepository;
import in.ac.iitj.instiapp.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserBaseDtoMapper userBaseDtoMapper;
    private final UserDetailedDtoMapper userDetailedDtoMapper;
    private final OrganisationRoleRepository organisationRoleRepository;



    public UserServiceImpl(UserRepository userRepository, UserBaseDtoMapper userBaseDtoMapper, UserDetailedDtoMapper userDetailedDtoMapper, OrganisationRoleRepository organisationRoleRepository) {
        this.userRepository = userRepository;
        this.userBaseDtoMapper = userBaseDtoMapper;
        this.userDetailedDtoMapper = userDetailedDtoMapper;
        this.organisationRoleRepository = organisationRoleRepository;
    }

    public Long save(UserBaseDto userBaseDto) {
        User user = userBaseDtoMapper.toUser(userBaseDto);
        return userRepository.save(user);
    }

    public Long save(UserDetailedDto userDetailedDto) {
        User user = userDetailedDtoMapper.toUser(userDetailedDto);
        return userRepository.save(user);
    }

    public void save(Usertype usertype){
        userRepository.save(usertype);
    }

    public List<String> getAllUserTypes(Pageable pageable){
        return userRepository.getAllUserTypes(pageable);
    }

    public Long userTypeExists(String name){
        return userRepository.userTypeExists(name);
    }

    public void userTypeUpdate(String oldName,  String newName){
        userRepository.update(oldName, newName);
    }

    public void userTypeDelete(String userTypeName){
        userRepository.delete(userTypeName);
    }

    public UserBaseDto getUserLimited(String username){
        return userRepository.getUserLimited(username);
    }

    public UserDetailedDto getUserDetailed(String username, boolean isPrivate, Pageable pageable){
         UserDetailedDto userDetailedDto = userRepository.getUserDetailed(username, isPrivate);
         userDetailedDto.setOrganisationRoleSet(userRepository.getOrganisationRoleDTOsByUsername(username,pageable));
         return userDetailedDto;
    }

    public List<UserBaseDto> getListUserLimitedByUsertype(String usertype, Pageable pageable){
        return userRepository.getListUserLimitedByUsertype(usertype, pageable);
    }

    public Optional<OrganisationRoleDto> getOrganisationPermission(String username, String organisationUsername){
        return userRepository.getOrganisationPermission(username, organisationUsername);
    }

    public Set<OrganisationRoleDto> getOrganisationRoleDTOsByUsername(String username, Pageable pageable){
        return userRepository.getOrganisationRoleDTOsByUsername(username, pageable);
    }

    public Long usernameExists(String username){
        return userRepository.usernameExists(username);
    }

    public void updateOauth2Info(String newName, String avatarURL, String userName){
        userRepository.updateOauth2Info(newName, avatarURL, userName);
    }

    public void setUserType(String username, String newUserType){
        userRepository.setUserType(username, newUserType);
    }

    public void updatePhoneNumber(String username, String newPhoneNumber){
        userRepository.updatePhoneNumber(username, newPhoneNumber);
    }

    public void delete(String userTypeName){
        userRepository.delete(userTypeName);
    }

}
