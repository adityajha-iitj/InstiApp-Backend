package in.ac.iitj.instiapp.Tests.Repository;

import in.ac.iitj.instiapp.Repository.LostnFoundRepository;
import in.ac.iitj.instiapp.Repository.impl.*;
import in.ac.iitj.instiapp.Tests.EntityTestData.LocationData;
import in.ac.iitj.instiapp.Tests.EntityTestData.LostnFoundData;
import in.ac.iitj.instiapp.Tests.EntityTestData.MediaData;
import in.ac.iitj.instiapp.Tests.EntityTestData.UserData;
import in.ac.iitj.instiapp.Tests.Utilities.InitialiseEntities;
import in.ac.iitj.instiapp.payload.LostnFound.LostnFoundDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@Import({LostnFoundRepositoryImpl.class , InitialiseEntities.InitialiseLostnFound.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LostnFoundTest {


    private final LostnFoundRepository lostnFoundRepository;

    @Autowired
    public LostnFoundTest(LostnFoundRepository lostnFoundRepository) {
        this.lostnFoundRepository = lostnFoundRepository;
    }

    @BeforeAll
    public static void setup(@Autowired InitialiseEntities.InitialiseLostnFound lostnFound)  {
        lostnFound.initialise();
    }

    @Test
    @Order(1)
    public void testExistLocation(){
        Long id = lostnFoundRepository.existLocation(LocationData.LOCATION1.name);
        Long id2 = lostnFoundRepository.existLocation(LocationData.LOCATION4.name);
        Assertions.assertNotEquals(-1, id);
        Assertions.assertEquals(-1, id2);
    }

    @Test
    @Order(2)
    public void testGetListOfLocationsName(){
        Pageable pageable = PageRequest.of(0, 10);
        List<String> locations = lostnFoundRepository.getListOfLocationsName(pageable);
        Assertions.assertEquals(3, locations.size());
        Assertions.assertEquals(LocationData.LOCATION1.name, locations.get(0));
        Assertions.assertEquals(LocationData.LOCATION2.name, locations.get(1));
        Assertions.assertEquals(LocationData.LOCATION3.name, locations.get(2));

    }
    @Test
    @Order(3)
    @Rollback(value = true)
    public void testUpdateLocation(){
        lostnFoundRepository.updateLocation(LocationData.LOCATION1.name , LocationData.LOCATION4.toEntity());
        Long id = lostnFoundRepository.existLocation(LocationData.LOCATION4.name);
        Assertions.assertNotEquals(-1, id);
    }

    @Test
    @Order(4)
    @Rollback(value = true)
    public void testDeleteLocation(){
        lostnFoundRepository.deleteLocationByName(LocationData.LOCATION1.name);
        Long id = lostnFoundRepository.existLocation(LocationData.LOCATION1.name);
        Assertions.assertEquals(-1, id);
    }

    @Test
    @Order(5)
    public void testExistLostnfound(){
        Long id1 = lostnFoundRepository.exsitLostnFound(LostnFoundData.LOST_N_FOUND1.publicId);
        Long id2 = lostnFoundRepository.exsitLostnFound(LostnFoundData.LOST_N_FOUND4.publicId);
        Assertions.assertNotEquals(-1, id1);
        Assertions.assertEquals(-1, id2);

    }
    @Test
    @Order(6)
    public void testGetLostnFoundByFilter(){
        Pageable pageable = PageRequest.of(0, 10);
        List<LostnFoundDto> status_filter = lostnFoundRepository.getLostnFoundByFilter(Optional.of(true) , Optional.empty() , Optional.empty() , Optional.empty() , pageable);
        Assertions.assertEquals(1, status_filter.size());
        Assertions.assertEquals(LostnFoundData.LOST_N_FOUND1.publicId, status_filter.get(0).getPublicId());
        Assertions.assertEquals(LostnFoundData.LOST_N_FOUND1.extraInfo, status_filter.get(0).getExtraInfo());

        List<LostnFoundDto>  owner_filter = lostnFoundRepository.getLostnFoundByFilter(Optional.empty() , Optional.of(UserData.USER15.userName) ,Optional.empty() , Optional.empty() , pageable);
        Assertions.assertEquals(1, owner_filter.size());
        Assertions.assertEquals(LostnFoundData.LOST_N_FOUND2.publicId, owner_filter.get(0).getPublicId());

        List<LostnFoundDto> finder_filter = lostnFoundRepository.getLostnFoundByFilter(Optional.empty() , Optional.empty() , Optional.of(UserData.USER14.userName) , Optional.empty() , pageable);
        Assertions.assertEquals(1, finder_filter.size());
        Assertions.assertEquals(LostnFoundData.LOST_N_FOUND3.publicId, finder_filter.get(0).getPublicId());

        List<LostnFoundDto> land_filter = lostnFoundRepository.getLostnFoundByFilter(Optional.empty() , Optional.empty(), Optional.empty() , Optional.of(LocationData.LOCATION2.name) , pageable);
        Assertions.assertEquals(1, land_filter.size());
        Assertions.assertEquals(LostnFoundData.LOST_N_FOUND2.publicId, land_filter.get(0).getPublicId());

        List<LostnFoundDto> no_filter = lostnFoundRepository.getLostnFoundByFilter(Optional.empty() , Optional.empty() , Optional.empty() , Optional.empty() , pageable);
        Assertions.assertEquals(3, no_filter.size());
        Assertions.assertEquals(LostnFoundData.LOST_N_FOUND1.publicId, no_filter.get(0).getPublicId());
        Assertions.assertEquals(UserData.USER14.userName, no_filter.get(0).getOwner().getUserName());
        Assertions.assertEquals(LocationData.LOCATION1.name , no_filter.get(0).getLandmarkName());
        Assertions.assertEquals(MediaData.MEDIA1.publicId, no_filter.get(0).getMedia().getPublicId());
        Assertions.assertEquals(LostnFoundData.LOST_N_FOUND2.publicId, no_filter.get(1).getPublicId());
        Assertions.assertEquals(UserData.USER14.userName, no_filter.get(2).getFinder().getUserName());
        Assertions.assertEquals(LostnFoundData.LOST_N_FOUND3.publicId, no_filter.get(2).getPublicId());


    }


    @Test
    @Order(7)
    @Rollback(value = true)
    public void updateLostnfound(){
        Pageable pageable = PageRequest.of(0, 10);
        lostnFoundRepository.updateLostnFound(LostnFoundData.LOST_N_FOUND4.toEntity() , LostnFoundData.LOST_N_FOUND1.publicId);
        List<LostnFoundDto> data = lostnFoundRepository.getLostnFoundByFilter(Optional.empty() , Optional.of(UserData.USER14.userName) , Optional.empty() , Optional.empty() , pageable);
        Assertions.assertEquals(1, data.size());
        Assertions.assertEquals(LocationData.LOCATION1.name, data.get(0).getLandmarkName());
        Assertions.assertEquals(LostnFoundData.LOST_N_FOUND4.extraInfo , data.get(0).getExtraInfo());

    }

    @Test
    @Order(8)
    @Rollback(value = true)
    public void testDeleteLostnfound(){
        lostnFoundRepository.deleteLostnFound(LostnFoundData.LOST_N_FOUND1.publicId);
        long id = lostnFoundRepository.exsitLostnFound(LostnFoundData.LOST_N_FOUND1.publicId);
        Assertions.assertEquals(-1, id);
    }








}
