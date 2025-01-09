package in.ac.iitj.instiapp.Tests.Repository;

import in.ac.iitj.instiapp.Repository.MediaRepository;
import in.ac.iitj.instiapp.Repository.User.Organisation.OrganisationRepository;
import in.ac.iitj.instiapp.Repository.impl.MediaRepositoryImpl;
import in.ac.iitj.instiapp.Repository.impl.OrganisationRepositoryImpl;
import in.ac.iitj.instiapp.Tests.EntityTestData.MediaData;
import in.ac.iitj.instiapp.Tests.EntityTestData.OrganisationData;
import in.ac.iitj.instiapp.Tests.EntityTestData.OrganisationTypeData;
import in.ac.iitj.instiapp.Tests.Utilities.InitialiseEntities;
import in.ac.iitj.instiapp.Tests.Utilities.Utils;
import in.ac.iitj.instiapp.database.entities.Media.Media;
import in.ac.iitj.instiapp.database.entities.User.Organisation.Organisation;
import in.ac.iitj.instiapp.database.entities.User.Organisation.OrganisationType;
import in.ac.iitj.instiapp.payload.User.Organisation.OrganisationBaseDto;
import in.ac.iitj.instiapp.payload.User.Organisation.OrganisationDetailedDto;
import in.ac.iitj.instiapp.payload.User.UserDetailedDto;
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
import java.util.List;

import static in.ac.iitj.instiapp.Tests.EntityTestData.CalendarData.CALENDAR1;
import static in.ac.iitj.instiapp.Tests.EntityTestData.OrganisationData.*;
import static in.ac.iitj.instiapp.Tests.EntityTestData.OrganisationTypeData.*;
import static in.ac.iitj.instiapp.Tests.EntityTestData.UserData.*;
import static in.ac.iitj.instiapp.Tests.EntityTestData.UserData.USER3;
import static in.ac.iitj.instiapp.Tests.EntityTestData.UserTypeData.USER_TYPE1;

