//package in.ac.iitj.instiapp.Tests.Repository;
//
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//
//import in.ac.iitj.instiapp.payload.AnnouncementsDto;
//import in.ac.iitj.instiapp.Repository.AnnouncementsRepository;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//import java.util.List;
//
//@SpringBootTest
//public class AnnouncementsTest {
//
//    @Autowired
//    private AnnouncementsRepository AnnouncementsRepository;
//
//    @Test
//    @Order(1)
//    public void testCreateAnnouncement() {
//        // Create a test AnnouncementsDto
//        AnnouncementsDto announcementsDto = new AnnouncementsDto(
//                "user1",                   // userUserName
//                "User One",                // userFullName
//                "user1@example.com",       // userEmail
//                "https://example.com/avatar.jpg",  // userAvatarUrl
//                "New Announcement",        // title
//                "This is a new announcement.",     // description
//                null,                      // media (null for simplicity)
//                null,                      // groupsList (null)
//                null,
//                null
//        );
//
//        // Call the createAnnouncement method
//        AnnouncementsRepository.createAnnouncement(announcementsDto);
//
//        // Fetch the announcement from the database
//        Pageable pageable = PageRequest.of(0, 10);
//        List<AnnouncementsDto> announcements = AnnouncementsRepository.getAllAnnouncements(pageable);
//
//        // Assert that the announcement is present in the result
//        assertThat(announcements).isNotEmpty();
//        assertThat(announcements.get(0).getTitle()).isEqualTo("New Announcement");
//        assertThat(announcements.get(0).getDescription()).isEqualTo("This is a new announcement.");
//    }
//
//    @Test
//    @Order(2)
//    public void testEditAnnouncement() {
//        // Create a test AnnouncementsDto with initial values
//        AnnouncementsDto announcementsDto = new AnnouncementsDto(
//                "user1",                   // userUserName
//                "User One",                // userFullName
//                "user1@example.com",       // userEmail
//                "https://example.com/avatar.jpg",  // userAvatarUrl
//                "Initial Announcement",    // title
//                "This is the initial description.", // description
//                null,                // dateOfAnnouncement
//                null,                      // media
//                null,                      // groupsList
//                null                       // users
//        );
//
//        // Call the createAnnouncement method first to insert the announcement
//        AnnouncementsRepository.createAnnouncement(announcementsDto);
//
//        // Create a new AnnouncementsDto with updated title and description
//        announcementsDto = new AnnouncementsDto(
//                announcementsDto.getUserName(),
//                announcementsDto.getUserUserName(),
//                announcementsDto.getUserEmail(),
//                announcementsDto.getUserAvatarUrl(),
//                "Updated Announcement",    // Updated title
//                "This announcement has been updated.", // Updated description
//                announcementsDto.getDateOfAnnouncement(),  // Preserve dateOfAnnouncement
//                announcementsDto.getMedia(),
//                announcementsDto.getGroupsListNames(),
//                announcementsDto.getUsers()
//        );
//
//        // Call the editAnnouncement method to update the announcement
//        AnnouncementsRepository.editAnnouncement(1L, announcementsDto);
//
//        // Fetch the updated announcement from the database
//        Pageable pageable = PageRequest.of(0, 10);
//        List<AnnouncementsDto> announcements = AnnouncementsRepository.getAllAnnouncements(pageable);
//
//        // Assert that the announcement was updated correctly
//        assertThat(announcements).isNotEmpty();
//        assertThat(announcements.get(0).getTitle()).isEqualTo("Updated Announcement");
//        assertThat(announcements.get(0).getDescription()).isEqualTo("This announcement has been updated.");
//    }
//
//
//
//    @Test
//    @Order(3)
//    public void testDeleteAnnouncement() {
//        // Create a test AnnouncementsDto
//        AnnouncementsDto announcementsDto = new AnnouncementsDto(
//                "user1",                   // userUserName
//                "User One",                // userFullName
//                "user1@example.com",       // userEmail
//                "https://example.com/avatar.jpg",  // userAvatarUrl
//                "Announcement to delete",  // title
//                "This announcement will be deleted.", // description
//                null,                      // media
//                null,                      // groupsList
//                null,
//                null// users
//        );
//
//        // Call the createAnnouncement method first to insert an announcement
//        AnnouncementsRepository.createAnnouncement(announcementsDto);
//
//        // Delete the created announcement
//        AnnouncementsRepository.deleteAnnouncement(1L);
//
//        // Fetch the announcement from the database
//        Pageable pageable = PageRequest.of(0, 10);
//        List<AnnouncementsDto> announcements = AnnouncementsRepository.getAllAnnouncements(pageable);
//
//        // Assert that the announcement has been deleted
//        assertThat(announcements).isEmpty();
//    }
//
//    @Test
//    @Order(4)
//    public void testGetAllAnnouncements() {
//        // Create and save multiple AnnouncementsDto objects
//        AnnouncementsDto announcementsDto1 = new AnnouncementsDto(
//                "user1",                   // userUserName
//                "User One",                // userFullName
//                "user1@example.com",       // userEmail
//                "https://example.com/avatar1.jpg",  // userAvatarUrl
//                "Announcement 1",          // title
//                "This is the first announcement.",   // description
//                null,                      // media
//                null,                      // groupsList
//                null,
//                null// users
//        );
//
//        AnnouncementsDto announcementsDto2 = new AnnouncementsDto(
//                "user2",                   // userUserName
//                "User Two",                // userFullName
//                "user2@example.com",       // userEmail
//                "https://example.com/avatar2.jpg",  // userAvatarUrl
//                "Announcement 2",          // title
//                "This is the second announcement.",  // description
//                null,                      // media
//                null,                      // groupsList
//                null,
//                null// users
//        );
//
//        // Save the announcements
//        AnnouncementsRepository.createAnnouncement(announcementsDto1);
//        AnnouncementsRepository.createAnnouncement(announcementsDto2);
//
//        // Fetch all announcements from the repository
//        Pageable pageable = PageRequest.of(0, 10);
//        List<AnnouncementsDto> announcements = AnnouncementsRepository.getAllAnnouncements(pageable);
//
//        // Assert that both announcements are returned
//        assertThat(announcements).hasSize(2);
//        assertThat(announcements.get(0).getTitle()).isEqualTo("Announcement 1");
//        assertThat(announcements.get(1).getTitle()).isEqualTo("Announcement 2");
//    }
//}
