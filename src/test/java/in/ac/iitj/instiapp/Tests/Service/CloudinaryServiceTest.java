package in.ac.iitj.instiapp.Tests.Service;

import in.ac.iitj.instiapp.config.CloudinaryConfig;
import in.ac.iitj.instiapp.services.CloudinaryService;
import in.ac.iitj.instiapp.services.impl.CloudinaryServiceImpl;
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.nio.file.Files;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = {TestConfig.class})
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CloudinaryServiceTest {

    @Autowired
    CloudinaryService cloudinaryService;

    private static Map uploadedFileInfo;  // Make this static so it's shared across all tests

    @BeforeAll
    static void setUp(@Autowired CloudinaryService cloudinaryService) {
        if (uploadedFileInfo == null) {
            try {
                // Load the image only once before any tests run
                ClassPathResource resource = new ClassPathResource("img.png");  // Ensure this image exists in src/test/resources
                byte[] fileContent = Files.readAllBytes(resource.getFile().toPath());
                uploadedFileInfo = cloudinaryService.uploadFile(fileContent, "tests");
            } catch (Exception e) {
                fail("Failed to setup test: " + e.getMessage());
            }
        }
    }

    @Test
    @Order(1)
    @DisplayName("Verify Upload Result")
    void verifyUploadResult() {
        assertAll(
                () -> assertNotNull(uploadedFileInfo.get("public_id"), "public_id is null"),
                () -> assertNotNull(uploadedFileInfo.get("url"), "url is null"),
                () -> assertNotNull(uploadedFileInfo.get("asset_id"), "asset_id is null"),
                () -> assertTrue(uploadedFileInfo.get("url").toString().startsWith("http"),
                        "URL should start with http")
        );
    }

    @Test
    @Order(2)
    @DisplayName("Delete Image")
    void deleteUploadedFile() {
        Map response = cloudinaryService.deleteFile(uploadedFileInfo.get("public_id").toString());
        assertEquals("ok", response.get("result"));
    }

    @AfterAll
    static void cleanup(@Autowired CloudinaryService cloudinaryService) {
        // Cleanup after all tests are done
        if (uploadedFileInfo != null && uploadedFileInfo.get("public_id") != null) {
            try {
                cloudinaryService.deleteFile(uploadedFileInfo.get("public_id").toString());
            } catch (Exception e) {
                System.err.println("Cleanup failed: " + e.getMessage());
            }
        }
    }
}

@TestConfiguration
@Import(CloudinaryConfig.class)
class TestConfig {
    @Bean
    public CloudinaryService cloudinaryService() {
        return new CloudinaryServiceImpl();
    }

    @Bean
    public Dotenv dotenv() {
        return Dotenv.load();
    }
}