@DataJpaTest
@Import({OrganisationRepositoryImpl.class, InitialiseEntities.InitialiseOrganisation.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrganisationTest {

    private final OrganisationRepository organisationRepository;
    private final MediaRepository mediaRepository;


    @Autowired
    public OrganisationTest(OrganisationRepository organisationRepository, MediaRepository mediaRepository) {
        this.organisationRepository = organisationRepository;
        this.mediaRepository = mediaRepository;
    }

    @BeforeAll
    public static void setUp(@Autowired InitialiseEntities.InitialiseOrganisation initialise) {
        initialise.initialise();
    }

    @Test
    @Order(1)
    @Rollback(value = true)
    public void testSaveOrganisationType(){
        Assertions.assertThat(organisationRepository.existsOrganisationType(ORGANISATION_TYPE1.name)).isNotNull().isNotEqualTo(-1L);
        Assertions.assertThat(organisationRepository.existsOrganisationType(ORGANISATION_TYPE4.name)).isEqualTo(-1L);

        organisationRepository.saveOrganisationType(ORGANISATION_TYPE4.toEntity());

        Assertions.assertThat(organisationRepository.existsOrganisationType(ORGANISATION_TYPE4.name)).isNotNull().isNotEqualTo(-1L);


        Assertions.assertThatThrownBy(() -> organisationRepository.saveOrganisationType(ORGANISATION_TYPE1.toEntity()))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @Order(2)
    public void testGetAllOrganisationTypes(){
        Assertions.assertThat(organisationRepository.getAllOrganisationTypes(PageRequest.of(0,10)))
                .containsExactlyInAnyOrder(ORGANISATION_TYPE1.name, ORGANISATION_TYPE2.name,ORGANISATION_TYPE3.name);
    }

    @Test
    @Order(3)
    public void testExistsOrganisationType(){
        Assertions.assertThat(organisationRepository.existsOrganisationType(ORGANISATION_TYPE1.name)).isNotNull().isNotEqualTo(-1L);
        Assertions.assertThat(organisationRepository.existsOrganisationType(ORGANISATION_TYPE2.name)).isNotNull().isNotEqualTo(-1L);
        Assertions.assertThat(organisationRepository.existsOrganisationType(ORGANISATION_TYPE3.name)).isNotNull().isNotEqualTo(-1L);
        Assertions.assertThat(organisationRepository.existsOrganisationType(ORGANISATION_TYPE4.name)).isEqualTo(-1L);
    }


    @Test
    @Order(4)
    @Rollback(value = true)
    public void testUpdateOrganisationType(){
        Assertions.assertThatThrownBy(() -> organisationRepository.updateOrganisationType(ORGANISATION_TYPE4.name,ORGANISATION_TYPE1.name))
                .isInstanceOf(EmptyResultDataAccessException.class);
        Assertions.assertThatThrownBy(() -> organisationRepository.updateOrganisationType(ORGANISATION_TYPE1.name,ORGANISATION_TYPE2.name))
                .isInstanceOf(DataIntegrityViolationException.class);


        Assertions.assertThat(organisationRepository.existsOrganisationType(ORGANISATION_TYPE1.name)).isNotNull().isNotEqualTo(-1L);
        Assertions.assertThat(organisationRepository.existsOrganisationType(ORGANISATION_TYPE4.name)).isEqualTo(-1L);


        organisationRepository.updateOrganisationType(ORGANISATION_TYPE1.name, ORGANISATION_TYPE4.name);

        Assertions.assertThat(organisationRepository.existsOrganisationType(ORGANISATION_TYPE4.name)).isNotNull().isNotEqualTo(-1L);
        Assertions.assertThat(organisationRepository.existsOrganisationType(ORGANISATION_TYPE1.name)).isEqualTo(-1L);
    }

    // TODO
    @Test
    @Order(5)
    @Rollback(value = true)
    public void testDeleteOrganisationType(){
        //TODO
        Assertions.assertThat(organisationRepository.existsOrganisationType(ORGANISATION_TYPE1.name)).isNotNull().isNotEqualTo(-1L);
        organisationRepository.deleteOrganisationType(ORGANISATION_TYPE1.name);
        Assertions.assertThat(organisationRepository.existsOrganisationType(ORGANISATION_TYPE1.name)).isEqualTo(-1L);
    }

    @Test
    @Order(6)
    public void testGetOrganisationLimited(){
        Assertions.assertThatThrownBy(() ->{
            organisationRepository.getOrganisation(USER4.userName);
        }).isInstanceOf(EmptyResultDataAccessException.class);

        OrganisationBaseDto organisationBaseDto = organisationRepository.getOrganisation(USER1.userName);

        Utils.matchOrganisationBaseDto(organisationBaseDto, ORGANISATION1, ORGANISATION_TYPE1);

    }

    @Test
    @Order(7)
    public void testGetListOrganisationLimitedByOrganisationType(){
        List<OrganisationBaseDto> organisationBaseDtoList = organisationRepository.getOrganisationByType(ORGANISATION_TYPE1.toEntity(), PageRequest.of(0,10));

        Assertions.assertThat(organisationBaseDtoList.size()).isEqualTo(1);


        Utils.matchOrganisationBaseDto(organisationBaseDtoList.get(0),ORGANISATION1,ORGANISATION_TYPE1);
    }

    @Test
    @Order(8)
    public void testGetOrganisationDetailed(){
        Assertions.assertThatThrownBy(() ->{
            organisationRepository.organisationDetailed(USER4.userName);
        }).isInstanceOf(EmptyResultDataAccessException.class);


        OrganisationDetailedDto organisationDetailedDto = organisationRepository.organisationDetailed(USER3.userName);
        Assertions.assertThat(organisationDetailedDto.getDescription()).isEqualTo(ORGANISATION3.description);
        Assertions.assertThat(organisationDetailedDto.getWebsite()).isEqualTo(ORGANISATION3.website);
        Assertions.assertThat(organisationDetailedDto.getTypeName()).isEqualTo(ORGANISATION_TYPE3.name);
        Assertions.assertThat(organisationDetailedDto.getParentOrganisation().getUser().getUserName()).isEqualTo(USER1.userName);

        OrganisationDetailedDto organisationDetailedDto1 = organisationRepository.organisationDetailed(USER1.userName);
        Assertions.assertThat(organisationDetailedDto1.getDescription()).isEqualTo(ORGANISATION1.description);
        Assertions.assertThat(organisationDetailedDto1.getWebsite()).isEqualTo(ORGANISATION1.website);
        Assertions.assertThat(organisationDetailedDto1.getTypeName()).isEqualTo(ORGANISATION_TYPE1.name);

    }

    @Test
    @Order(9)
    public void testOrganisationExist(){
        Assertions.assertThat(organisationRepository.existOrganisation(USER1.userName)).isNotNull().isNotEqualTo(-1L);
        Assertions.assertThat(organisationRepository.existOrganisation(USER2.userName)).isNotNull().isNotEqualTo(-1L);
        Assertions.assertThat(organisationRepository.existOrganisation(USER3.userName)).isNotNull().isNotEqualTo(-1L);
        Assertions.assertThat(organisationRepository.existOrganisation(USER4.userName)).isEqualTo(-1L);
    }

    @Test
    @Order(10)
    @Rollback(value = true)
    public void testUpdateOrganisation(){

        Organisation organisation = OrganisationData.ORGANISATION3.toEntity();
        organisation.setId(organisationRepository.existOrganisation(USER1.userName));
        organisation.setMedia(Arrays.asList(new Media(mediaRepository.getIdByPublicId(MediaData.MEDIA5.publicId))));
        organisation.setType(new OrganisationType(organisationRepository.existsOrganisationType(ORGANISATION_TYPE1.name)));
        organisation.setDescription(ORGANISATION1.description);
        organisation.setWebsite(ORGANISATION1.website);

//        Assertions.assertThatThrownBy(() ->
//                organisationRepository.updateOrganisation(organisation)
//        ).isInstanceOf(EmptyResultDataAccessException.class);

        Assertions.assertThat((organisationRepository.organisationDetailed(USER3.userName)).getUser().getUserName()).isEqualTo(USER3.userName);

        organisationRepository.updateOrganisation(organisation);

        Assertions.assertThat(organisationRepository.organisationDetailed(USER1.userName).getDescription()).isEqualTo(ORGANISATION1.description);
         organisation.setType(new OrganisationType(organisationRepository.existsOrganisationType(ORGANISATION_TYPE2.name)));
         organisationRepository.updateOrganisation(organisation);
        Assertions.assertThat(organisationRepository.organisationDetailed(USER1.userName).getTypeName()).isEqualTo(ORGANISATION_TYPE2.name);
        organisation.setParentOrganisation(new Organisation(organisationRepository.existOrganisation(USER2.userName)));
        organisationRepository.updateOrganisation(organisation);
        Assertions.assertThat(organisationRepository.organisationDetailed(USER1.userName).getParentOrganisation().getUser().getUserName()).isEqualTo(USER2.userName);

    }




}
