package in.ac.iitj.instiapp.Tests.Repository;

import in.ac.iitj.instiapp.Repository.User.Organisation.OrganisationRoleRepository;
import in.ac.iitj.instiapp.Repository.impl.OrganisationRepositoryImpl;
import in.ac.iitj.instiapp.Repository.impl.OrganisationRoleRepositoryImpl;
import in.ac.iitj.instiapp.Tests.EntityTestData.OrganisationData;
import in.ac.iitj.instiapp.Tests.EntityTestData.OrganisationRoleData;
import in.ac.iitj.instiapp.Tests.EntityTestData.UserData;
import in.ac.iitj.instiapp.Tests.Utilities.InitialiseEntities;
import in.ac.iitj.instiapp.Tests.Utilities.Utils;
import in.ac.iitj.instiapp.database.entities.User.Organisation.Organisation;
import in.ac.iitj.instiapp.database.entities.User.Organisation.OrganisationRole;
import in.ac.iitj.instiapp.database.entities.User.User;
import in.ac.iitj.instiapp.payload.User.Organisation.OrganisationRoleDto;
import org.junit.jupiter.api.*;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static in.ac.iitj.instiapp.Tests.EntityTestData.OrganisationData.ORGANISATION1;
import static in.ac.iitj.instiapp.Tests.EntityTestData.OrganisationTypeData.*;
import static in.ac.iitj.instiapp.Tests.EntityTestData.OrganisationTypeData.ORGANISATION_TYPE4;
import static in.ac.iitj.instiapp.Tests.EntityTestData.UserData.*;

@DataJpaTest
@Import({OrganisationRoleRepositoryImpl.class, InitialiseEntities.InitialiseUser.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrganisationRoleTest {

    private final OrganisationRoleRepository organisationRoleRepository;

    @Autowired
    public OrganisationRoleTest(OrganisationRoleRepository organisationRoleRepository) {
        this.organisationRoleRepository = organisationRoleRepository;
    }

    @BeforeAll
    public static void setUp(@Autowired InitialiseEntities.InitialiseOrganisationRole initialise) {
        initialise.initialise();
    }

    @Test
    @Order(1)
    public void testGetListOrganisationRoles() {
        List<OrganisationRoleDto> organisationRoleDtoList = organisationRoleRepository.getOrganisationRoles(USER1.userName, PageRequest.of(0, 10));

        Assertions.assertThat(organisationRoleDtoList.size()).isEqualTo(1);

        Utils.matchOrganisationRoleDto(organisationRoleDtoList.get(0), OrganisationRoleData.ORGANISATION_ROLE1, USER1);
    }

    @Test
    @Order(2)
    public void testExistOrganisationRole() {
        Assertions.assertThat(organisationRoleRepository.existOrganisationRole(USER1.userName, OrganisationRoleData.ORGANISATION_ROLE1.roleName)).isNotNull().isNotEqualTo(-1L);
        Assertions.assertThat(organisationRoleRepository.existOrganisationRole(USER2.userName, OrganisationRoleData.ORGANISATION_ROLE2.roleName)).isNotNull().isNotEqualTo(-1L);
        Assertions.assertThat(organisationRoleRepository.existOrganisationRole(USER3.userName, OrganisationRoleData.ORGANISATION_ROLE3.roleName)).isNotNull().isNotEqualTo(-1L);
        Assertions.assertThat(organisationRoleRepository.existOrganisationRole(USER4.userName, OrganisationRoleData.ORGANISATION_ROLE4.roleName)).isEqualTo(-1L);
    }

    @Test
    @Order(3)
    public void testUpdateOrganisationRole() {
        OrganisationRole organisationRole1 = new OrganisationRole();
        Organisation organisation1 = new Organisation();
        organisation1.setUser(new User(USER1.userName));
        organisationRole1.setOrganisation(organisation1);
        organisationRole1.setRoleName(OrganisationRoleData.ORGANISATION_ROLE1.roleName);
        organisationRole1.setPermission(OrganisationRoleData.ORGANISATION_ROLE1.organisationPermission);

        OrganisationRole organisationRole2 = new OrganisationRole();
        Organisation organisation2 = new Organisation();
        organisation2.setUser(new User(USER2.userName));
        organisationRole2.setOrganisation(organisation2);
        organisationRole2.setRoleName(OrganisationRoleData.ORGANISATION_ROLE2.roleName);
        organisationRole2.setPermission(OrganisationRoleData.ORGANISATION_ROLE2.organisationPermission);

        OrganisationRole organisationRole4 = new OrganisationRole();
        Organisation organisation4 = new Organisation();
        organisation4.setUser(new User(USER4.userName));
        organisationRole4.setOrganisation(organisation4);
        organisationRole4.setRoleName(OrganisationRoleData.ORGANISATION_ROLE4.roleName);
        organisationRole4.setPermission(OrganisationRoleData.ORGANISATION_ROLE4.organisationPermission);


        Assertions.assertThatThrownBy(() -> organisationRoleRepository.updateOrganisationRole(organisationRole4, organisationRole1))
                .isInstanceOf(EmptyResultDataAccessException.class);
        Assertions.assertThatThrownBy(() -> organisationRoleRepository.updateOrganisationRole(organisationRole1, organisationRole2))
                .isInstanceOf(DataIntegrityViolationException.class);
    }


}
