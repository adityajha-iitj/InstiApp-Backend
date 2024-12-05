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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = {TestConfig.class})
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CloudinaryServiceTest {

    private static Map uploadedFileInfo;  // Make this static so it's shared across all tests
    @Autowired
    CloudinaryService cloudinaryService;

    @BeforeAll
    static void setUp(@Autowired CloudinaryService cloudinaryService) {
        if (uploadedFileInfo == null) {

            // Load the image only once before any tests run
            try {


                ClassPathResource resource = new ClassPathResource("img.png");  // Ensure this image exists in src/test/resources
                byte[] fileContent = Files.readAllBytes(resource.getFile().toPath());
                Optional<Map<Object, Object>> uploadedFileInfoOptional = cloudinaryService.uploadFile(fileContent, "tests").get();
                if (uploadedFileInfoOptional.isEmpty()) {
                   fail();
                }
                uploadedFileInfoOptional.ifPresent(map -> uploadedFileInfo = map);
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }
    }

    @AfterAll
    static void cleanup(@Autowired CloudinaryService cloudinaryService) {
        // Cleanup after all tests are done
        if (uploadedFileInfo != null && uploadedFileInfo.get("public_id") != null) {
            try {
                cloudinaryService.deleteFileAsync(uploadedFileInfo.get("public_id").toString());
            } catch (Exception e) {
                System.err.println("Cleanup failed: " + e.getMessage());
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
        try {
            Optional<Map<Object, Object>> response = cloudinaryService.deleteFileAsync(uploadedFileInfo.get("public_id").toString()).get();
            response.ifPresentOrElse(objectObjectMap -> {
                assertEquals("ok", objectObjectMap.get("result"));
            }, () -> {
                assert false;
            });
        } catch (Exception e) {
            e.printStackTrace();
           fail();
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
