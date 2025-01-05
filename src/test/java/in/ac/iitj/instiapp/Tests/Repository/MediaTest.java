package in.ac.iitj.instiapp.Tests.Repository;

import in.ac.iitj.instiapp.Repository.MediaRepository;
import in.ac.iitj.instiapp.Repository.impl.MediaRepositoryImpl;
import in.ac.iitj.instiapp.Tests.EntityTestData.MediaData;
import in.ac.iitj.instiapp.payload.Media.MediaBaseDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@Import({MediaRepositoryImpl.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MediaTest {
    @Autowired
    MediaRepository mediaRepository;
    
    @BeforeAll
    public static void setUp(@Autowired MediaRepository mediaRepository) {
        mediaRepository.save(MediaData.MEDIA1.toEntity());
        mediaRepository.save(MediaData.MEDIA2.toEntity());
    }

    @Order(1)
    @Test
    public void testGetByPublicId(){
        MediaBaseDto mediadata = mediaRepository.findByPublicId(MediaData.MEDIA1.publicId);
        assertEquals(mediadata.getPublicUrl(), MediaData.MEDIA1.publicUrl);
        assertEquals(mediadata.getType() , MediaData.MEDIA1.mediaType.toEntity());
                
    }

    @Order(2)
    @Test
    public void testGetIDByPublicId(){
        Long id = mediaRepository.getIdByPublicId(MediaData.MEDIA1.publicId);
        Long id2 = mediaRepository.getIdByPublicId(MediaData.MEDIA2.publicId);
        Long id3 = mediaRepository.getIdByPublicId(MediaData.MEDIA3.publicId);
        assertEquals(-1 , id3);
        assertNotNull(id);
        assertNotNull(id2);
        assertNotEquals(-1 , id2);
        assertNotEquals(-1 , id);
    }

    @Order(3)
    @Test
    public void testDeleteByPublicId(){
        mediaRepository.delete(MediaData.MEDIA1.publicId);
        Long id = mediaRepository.getIdByPublicId(MediaData.MEDIA1.publicId);
        assertEquals(-1 , id);
    }
}
