package in.ac.iitj.instiapp.services.impl;

import in.ac.iitj.instiapp.Repository.CalendarRepository;
import in.ac.iitj.instiapp.Repository.OAuth2TokenRepository;
import in.ac.iitj.instiapp.controllers.ValidationUtil;
import in.ac.iitj.instiapp.database.entities.Auth.OAuth2Tokens;
import in.ac.iitj.instiapp.database.entities.Scheduling.Calendar.Calendar;
import in.ac.iitj.instiapp.database.entities.User.Organisation.OrganisationRole;
import in.ac.iitj.instiapp.database.entities.User.User;
import in.ac.iitj.instiapp.payload.Auth.SignupDto;
import in.ac.iitj.instiapp.payload.User.Organisation.OrganisationRoleDto;
import in.ac.iitj.instiapp.payload.User.UserBaseDto;
import in.ac.iitj.instiapp.payload.User.UserDetailedDto;
import in.ac.iitj.instiapp.Repository.UserRepository;
import in.ac.iitj.instiapp.mappers.User.UserBaseDtoMapper;
import in.ac.iitj.instiapp.mappers.User.UserDetailedDtoMapper;
import in.ac.iitj.instiapp.database.entities.User.Usertype;
import in.ac.iitj.instiapp.Repository.User.Organisation.OrganisationRoleRepository;
import in.ac.iitj.instiapp.services.UserService;
import in.ac.iitj.instiapp.services.UtilitiesService;
import io.jsonwebtoken.Claims;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.ValidationUtils;

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
    private final UtilitiesService utilitiesService;
    private final CalendarRepository calendarRepository;
    private final  ValidationUtil  validationUtil ;
    private final OAuth2TokenRepository oAuth2TokenRepository;

    public UserServiceImpl(UserRepository userRepository, UserBaseDtoMapper userBaseDtoMapper, UserDetailedDtoMapper userDetailedDtoMapper,
                           OrganisationRoleRepository organisationRoleRepository,UtilitiesService utilitiesService, ValidationUtil validationUtil, CalendarRepository calendarRepository,
                           OAuth2TokenRepository oAuth2TokenRepository) {
        this.userRepository = userRepository;
        this.userBaseDtoMapper = userBaseDtoMapper;
        this.userDetailedDtoMapper = userDetailedDtoMapper;
        this.organisationRoleRepository = organisationRoleRepository;
        this.utilitiesService = utilitiesService;
        this.validationUtil = validationUtil;
        this.calendarRepository = calendarRepository;
        this.oAuth2TokenRepository = oAuth2TokenRepository;
    }

    public Long save(UserBaseDto userBaseDto) {
        User user = userBaseDtoMapper.toUser(userBaseDto);
        return userRepository.save(user);
    }

    public Long save(UserDetailedDto userDetailedDto) {
        User user = userDetailedDtoMapper.toUser(userDetailedDto);
        return userRepository.save(user);
    }

    @Override
    public String save(@Valid  SignupDto signupDto, Claims claim) throws DataIntegrityViolationException, EmptyResultDataAccessException , ConstraintViolationException {




//        =========================   Saving User and oauth2 Tokens and also validating them ========================================
        User u = new User();

        u.setName( validationUtil.validateString( claim.get("name").toString(), 12, 500));
        u.setEmail(validationUtil.validateEmail(claim.get("email").toString()));
        u.setAvatarUrl(validationUtil.validateString(claim.get("avatarUrl").toString(), 12, 500));
        String userName = utilitiesService.generateRandom(claim.get("name").toString());
        u.setUserName(userName);

        if(userRepository.emailExists(u.getEmail())){
            throw new DataIntegrityViolationException("Email already exists");
        }

        Long id = userRepository.userTypeExists(signupDto.getUserTypeName());
        if(id == -1L){
            throw  new EmptyResultDataAccessException("User Type Doesn't exists in database",1);
        }

        String calendarPublicId = utilitiesService.generateRandom(claim.get("name").toString() + "calendar" );
        calendarRepository.save(new Calendar(calendarPublicId));
        Long idOfCalendar= calendarRepository.calendarExists(calendarPublicId);

        u.setCalendar(new Calendar(idOfCalendar));



        // Setting OAuth2Token Data
        OAuth2Tokens oAuth2Tokens = new OAuth2Tokens();
        oAuth2Tokens.setAccessToken(validationUtil.validateString(claim.get("oauth2AccessToken").toString(),12, 3000));
        oAuth2Tokens.setRefreshToken(validationUtil.validateString(claim.get("oauth2RefreshToken").toString(),12, 3000));
        oAuth2Tokens.setDeviceId(validationUtil.validateString(claim.get("deviceId").toString(),12, 3000));


        // Saving user
        Long idOfUser =  userRepository.save(u);

        // Saving ouath2Token
        User newUser = new User(idOfUser);
        newUser.setUserName(userName);
        oAuth2Tokens.setUser(u);
        oAuth2TokenRepository.save(oAuth2Tokens);

        return userName;
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
