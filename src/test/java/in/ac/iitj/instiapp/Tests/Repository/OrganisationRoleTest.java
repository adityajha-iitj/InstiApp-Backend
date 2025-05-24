package in.ac.iitj.instiapp.Tests.Repository;

import in.ac.iitj.instiapp.Repository.User.Organisation.OrganisationRepository;
import in.ac.iitj.instiapp.Repository.User.Organisation.OrganisationRoleRepository;
import in.ac.iitj.instiapp.Repository.UserRepository;
import in.ac.iitj.instiapp.Repository.impl.OrganisationRoleRepositoryImpl;
import in.ac.iitj.instiapp.Repository.impl.UserRepositoryImpl;
import in.ac.iitj.instiapp.Tests.EntityTestData.OrganisationRoleData;
import in.ac.iitj.instiapp.Tests.InitialiseEntities.User.InitialiseOrganisationRole;
import in.ac.iitj.instiapp.Tests.Utilities.Utils;
import in.ac.iitj.instiapp.database.entities.User.Organisation.Organisation;
import in.ac.iitj.instiapp.database.entities.User.Organisation.OrganisationRole;
import in.ac.iitj.instiapp.database.entities.User.User;
import in.ac.iitj.instiapp.payload.User.Organisation.OrganisationRoleDto;
import in.ac.iitj.instiapp.payload.User.UserBaseDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static in.ac.iitj.instiapp.Tests.EntityTestData.OrganisationRoleData.*;
import static in.ac.iitj.instiapp.Tests.EntityTestData.UserData.*;

