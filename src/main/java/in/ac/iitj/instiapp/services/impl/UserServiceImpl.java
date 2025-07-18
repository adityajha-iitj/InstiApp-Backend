package in.ac.iitj.instiapp.services.impl;

import in.ac.iitj.instiapp.Repository.CalendarRepository;
import in.ac.iitj.instiapp.Repository.MediaRepository;
import in.ac.iitj.instiapp.database.entities.Media.Media;
import jakarta.persistence.EntityManager;
import in.ac.iitj.instiapp.Repository.OAuth2TokenRepository;
import in.ac.iitj.instiapp.Repository.User.Organisation.OrganisationRoleRepository;
import in.ac.iitj.instiapp.Repository.UserRepository;
import in.ac.iitj.instiapp.controllers.ValidationUtil;
import in.ac.iitj.instiapp.database.entities.User.User;
import in.ac.iitj.instiapp.database.entities.User.Usertype;
import in.ac.iitj.instiapp.mappers.User.UserBaseDtoMapper;
import in.ac.iitj.instiapp.mappers.User.UserDetailedDtoMapper;
import in.ac.iitj.instiapp.payload.Auth.SignupDto;
import in.ac.iitj.instiapp.payload.User.Organisation.OrganisationRoleDto;
import in.ac.iitj.instiapp.payload.User.UserBaseDto;
import in.ac.iitj.instiapp.payload.User.UserDetailedDto;
import in.ac.iitj.instiapp.services.UserService;
import in.ac.iitj.instiapp.services.UtilitiesService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
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
    private final EntityManager entityManager;
    private final MediaRepository mediaRepository;

    public UserServiceImpl(UserRepository userRepository, UserBaseDtoMapper userBaseDtoMapper, UserDetailedDtoMapper userDetailedDtoMapper,
                           OrganisationRoleRepository organisationRoleRepository, UtilitiesService utilitiesService, ValidationUtil validationUtil, CalendarRepository calendarRepository,
                           OAuth2TokenRepository oAuth2TokenRepository, EntityManager entityManager, MediaRepository mediaRepository) {
        this.userRepository = userRepository;
        this.userBaseDtoMapper = userBaseDtoMapper;
        this.userDetailedDtoMapper = userDetailedDtoMapper;
        this.organisationRoleRepository = organisationRoleRepository;
        this.utilitiesService = utilitiesService;
        this.validationUtil = validationUtil;
        this.calendarRepository = calendarRepository;
        this.oAuth2TokenRepository = oAuth2TokenRepository;
        this.entityManager = entityManager;
        this.mediaRepository = mediaRepository;
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
    public Long save(@Valid SignupDto signupDto) throws DataIntegrityViolationException, EmptyResultDataAccessException {
        User u = new User();

        // Get directly from signupDto
        u.setName(signupDto.getName());
        u.setEmail(validationUtil.validateEmail(signupDto.getEmail()));
        u.setPassword(signupDto.getPassword());
        u.setAvatarUrl(signupDto.getAvatarUrl());
        u.setUserName(signupDto.getUsername());
        u.setUserType(signupDto.getUserType());

        // Check for duplicate email
        if (userRepository.emailExists(u.getEmail()) != -1L) {
            throw new DataIntegrityViolationException("Email already exists");
        }

        // Validate user type
        Long userTypeId = userRepository.userTypeExists(signupDto.getUserType().getName());
        if (userTypeId == -1L) {
            throw new EmptyResultDataAccessException("User Type doesn't exist in database", 1);
        }

        // ✅ Load the user type entity correctly (not a dummy with null ID)
        Usertype userTypeEntity = entityManager.getReference(Usertype.class, userTypeId); // or a proper findById
        u.setUserType(userTypeEntity);


        try {
            return userRepository.save(u);
        } catch (Exception e) {
            log.error("Error saving user", e);
            throw e;
        }

    }

    public String createUsername(String firstName, String lastName, String email) {
        String base = String.format("%s.%s.%s",
                firstName,
                lastName,
                email.substring(0, email.indexOf('@'))
        ).toLowerCase().replaceAll("[^a-z0-9.]", "");

        String username;
        do {
            int suffix = ThreadLocalRandom.current().nextInt(1000, 10000);
            username = base + suffix;
        }  while (userRepository.usernameExists(username) > -1L);
            // Check for uniqueness in DB

        return username;
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
    public UserDetailedDto getUserDetailed(String username) {
        UserDetailedDto userDetailedDto = userRepository.getUserDetailed(username);
        userDetailedDto.setOrganisationRoleSet(userRepository.getOrganisationRoleDTOsByUsername(username, PageRequest.of(0,10)));
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


    public String generateJwtToken(Long userId, String email) {
        // Build JWT with your secret (HS256)
        return Jwts.builder()
                .setSubject(email)
                .claim("userId", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day
                .signWith(SignatureAlgorithm.HS256, "your_jwt_secret") // Use env/config
                .compact();
    }

    public String getUsernameFromEmail(String email){
        return userRepository.getUserNameFromEmail(email);
    }

    public Long updateUserDetails(UserDetailedDto userDetailedDto){
        User updatedUser = userDetailedDtoMapper.toUser(userDetailedDto);
        updatedUser.setId(userRepository.getUserIdFromUsername(userDetailedDto.getUserName()));
        User user = userRepository.getUserFromUsername(userDetailedDto.getUserName());

        updatedUser.setUserType(user.getUserType());

        Media media = new Media();
        media.setPublicUrl(userDetailedDto.getAvatarUrl());
        mediaRepository.save(media);

        return userRepository.save(updatedUser);
    }



}
