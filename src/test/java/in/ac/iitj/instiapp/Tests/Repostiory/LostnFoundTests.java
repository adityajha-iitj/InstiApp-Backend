package in.ac.iitj.instiapp.Tests.Repostiory;

import in.ac.iitj.instiapp.Repository.LostnFoundRepository;
import in.ac.iitj.instiapp.Repository.impl.LostnFoundRepositoryImpl;
import in.ac.iitj.instiapp.database.entities.LostnFound.Locations;
import in.ac.iitj.instiapp.database.entities.LostnFound.LostnFound;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@DataJpaTest
@Import({LostnFoundRepositoryImpl.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Rollback(value = false)
public class LostnFoundTests {

    @Autowired
    private LostnFoundRepository lostnFoundRepository;

    @BeforeAll
    public static  void setup(@Autowired LostnFoundRepository lostnFoundRepository){
        Locations location1 = new Locations();
        location1.setName("LHC");
        Locations location2 = new Locations();
        location2.setName("PHC");
        lostnFoundRepository.saveLocation(location1);
        lostnFoundRepository.saveLocation(location2);
    }

    @Test
    @Order(1)
    public void testGetListOfLocations() {
        Pageable pageable = PageRequest.of(0, 10);
        List<String> locations = lostnFoundRepository.getListOfLocationsName(pageable);
        Assertions.assertThat(locations).containsExactlyInAnyOrder("LHC", "PHC");
    }

    @Test
    @Order(2)
    public void testDeleteLocationByName(){
        lostnFoundRepository.deleteLocationByName("PHC");
        Pageable pageable = PageRequest.of(0, 10);
        List<String> locations = lostnFoundRepository.getListOfLocationsName(pageable);
        System.out.println(locations);
        Assertions.assertThat(locations).containsExactlyInAnyOrder("LHC");
    }
}
