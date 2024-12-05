
package in.ac.iitj.instiapp.Tests.Service;
import in.ac.iitj.instiapp.Tests.BaseTestConfig;
import in.ac.iitj.instiapp.config.CloudinaryConfig;
import in.ac.iitj.instiapp.services.CloudinaryService;
import in.ac.iitj.instiapp.services.impl.CloudinaryServiceImpl;
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

@ContextConfiguration(classes = { TestConfig.class, BaseTestConfig.class})
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CloudinaryServiceTest {

    @Autowired
    CloudinaryService cloudinaryService;

    private static Map uploadedFileInfo;

    @BeforeAll
    static void setUp(@Autowired CloudinaryService cloudinaryService) {
        try {
            ClassPathResource resource = new ClassPathResource("image.jpg");
            byte[] fileContent = Files.readAllBytes(resource.getFile().toPath());
            uploadedFileInfo = cloudinaryService.uploadFile(fileContent, "tests");
        } catch (Exception e) {
            fail("Failed to setup test: " + e.getMessage());
        }
    }

    @Test
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
    @DisplayName("Delete Image")
    void deleteUploadedFile() {
        Map response = cloudinaryService.deleteFile(uploadedFileInfo.get("public_id").toString());
        assertEquals("ok", response.get("result"));
    }

    @AfterAll
    static void cleanup(@Autowired CloudinaryService cloudinaryService) {
        // In case test fails before deletion
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
class TestConfig{

    @Bean
    public  CloudinaryService cloudinaryService(){
        return  new CloudinaryServiceImpl();
    }
}
