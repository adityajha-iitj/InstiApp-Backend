package in.ac.iitj.instiapp.Tests.Repository;

import in.ac.iitj.instiapp.Repository.MediaRepository;
import in.ac.iitj.instiapp.Repository.PermissionsRepository;
import in.ac.iitj.instiapp.Repository.User.Organisation.OrganisationRepository;
import in.ac.iitj.instiapp.Repository.impl.MessRepositoryImpl;
import in.ac.iitj.instiapp.Repository.impl.PermissionsRepositoryImpl;
import in.ac.iitj.instiapp.Tests.Utilities.InitialiseEntities;
import in.ac.iitj.instiapp.database.entities.User.Permissions;
import in.ac.iitj.instiapp.database.entities.User.PermissionsData;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;

import static in.ac.iitj.instiapp.Tests.EntityTestData.OrganisationTypeData.*;

@DataJpaTest
@Import({PermissionsRepositoryImpl.class, InitialiseEntities.InitialisePermission.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //  Cannot be removed as the repository uses postgres specific schema for queries
public class PermissionsTest {

    private final PermissionsRepositoryImpl permissionsRepositoryImpl;

    @Autowired
    public PermissionsTest(PermissionsRepositoryImpl permissionsRepositoryImpl) {
        this.permissionsRepositoryImpl = permissionsRepositoryImpl;
    }

    @BeforeAll
    public static void setUp(@Autowired InitialiseEntities.InitialisePermission initialise) {
        initialise.initialise();
    }

    @Test
    @Order(1)
    public void testSavePermission(){
        Assertions.assertThat(permissionsRepositoryImpl.existsPermission(PermissionsData.MENU_SCHEDULE)).isEqualTo(-1L);

        permissionsRepositoryImpl.savePermission(PermissionsData.MENU_SCHEDULE.toEntity());

        Assertions.assertThat(permissionsRepositoryImpl.existsPermission(PermissionsData.MENU_SCHEDULE)).isNotNull().isNotEqualTo(-1L);

        Assertions.assertThatThrownBy(() -> permissionsRepositoryImpl.savePermission(PermissionsData.CALENDAR.toEntity())).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @Order(2)
    public void testExistsPermission(){
        Assertions.assertThat(permissionsRepositoryImpl.existsPermission(PermissionsData.BUS_SCHEDULE)).isNotNull().isNotEqualTo(-1L);
        Assertions.assertThat(permissionsRepositoryImpl.existsPermission(PermissionsData.CALENDAR)).isNotNull().isNotEqualTo(-1L);
        Assertions.assertThat(permissionsRepositoryImpl.existsPermission(PermissionsData.MENU_SCHEDULE)).isEqualTo(-1L);
    }

    @Test
    @Order(3)
    public void testGetPermissions(){
        Assertions.assertThat(permissionsRepositoryImpl.getPermissions(PageRequest.of(0, 10)))
                .extracting(Permissions::getPermissionsData)
                .containsExactlyInAnyOrder(PermissionsData.BUS_SCHEDULE, PermissionsData.CALENDAR);
    }

    @Test
    @Order(4)
    public void testUpdatePermissions(){
        Assertions.assertThatThrownBy(() -> permissionsRepositoryImpl.updatePermission(PermissionsData.MENU_SCHEDULE.toEntity(), PermissionsData.BUS_SCHEDULE.toEntity())).isInstanceOf(EmptyResultDataAccessException.class);

        Assertions.assertThatThrownBy(() -> permissionsRepositoryImpl.updatePermission(PermissionsData.BUS_SCHEDULE.toEntity(), PermissionsData.CALENDAR.toEntity())).isInstanceOf(DataIntegrityViolationException.class);

        Assertions.assertThat(permissionsRepositoryImpl.existsPermission(PermissionsData.BUS_SCHEDULE)).isNotNull().isNotEqualTo(-1L);
        Assertions.assertThat(permissionsRepositoryImpl.existsPermission(PermissionsData.MENU_SCHEDULE)).isEqualTo(-1L);

        permissionsRepositoryImpl.updatePermission(PermissionsData.BUS_SCHEDULE.toEntity(), PermissionsData.MENU_SCHEDULE.toEntity());

        Assertions.assertThat(permissionsRepositoryImpl.existsPermission(PermissionsData.MENU_SCHEDULE)).isNotNull().isNotEqualTo(-1L);
        Assertions.assertThat(permissionsRepositoryImpl.existsPermission(PermissionsData.BUS_SCHEDULE)).isEqualTo(-1L);

    }


}
