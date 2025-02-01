package in.ac.iitj.instiapp.Tests.Service;

import in.ac.iitj.instiapp.Repository.User.Organisation.OrganisationRoleRepository;
import in.ac.iitj.instiapp.Repository.UserRepository;
import in.ac.iitj.instiapp.database.entities.User.Organisation.OrganisationPermission;
import in.ac.iitj.instiapp.database.entities.User.User;
import in.ac.iitj.instiapp.database.entities.User.Usertype;
import in.ac.iitj.instiapp.mappers.User.UserBaseDtoMapper;
import in.ac.iitj.instiapp.mappers.User.UserDetailedDtoMapper;
import in.ac.iitj.instiapp.payload.User.Organisation.OrganisationRoleDto;
import in.ac.iitj.instiapp.payload.User.UserBaseDto;
import in.ac.iitj.instiapp.payload.User.UserDetailedDto;
import in.ac.iitj.instiapp.services.UserService;
import in.ac.iitj.instiapp.services.impl.UserServiceImpl;
import jakarta.inject.Inject;
import org.hibernate.usertype.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserBaseDtoMapper userBaseDtoMapper;

    @Mock
    private UserDetailedDtoMapper userDetailedDtoMapper;

    @Mock
    private OrganisationRoleRepository organisationRoleRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks and inject them
    }

    @Test
    @Order(1)
    public void getAllUserTypes() {
        Usertype userTypeName1 = new Usertype(1L,"Admin");
        Usertype userTypeName2 = new Usertype(2L,"Student");
        Usertype userTypeName3 = new Usertype(3L,"Faculty");
        Usertype userTypeName4 = new Usertype(4L,"Organisation");

        List<String> usertypeList = Arrays.asList(userTypeName1.getName(), userTypeName2.getName(), userTypeName3.getName(), userTypeName4.getName());
        Pageable pageable = PageRequest.of(0,10);

        // Mock the repository call
        when(userRepository.getAllUserTypes(pageable)).thenReturn(usertypeList);

        // Call the service method
        List<String> result = userService.getAllUserTypes(pageable);

        // Verify interactions and assert results
        verify(userRepository, times(1)).getAllUserTypes(pageable);
        assertNotNull(result);
        assertEquals(4, result.size());
        assertEquals("Admin", result.get(0));
        assertEquals("Student", result.get(1));
        assertEquals("Faculty", result.get(2));
        assertEquals("Organisation", result.get(3));

    }

    @Test
    @Order(2)
    public void testUserTypeExists() {
        Usertype userTypeName1 = new Usertype(1L,"Admin");

        String name1 = userTypeName1.getName();
        String name2 = "The King";
        when(userRepository.userTypeExists(name1)).thenReturn(1L);
        when(userRepository.userTypeExists(name2)).thenReturn(-1L);

        Long result1 = userService.userTypeExists(name1);
        Long result2 = userService.userTypeExists(name2);

        verify(userRepository, times(1)).userTypeExists(name1);
        verify(userRepository, times(1)).userTypeExists(name2);
        assertNotNull(result1);
        assertNotNull(result2);
        assertEquals(1L, result1);
        assertEquals(-1L, result2);
    }

    @Test
    @Order(3)
    public void testUserTypeUpdate() {
        String oldName = "Student";
        String newName = "Scholar";
        Pageable pageable = PageRequest.of(0, 10);

        // Mock existence check before update
        when(userRepository.userTypeExists(oldName)).thenReturn(1L);
        when(userRepository.userTypeExists(newName)).thenReturn(-1L);

        // Perform the update
        userService.userTypeUpdate(oldName, newName);

        // Verify update method was called
        verify(userRepository, times(1)).update(oldName, newName);

        // Mock the getAllUserTypes method after update
        List<String> updatedUserTypes = Arrays.asList("Admin", "Scholar", "Faculty", "Organisation");
        when(userRepository.getAllUserTypes(pageable)).thenReturn(updatedUserTypes);

        // Fetch updated user types
        List<String> result = userService.getAllUserTypes(pageable);

        // Verify interactions and assert results
        verify(userRepository, times(1)).getAllUserTypes(pageable);
        assertNotNull(result);
        assertEquals(4, result.size());
        assertFalse(result.contains(oldName)); // Old name should not exist
        assertTrue(result.contains(newName));  // New name should exist
    }

    @Test
    @Order(4)
    public void testUserTypeDelete() {
        String userTypeName = "Student";

        // Mock userTypeExists to simulate that the user type exists
        when(userRepository.userTypeExists(userTypeName)).thenReturn(1L);

        // Call the delete method
        userService.userTypeDelete(userTypeName);

        // Verify that the delete method was called exactly once
        verify(userRepository, times(1)).delete(userTypeName);

        // Mock getAllUserTypes to simulate the updated list without "Student"
        List<String> updatedUserTypes = Arrays.asList("Admin", "Faculty", "Organisation");
        Pageable pageable = PageRequest.of(0, 10);
        when(userRepository.getAllUserTypes(pageable)).thenReturn(updatedUserTypes);

        // Fetch updated user types and verify "Student" is removed
        List<String> result = userService.getAllUserTypes(pageable);
        assertNotNull(result);
        assertEquals(3, result.size());
        assertFalse(result.contains(userTypeName));
    }

    @Test
    @Order(5)
    public void testGetUserLimited() {
        String username = "john_doe";

        // Create a mock UserBaseDto object
        UserBaseDto mockUser = new UserBaseDto("John Doe", "john_doe", "john.doe@example.com", "Student", "avatar_url");

        // Mock the repository call
        when(userRepository.getUserLimited(username)).thenReturn(mockUser);

        // Call the service method
        UserBaseDto result = userService.getUserLimited(username);

        // Verify interactions and assert results
        verify(userRepository, times(1)).getUserLimited(username);
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("john_doe", result.getUserName());
        assertEquals("john.doe@example.com", result.getEmail());
        assertEquals("Student", result.getUserTypeName());
        assertEquals("avatar_url", result.getAvatarUrl());
    }

    @Test
    @Order(6)
    public void testGetUserDetailed() {
        String username = "john_doe";
        boolean isPrivate = true;
        Pageable pageable = mock(Pageable.class);

        // Mock the UserDetailedDto returned by userRepository
        UserDetailedDto mockUser = new UserDetailedDto("John Doe", "john_doe", "john.doe@example.com", "Student", "avatar_url");

        // Mock the OrganisationRoleDto list
        OrganisationRoleDto role1 = new OrganisationRoleDto("org1", "Manager", OrganisationPermission.MASTER);
        OrganisationRoleDto role2 = new OrganisationRoleDto("org2", "Member", OrganisationPermission.READ);
        Set<OrganisationRoleDto> mockRoles = Set.of(role1, role2);

        // Mock repository behavior
        when(userRepository.getUserDetailed(username, isPrivate)).thenReturn(mockUser);
        when(userRepository.getOrganisationRoleDTOsByUsername(username, pageable)).thenReturn(mockRoles);

        // Call the service method
        UserDetailedDto result = userService.getUserDetailed(username, isPrivate, pageable);

        // Verify interactions and assert results
        verify(userRepository, times(1)).getUserDetailed(username, isPrivate);
        verify(userRepository, times(1)).getOrganisationRoleDTOsByUsername(username, pageable);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("john_doe", result.getUserName());
        assertEquals("john.doe@example.com", result.getEmail());
        assertEquals("Student", result.getUserTypeName());
        assertEquals("avatar_url", result.getAvatarUrl());
        assertEquals(mockRoles.size(), result.getOrganisationRoleSet().size());
        assertTrue(result.getOrganisationRoleSet().contains(role1));
        assertTrue(result.getOrganisationRoleSet().contains(role2));
    }

    @Test
    @Order(7)
    public void testGetListUserLimitedByUsertype() {
        String userType = "Student";
        Pageable pageable = mock(Pageable.class);

        // Mock user list
        UserBaseDto user1 = new UserBaseDto("John Doe", "john_doe", "john.doe@example.com", "Student", "avatar1_url");
        UserBaseDto user2 = new UserBaseDto("Jane Doe", "jane_doe", "jane.doe@example.com", "Student", "avatar2_url");
        UserBaseDto user3 = new UserBaseDto("Jane Doe", "jane_doe", "jane.doe@example.com", "Faculty", "avatar23_url");
        List<UserBaseDto> mockUserList = List.of(user1, user2);

        // Mock repository behavior
        when(userRepository.getListUserLimitedByUsertype(userType, pageable)).thenReturn(mockUserList);

        // Call the service method
        List<UserBaseDto> result = userService.getListUserLimitedByUsertype(userType, pageable);

        // Verify interactions
        verify(userRepository, times(1)).getListUserLimitedByUsertype(userType, pageable);

        // Assertions
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals("jane_doe", result.get(1).getUserName());
        assertEquals("Student", result.get(0).getUserTypeName());
        assertEquals("Student", result.get(1).getUserTypeName());
    }

    @Test
    @Order(8)
    public void testGetOrganisationPermission() {
        String username = "john_doe";
        String organisationUsername = "org_xyz";

        OrganisationRoleDto mockRole = new OrganisationRoleDto(organisationUsername, "Admin", OrganisationPermission.MASTER);
        Optional<OrganisationRoleDto> mockOptionalRole = Optional.of(mockRole);

        when(userRepository.getOrganisationPermission(username, organisationUsername)).thenReturn(mockOptionalRole);

        Optional<OrganisationRoleDto> result = userService.getOrganisationPermission(username, organisationUsername);

        verify(userRepository, times(1)).getOrganisationPermission(username, organisationUsername);

        assertTrue(result.isPresent());
        assertEquals("org_xyz", result.get().getOrganisationUsername());
        assertEquals("Admin", result.get().getRoleName());
        assertEquals(OrganisationPermission.MASTER, result.get().getPermission());
    }

    @Test
    @Order(9)
    public void testGetOrganisationRoleDTOsByUsername() {
        String username = "john_doe";
        Pageable pageable = mock(Pageable.class);

        // Create mock OrganisationRoleDto objects
        OrganisationRoleDto role1 = new OrganisationRoleDto("org1", "Admin", OrganisationPermission.MASTER);
        OrganisationRoleDto role2 = new OrganisationRoleDto("org2", "Manager", OrganisationPermission.INTERMEDIATE);

        // Create a set of mock roles
        Set<OrganisationRoleDto> mockRoles = Set.of(role1, role2);

        // Mock repository behavior
        when(userRepository.getOrganisationRoleDTOsByUsername(username, pageable)).thenReturn(mockRoles);

        // Call the service method
        Set<OrganisationRoleDto> result = userService.getOrganisationRoleDTOsByUsername(username, pageable);

        // Verify interactions and assert results
        verify(userRepository, times(1)).getOrganisationRoleDTOsByUsername(username, pageable);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(role1));
        assertTrue(result.contains(role2));
        assertEquals("org1", role1.getOrganisationUsername());
        assertEquals("Admin", role1.getRoleName());
        assertEquals(OrganisationPermission.MASTER, role1.getPermission());
    }

    @Test
    @Order(10)
    public void testUsernameExists() {
        String username = "john_doe";

        // Mocking the behavior of userRepository.usernameExists
        Long mockId = 123L;
        when(userRepository.usernameExists(username)).thenReturn(mockId);

        // Call the service method
        Long result = userService.usernameExists(username);

        // Verify interactions and assert results
        verify(userRepository, times(1)).usernameExists(username);

        assertNotNull(result);
        assertEquals(mockId, result);
    }







}
