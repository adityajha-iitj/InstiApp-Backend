package in.ac.iitj.instiapp.services.impl;

import com.nimbusds.jwt.JWTClaimsSet;
import in.ac.iitj.instiapp.Repository.CalendarRepository;
import in.ac.iitj.instiapp.Repository.OAuth2TokenRepository;
import in.ac.iitj.instiapp.Repository.User.Organisation.OrganisationRoleRepository;
import in.ac.iitj.instiapp.Repository.UserRepository;
import in.ac.iitj.instiapp.controllers.ValidationUtil;
import in.ac.iitj.instiapp.database.entities.Scheduling.Calendar.Calendar;
import in.ac.iitj.instiapp.database.entities.User.User;
import in.ac.iitj.instiapp.database.entities.User.Usertype;
import in.ac.iitj.instiapp.mappers.User.UserBaseDtoMapper;
import in.ac.iitj.instiapp.mappers.User.UserDetailedDtoMapper;
import in.ac.iitj.instiapp.payload.Auth.SignupDto;
import in.ac.iitj.instiapp.payload.User.Organisation.OrganisationRoleDto;
import in.ac.iitj.instiapp.payload.User.UserBaseDto;
import in.ac.iitj.instiapp.payload.User.UserDetailedDto;
import in.ac.iitj.instiapp.services.JWTTokens.JWEConstants;
import in.ac.iitj.instiapp.services.UserService;
import in.ac.iitj.instiapp.services.UtilitiesService;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

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
    @Transactional
    public Pair<String, Long> save(@Valid  SignupDto signupDto, JWTClaimsSet claim) throws DataIntegrityViolationException, EmptyResultDataAccessException , ConstraintViolationException {




//        =========================   Saving User==================================
        User u = new User();

        u.setName( validationUtil.validateString( claim.getClaim(JWEConstants.KEYS_NAME).toString(), 1, 500));
        u.setEmail(validationUtil.validateEmail(claim.getClaim(JWEConstants.KEYS_EMAIL).toString()));
        u.setAvatarUrl(validationUtil.validateString(claim.getClaim(JWEConstants.KEYS_AVATAR).toString(), 12, 500));
        String userName = utilitiesService.generateRandom(claim.getClaim(JWEConstants.KEYS_NAME).toString());
        u.setUserName(userName);

        if(userRepository.emailExists(u.getEmail()) != -1L){
            throw new DataIntegrityViolationException("Email already exists");
        }

        Long id = userRepository.userTypeExists(signupDto.getUserTypeName());
        if(id == -1L){
            throw  new EmptyResultDataAccessException("User Type Doesn't exists in database",1);
        }
        u.setUserType(new Usertype(id));

        String calendarPublicId = utilitiesService.generateRandom(claim.getClaim(JWEConstants.KEYS_NAME).toString() + "calendar" );
        calendarRepository.save(new Calendar(calendarPublicId));
        Long idOfCalendar= calendarRepository.calendarExists(calendarPublicId);

        u.setCalendar(new Calendar(idOfCalendar));

        // Saving user
        Long idOfUser =  userRepository.save(u);

        return Pair.of(userName, idOfUser);
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

    @Override
    public UserDetailedDto getUserDetailed(String email ) {
        UserDetailedDto userDetailedDto = userRepository.getUserDetailed(email);
        userDetailedDto.setOrganisationRoleSet(userRepository.getOrganisationRoleDTOsByUsername(userDetailedDto.getUserName(), PageRequest.of(0,10)));
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

    @Override
    public Long emailExists(String email) {
        return userRepository.emailExists(email);
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
