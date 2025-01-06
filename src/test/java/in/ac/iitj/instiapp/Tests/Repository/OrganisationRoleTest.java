package in.ac.iitj.instiapp.Tests.Repository;

import in.ac.iitj.instiapp.Repository.User.Organisation.OrganisationRoleRepository;
import in.ac.iitj.instiapp.Repository.impl.OrganisationRepositoryImpl;
import in.ac.iitj.instiapp.Repository.impl.OrganisationRoleRepositoryImpl;
import in.ac.iitj.instiapp.Tests.Utilities.InitialiseEntities;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({OrganisationRoleRepositoryImpl.class, InitialiseEntities.InitialiseUser.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrganisationRoleTest {

    private final OrganisationRoleRepository organisationRoleRepository;

    @Autowired
    public OrganisationRoleTest(OrganisationRoleRepository organisationRoleRepository) {
        this.organisationRoleRepository = organisationRoleRepository;
    }

    @BeforeAll
    public static void setUp(@Autowired InitialiseEntities initialiseEntities) {}
}
