package in.ac.iitj.instiapp.Tests.Repostiory;

import in.ac.iitj.instiapp.Repository.LostnFoundRepository;
import in.ac.iitj.instiapp.Repository.impl.LostnFoundRepositoryImpl;
import in.ac.iitj.instiapp.database.entities.LostnFound.Locations;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@Import({LostnFoundRepositoryImpl.class})
public class LostnFoundTests {


    @Autowired
    private LostnFoundRepository lostnFoundRepository;

    @Test
    public void testGetListOfLocations() {
        // Create and persist mock data
        Locations location1 = new Locations();
        location1.setName("Location1");
        Locations location2 = new Locations();
        location2.setName("Location2");

        lostnFoundRepository.save(location1);
        lostnFoundRepository.save(location2);

        // Define pageable
        Pageable pageable = PageRequest.of(0, 10);

        // Call the method under test
        List<Map<String, Object>> locations = lostnFoundRepository.getListOfLocations(pageable);
//
        // Assertions
        assertNotNull(locations);
        assertEquals(2, locations.size());
        assertEquals("Location1", locations.get(0).get("name"));
        assertEquals("Location2", locations.get(1).get("name"));
    }
}
