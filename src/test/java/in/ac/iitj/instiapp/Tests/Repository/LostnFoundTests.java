//package in.ac.iitj.instiapp.Tests.Repository;
//
//import in.ac.iitj.instiapp.Repository.LostnFoundRepository;
//import in.ac.iitj.instiapp.Repository.impl.LostnFoundRepositoryImpl;
//import in.ac.iitj.instiapp.database.entities.LostnFound.Locations;
//import in.ac.iitj.instiapp.database.entities.LostnFound.LostnFound;
//import in.ac.iitj.instiapp.database.entities.User.User;
//import in.ac.iitj.instiapp.database.entities.Media.Media;
//import in.ac.iitj.instiapp.payload.LostnFound.LostnFoundDto;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.test.annotation.Rollback;
//import java.util.stream.Collectors;
//import org.springframework.transaction.annotation.Transactional;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//
//
//import java.util.List;
//
//@DataJpaTest
//@Import({LostnFoundRepositoryImpl.class})
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@Rollback(value = false)
//public class LostnFoundTests {
//
//    @PersistenceContext
//    private EntityManager entityManager;
//
//    @Autowired
//    private LostnFoundRepository lostnFoundRepository;
//
//    @BeforeAll
//    public static void setup(@Autowired LostnFoundRepository lostnFoundRepository) {
//        // Create and save Locations
//        Locations location1 = new Locations();
//        location1.setName("LHC");
//        Locations location2 = new Locations();
//        location2.setName("PHC");
//
//        lostnFoundRepository.saveLocation(location1);
//        lostnFoundRepository.saveLocation(location2);
//    }
//
//    @Test
//    @Order(1)
//    public void testSaveLocation() {
//        // Create a new location
//        Locations location3 = new Locations();
//        location3.setName("New Location");
//
//        // Save the new location
//        lostnFoundRepository.saveLocation(location3);
//
//        // Check if it is saved
//        // Retrieve the list of locations
//        Pageable pageable = PageRequest.of(0, 10);
//        List<LostnFoundDto> locations = lostnFoundRepository.getListOfLocationsName(pageable);
//
//        // Check if the new location is in the list of locations
//        boolean locationFound = locations.stream()
//                .anyMatch(dto -> "New Location".equals(dto.getLandmarkName()));
//
//        // Assert that the location was saved
//        Assertions.assertThat(locationFound).isTrue();
//    }
//
//    @Test
//    @Order(2)
//    public void testGetListOfLocations() {
//        // Get a list of locations
//        Pageable pageable = PageRequest.of(0, 10);
//        List<LostnFoundDto> locations = lostnFoundRepository.getListOfLocationsName(pageable);
//
//        // Extract the landmark names from the LostnFoundDto list
//        List<String> locationNames = locations.stream()
//                .map(LostnFoundDto::getLandmarkName)
//                .collect(Collectors.toList());
//
//        // Assert that the list contains the expected location names
//        Assertions.assertThat(locationNames).containsExactlyInAnyOrder("LHC", "PHC", "New Location");
//    }
//
//    @Test
//    @Order(3)
//    public void testDeleteLocationByName() {
//        // Delete a location by name
//        lostnFoundRepository.deleteLocationByName("PHC");
//
//        // Check if the location "PHC" is deleted
//        Pageable pageable = PageRequest.of(0, 10);
//        List<LostnFoundDto> locations = lostnFoundRepository.getListOfLocationsName(pageable);
//
//        // Extract the landmark names from the LostnFoundDto list
//        List<String> locationNames = locations.stream()
//                .map(LostnFoundDto::getLandmarkName)
//                .collect(Collectors.toList());
//
//        // Assert that the list does not contain "PHC"
//        Assertions.assertThat(locationNames).doesNotContain("PHC");
//    }
//
//    @Test
//    @Order(4)
//    public void testGetLostnFoundByStatus() {
//        // Create and save multiple LostnFound entities with varying statuses
//        LostnFoundDto lostnFoundDto1 = new LostnFoundDto(
//                "finder1",          // finderName
//                "finder1",          // finderUserName
//                "finder1@example.com",  // finderEmail
//                "1234567890",       // finderPhoneNumber
//                "owner1",           // ownerName
//                "owner1",           // ownerUserName
//                "owner1@example.com",   // ownerEmail
//                "0987654321",       // ownerPhoneNumber
//                "LHC",              // LandmarkName
//                "Found item details", // extraInfo
//                false,              // status
//                "http://example.com/image.jpg"  // mediaPublicUrl
//        );
//
//        LostnFoundDto lostnFoundDto2 = new LostnFoundDto(
//                "finder2",          // finderName
//                "finder2",          // finderUserName
//                "finder2@example.com",  // finderEmail
//                "1234567891",       // finderPhoneNumber
//                "owner2",           // ownerName
//                "owner2",           // ownerUserName
//                "owner2@example.com",   // ownerEmail
//                "0987654322",       // ownerPhoneNumber
//                "PHC",              // LandmarkName
//                "Lost item details", // extraInfo
//                true,               // status
//                "http://example.com/image2.jpg"  // mediaPublicUrl
//        );
//
//        LostnFoundDto lostnFoundDto3 = new LostnFoundDto(
//                "finder3",          // finderName
//                "finder3",          // finderUserName
//                "finder3@example.com",  // finderEmail
//                "1234567892",       // finderPhoneNumber
//                "owner3",           // ownerName
//                "owner3",           // ownerUserName
//                "owner3@example.com",   // ownerEmail
//                "0987654323",       // ownerPhoneNumber
//                "Football Ground",              // LandmarkName
//                "Lost item details again", // extraInfo
//                false,              // status
//                "http://example.com/image3.jpg"  // mediaPublicUrl
//        );
//
//        // Save the LostnFound entities
//        lostnFoundRepository.saveLostnFoundDetails(lostnFoundDto1);
//        lostnFoundRepository.saveLostnFoundDetails(lostnFoundDto2);
//        lostnFoundRepository.saveLostnFoundDetails(lostnFoundDto3);
//
//        // Get LostnFound items by status (false)
//        Pageable pageable = PageRequest.of(0, 10);
//        List<LostnFoundDto> lostnFounds = lostnFoundRepository.getLostnFoundByStatus(false, pageable);
//
//        // Assert that the returned list contains only the items with a false status
//        Assertions.assertThat(lostnFounds).hasSize(2); // Two items should have false status
//        Assertions.assertThat(lostnFounds.get(0).getLandmarkName()).isEqualTo("LHC");
//        Assertions.assertThat(lostnFounds.get(1).getLandmarkName()).isEqualTo("Football Ground");// Verify the landmark names are correct
//        Assertions.assertThat(lostnFounds).allMatch(item -> !item.getStatus()); // Ensure all items have false status
//    }
//
//
//
//    @Test
//    @Order(5)
//    public void testGetLostnFoundWithDetails() {
//        // Create and save 3 LostnFound entities with different details
//        LostnFoundDto lostnFoundDto1 = new LostnFoundDto(
//                "finder1",          // finderName
//                "finder1",          // finderUserName
//                "finder1@example.com",  // finderEmail
//                "1234567890",       // finderPhoneNumber
//                "owner1",           // ownerName
//                "owner1",           // ownerUserName
//                "owner1@example.com",   // ownerEmail
//                "0987654321",       // ownerPhoneNumber
//                "LHC",              // LandmarkName
//                "Found item details 1", // extraInfo
//                false,              // status
//                "http://example.com/image1.jpg"  // mediaPublicUrl
//        );
//
//        LostnFoundDto lostnFoundDto2 = new LostnFoundDto(
//                "finder2",          // finderName
//                "finder2",          // finderUserName
//                "finder2@example.com",  // finderEmail
//                "1234567891",       // finderPhoneNumber
//                "owner2",           // ownerName
//                "owner2",           // ownerUserName
//                "owner2@example.com",   // ownerEmail
//                "0987654322",       // ownerPhoneNumber
//                "LBC",              // LandmarkName
//                "Found item details 2", // extraInfo
//                false,              // status
//                "http://example.com/image2.jpg"  // mediaPublicUrl
//        );
//
//        LostnFoundDto lostnFoundDto3 = new LostnFoundDto(
//                "finder3",          // finderName
//                "finder3",          // finderUserName
//                "finder3@example.com",  // finderEmail
//                "1234567892",       // finderPhoneNumber
//                "owner3",           // ownerName
//                "owner3",           // ownerUserName
//                "owner3@example.com",   // ownerEmail
//                "0987654323",       // ownerPhoneNumber
//                "LHD",              // LandmarkName
//                "Found item details 3", // extraInfo
//                true,               // status
//                "http://example.com/image3.jpg"  // mediaPublicUrl
//        );
//
//        // Save the LostnFound entities to the repository
//        lostnFoundRepository.saveLostnFoundDetails(lostnFoundDto1);
//        lostnFoundRepository.saveLostnFoundDetails(lostnFoundDto2);
//        lostnFoundRepository.saveLostnFoundDetails(lostnFoundDto3);
//
//        // Get all LostnFound items with details
//        Pageable pageable = PageRequest.of(0, 10);
//        List<LostnFoundDto> lostnFounds = lostnFoundRepository.getLostnFoundWithDetails(pageable);
//
//        // Assert that the list contains exactly 3 entities
//        Assertions.assertThat(lostnFounds).hasSize(3);
//
//        // Assert that the details of each LostnFound entity are correct
//        Assertions.assertThat(lostnFounds.get(0).getLandmarkName()).isEqualTo("LHC");
//        Assertions.assertThat(lostnFounds.get(0).getFinderUserName()).isEqualTo("finder1");
//        Assertions.assertThat(lostnFounds.get(0).getStatus()).isFalse();
//
//        Assertions.assertThat(lostnFounds.get(1).getLandmarkName()).isEqualTo("LBC");
//        Assertions.assertThat(lostnFounds.get(1).getFinderUserName()).isEqualTo("finder2");
//        Assertions.assertThat(lostnFounds.get(1).getStatus()).isFalse();
//
//        Assertions.assertThat(lostnFounds.get(2).getLandmarkName()).isEqualTo("LHD");
//        Assertions.assertThat(lostnFounds.get(2).getFinderUserName()).isEqualTo("finder3");
//        Assertions.assertThat(lostnFounds.get(2).getStatus()).isTrue();
//    }
//
//
//    @Test
//    @Order(6)
//    public void testGetLostnFoundByLandmark() {
//        // Create and save 3 LostnFound entities with different landmarks
//        LostnFoundDto lostnFoundDto1 = new LostnFoundDto(
//                "finder1",          // finderName
//                "finder1",          // finderUserName
//                "finder1@example.com",  // finderEmail
//                "1234567890",       // finderPhoneNumber
//                "owner1",           // ownerName
//                "owner1",           // ownerUserName
//                "owner1@example.com",   // ownerEmail
//                "0987654321",       // ownerPhoneNumber
//                "LHC",              // LandmarkName
//                "Found item details 1", // extraInfo
//                false,              // status
//                "http://example.com/image1.jpg"  // mediaPublicUrl
//        );
//
//        LostnFoundDto lostnFoundDto2 = new LostnFoundDto(
//                "finder2",          // finderName
//                "finder2",          // finderUserName
//                "finder2@example.com",  // finderEmail
//                "1234567891",       // finderPhoneNumber
//                "owner2",           // ownerName
//                "owner2",           // ownerUserName
//                "owner2@example.com",   // ownerEmail
//                "0987654322",       // ownerPhoneNumber
//                "LHC",              // LandmarkName
//                "Found item details 2", // extraInfo
//                false,              // status
//                "http://example.com/image2.jpg"  // mediaPublicUrl
//        );
//
//        LostnFoundDto lostnFoundDto3 = new LostnFoundDto(
//                "finder3",          // finderName
//                "finder3",          // finderUserName
//                "finder3@example.com",  // finderEmail
//                "1234567892",       // finderPhoneNumber
//                "owner3",           // ownerName
//                "owner3",           // ownerUserName
//                "owner3@example.com",   // ownerEmail
//                "0987654323",       // ownerPhoneNumber
//                "LHD",              // LandmarkName
//                "Found item details 3", // extraInfo
//                true,               // status
//                "http://example.com/image3.jpg"  // mediaPublicUrl
//        );
//
//        // Save the LostnFound entities to the repository
//        lostnFoundRepository.saveLostnFoundDetails(lostnFoundDto1);
//        lostnFoundRepository.saveLostnFoundDetails(lostnFoundDto2);
//        lostnFoundRepository.saveLostnFoundDetails(lostnFoundDto3);
//
//        // Get LostnFound items by landmark name "LHC"
//        Pageable pageable = PageRequest.of(0, 10);
//        List<LostnFoundDto> lostnFounds = lostnFoundRepository.getLostnFoundByLandmark("LHC", pageable);
//
//        // Assert that only 2 entities with the "LHC" landmark are returned
//        Assertions.assertThat(lostnFounds).hasSize(2);
//
//        // Assert that the returned entities have the correct landmark name "LHC"
//        Assertions.assertThat(lostnFounds.get(0).getLandmarkName()).isEqualTo("LHC");
//        Assertions.assertThat(lostnFounds.get(1).getLandmarkName()).isEqualTo("LHC");
//    }
//
//
//    @Test
//    @Transactional
//    @Order(7)
//    public void testSaveLostnFoundDetails() {
//        // Step 1: Create and persist related entities (User, Location, Media)
//        User finder = new User("finderUserName", "finder@example.com", "1234567890");
//        User owner = new User("ownerUserName", "owner@example.com", "0987654321");
//        Locations landmark = new Locations("LHC");
//        Media media = new Media("http://example.com/image.jpg");
//
//        entityManager.persist(finder);
//        entityManager.persist(owner);
//        entityManager.persist(landmark);
//        entityManager.persist(media);
//
//        // Step 2: Create a LostnFoundDto with the required data
//        LostnFoundDto lostnFoundDto = new LostnFoundDto(
//                "finderUserName",        // finderName
//                "finderUserName",        // finderUserName
//                "finder@example.com",    // finderEmail
//                "1234567890",            // finderPhoneNumber
//                "ownerUserName",         // ownerName
//                "ownerUserName",         // ownerUserName
//                "owner@example.com",     // ownerEmail
//                "0987654321",            // ownerPhoneNumber
//                "LHC",                   // LandmarkName
//                "Found item details",    // extraInfo
//                false,                   // status
//                "http://example.com/image.jpg" // mediaPublicUrl
//        );
//
//        // Step 3: Call the service method to save LostnFoundDto
//        lostnFoundRepository.saveLostnFoundDetails(lostnFoundDto);
//
//        // Step 4: Retrieve the LostnFound entity from the database using the saved data
//        LostnFound savedLostnFound = entityManager.createQuery(
//                        "SELECT lf FROM LostnFound lf WHERE lf.finder.userName = :finderUserName AND lf.owner.userName = :ownerUserName",
//                        LostnFound.class)
//                .setParameter("finderUserName", "finderUserName")
//                .setParameter("ownerUserName", "ownerUserName")
//                .getSingleResult();
//
//        // Step 5: Assert the retrieved entity's details to verify successful saving
//        Assertions.assertThat(savedLostnFound).isNotNull();
//        Assertions.assertThat(savedLostnFound.getFinder().getUserName()).isEqualTo("finderUserName");
//        Assertions.assertThat(savedLostnFound.getOwner().getUserName()).isEqualTo("ownerUserName");
//        Assertions.assertThat(savedLostnFound.getLandmark().getName()).isEqualTo("LHC");
//        Assertions.assertThat(savedLostnFound.getExtraInfo()).isEqualTo("Found item details");
//        Assertions.assertThat(savedLostnFound.getStatus()).isFalse();
//        Assertions.assertThat(savedLostnFound.getMedia().getPublicUrl()).isEqualTo("http://example.com/image.jpg");
//    }
//
//
//}