@DataJpaTest
@Import({OrganisationRoleRepositoryImpl.class, InitialiseOrganisationRole.class, UserRepositoryImpl.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrganisationRoleTest {

    private final OrganisationRoleRepository organisationRoleRepository;
    @Autowired
    private OrganisationRoleRepositoryImpl organisationRoleRepositoryImpl;

    private OrganisationRepository organisationRepository;
    private UserRepository userRepository;



    @Autowired
    public OrganisationRoleTest(OrganisationRoleRepository organisationRoleRepository, UserRepository userRepository) {
        this.organisationRoleRepository = organisationRoleRepository;
        this.userRepository = userRepository;
    }

    @BeforeAll
    public static void setUp(@Autowired InitialiseOrganisationRole initialise) {
        initialise.initialise();
    }

    @Test
    @Order(1)
    public void testGetListOrganisationRoles() {


        List<OrganisationRoleDto> organisationRoleDtoList =
                organisationRoleRepository.getOrganisationRoles(USER1.userName, PageRequest.of(0, 10));

        Assertions.assertThat(organisationRoleDtoList.size()).isEqualTo(1);

        if (!organisationRoleDtoList.isEmpty()) {
            Utils.matchOrganisationRoleDto(organisationRoleDtoList.get(0),
                    ORGANISATION_ROLE1, USER1);
        }
    }

    @Test
    @Order(2)
    public void testExistOrganisationRole() {
        Assertions.assertThat(organisationRoleRepository.existOrganisationRole(USER1.userName, ORGANISATION_ROLE1.roleName)).isNotNull().isNotEqualTo(-1L);
        System.out.println(organisationRoleRepository.existOrganisationRole(USER1.userName, ORGANISATION_ROLE1.roleName));
        Assertions.assertThat(organisationRoleRepository.existOrganisationRole(USER2.userName, OrganisationRoleData.ORGANISATION_ROLE2.roleName)).isNotNull().isNotEqualTo(-1L);
        Assertions.assertThat(organisationRoleRepository.existOrganisationRole(USER3.userName, OrganisationRoleData.ORGANISATION_ROLE3.roleName)).isNotNull().isNotEqualTo(-1L);
        Assertions.assertThat(organisationRoleRepository.existOrganisationRole(USER4.userName, OrganisationRoleData.ORGANISATION_ROLE4.roleName)).isEqualTo(-1L);
    }

    @Test
    @Order(3)
    @Rollback(value = true)
    public void testUpdateOrganisationRole() {

        // Sets RoleName and Permission - Checking for working of correct Errors
        OrganisationRole organisationRole1 =  ORGANISATION_ROLE1.toEntity();
        Organisation organisation1 = new Organisation();
        organisation1.setUser(new User(USER1.userName));
        organisationRole1.setOrganisation(organisation1);

        OrganisationRole organisationRole1ToBeUpdated = ORGANISATION_ROLE2.toEntity();
        organisationRole1ToBeUpdated.setRoleName(ORGANISATION_ROLE1.roleName);

        OrganisationRole organisationRole4 = ORGANISATION_ROLE4.toEntity();
        Organisation organisation4 = new Organisation();
        organisation4.setUser(new User(USER4.userName));
        organisationRole4.setOrganisation(organisation4);

        OrganisationRole organisationRole4ToBeUpdated = ORGANISATION_ROLE1.toEntity();



        Assertions.assertThatThrownBy(() -> organisationRoleRepository.updateOrganisationRole(organisationRole4, organisationRole4ToBeUpdated))
                .isInstanceOf(EmptyResultDataAccessException.class);
        Assertions.assertThatThrownBy(() -> organisationRoleRepository.updateOrganisationRole(organisationRole1, organisationRole1ToBeUpdated))
                .isInstanceOf(DataIntegrityViolationException.class);



        // Checking if Updation is working correctly

        Assertions.assertThat(organisationRoleRepository.existOrganisationRole(organisation1.getUser().getUserName(), organisationRole1.getRoleName())).isNotNull().isNotEqualTo(-1L);
        Assertions.assertThat(organisationRoleRepository.existOrganisationRole(organisation4.getUser().getUserName(), organisationRole4.getRoleName())).isEqualTo(-1L);

        organisationRoleRepository.updateOrganisationRole(organisationRole1, organisationRole4);

        Assertions.assertThat(organisationRoleRepository.existOrganisationRole(organisation1.getUser().getUserName(), organisationRole4.getRoleName())).isNotNull().isNotEqualTo(-1L);
        Assertions.assertThat(organisationRoleRepository.existOrganisationRole(organisation4.getUser().getUserName(), organisationRole4.getRoleName())).isEqualTo(-1L);
    }

    @Test
    @Order(4)
    @Rollback(value = true)
    public void testInsertIntoOrganisationRole() {
        Long user5Id = userRepository.usernameExists(USER5.userName);
        Long user6Id = userRepository.usernameExists(USER6.userName);

        Assertions.assertThatCode(() -> organisationRoleRepository.insertIntoOrganisationRole(USER1.userName, ORGANISATION_ROLE1.roleName, user5Id)).doesNotThrowAnyException();

        Assertions.assertThatThrownBy(()-> organisationRoleRepository.insertIntoOrganisationRole(USER4.userName, OrganisationRoleData.ORGANISATION_ROLE4.roleName, user6Id)).isInstanceOf(EmptyResultDataAccessException.class).hasMessageContaining("No role " + OrganisationRoleData.ORGANISATION_ROLE4.roleName + " exists for organisation " + USER4.userName);

        Assertions.assertThatThrownBy(() -> organisationRoleRepository.insertIntoOrganisationRole(USER1.userName, ORGANISATION_ROLE1.roleName, user5Id)).isInstanceOf(DataIntegrityViolationException.class);

        List<Map<UserBaseDto, OrganisationRoleDto>> organisationRoleList = organisationRoleRepository.getAllOrganisationRoles(USER1.userName, PageRequest.of(0, 10));
        Assertions.assertThat(organisationRoleList.get(0).keySet().stream().map(UserBaseDto::getUserName).toList()).contains(USER5.userName);

        Utils.matchOrganisationRoleDto(organisationRoleList.get(0).values().stream().toList().get(0), ORGANISATION_ROLE1, USER1);


    }

    @Test
    @Order(5)
    public void testGetAllOrganisationRoles(){
        List<Map<UserBaseDto, OrganisationRoleDto>> organisationRoleList = organisationRoleRepository.getAllOrganisationRoles(USER1.userName, PageRequest.of(0, 10));

        Assertions.assertThat(organisationRoleList.size()).isEqualTo(1);


    }

    @Test
    @Order(6)
    @Rollback(value = true)
    public void testRemoveFromOrganisationRole(){
        Long organisationRoleId1 = organisationRoleRepository.existOrganisationRole(USER1.userName, ORGANISATION_ROLE1.roleName);
        Long organisationRoleId4 = organisationRoleRepository.existOrganisationRole(USER4.userName, OrganisationRoleData.ORGANISATION_ROLE4.roleName);

        Long organisationId1 = organisationRepository.existOrganisation(USER1.userName);
        Long organisationId4 = organisationRepository.existOrganisation(USER4.userName);

        Assertions.assertThatCode(() -> organisationRoleRepository.removePersonFromOrganisationRole(organisationId1, ORGANISATION_ROLE1.roleName, organisationRoleId1)).doesNotThrowAnyException();

        Assertions.assertThatThrownBy(()-> organisationRoleRepository.removePersonFromOrganisationRole(organisationId4, OrganisationRoleData.ORGANISATION_ROLE4.roleName, organisationRoleId4)).isInstanceOf(EmptyResultDataAccessException.class);

        Assertions.assertThat(organisationRoleRepository.existOrganisationRole(USER1.userName, ORGANISATION_ROLE1.roleName)).isNotNull().isNotEqualTo(-1L);

    }

    @Test
    @Order(7)
    public void testGetOrganisationRoleIds(){
        List<String> organisationNames = Arrays.asList(USER1.userName, USER2.userName);
        List<String> organisationRoleNames = Arrays.asList(ORGANISATION_ROLE1.roleName, ORGANISATION_ROLE2.roleName);

        List<Long> orgRoleIds = organisationRoleRepository.getOrganisationRoleIds(organisationNames, organisationRoleNames, PageRequest.of(0, 10));

        System.out.println(orgRoleIds);

        Assertions.assertThat(orgRoleIds).isNotNull();
        Assertions.assertThat(orgRoleIds.size()).isEqualTo(2);

        Assertions.assertThat(orgRoleIds).contains(organisationRoleRepository.existOrganisationRole(USER1.userName, ORGANISATION_ROLE1.roleName), organisationRoleRepository.existOrganisationRole(USER2.userName, ORGANISATION_ROLE2.roleName));

        //testing pagination
        List<Long> limitedOrgRoleIds = organisationRoleRepository.getOrganisationRoleIds(organisationNames, organisationRoleNames, PageRequest.of(0, 1));
        Assertions.assertThat(limitedOrgRoleIds.size()).isEqualTo(1);

        //testing empty lists
        List<Long> emptyResult1 = organisationRoleRepository.getOrganisationRoleIds(Collections.emptyList(), organisationRoleNames, PageRequest.of(0, 10));
        Assertions.assertThat(emptyResult1).isEmpty();

        List<Long> emptyResult2 = organisationRoleRepository.getOrganisationRoleIds(organisationNames, Collections.emptyList(), PageRequest.of(0, 10));
        Assertions.assertThat(emptyResult2).isEmpty();
    }





}
