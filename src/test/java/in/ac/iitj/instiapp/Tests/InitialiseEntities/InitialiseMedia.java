package in.ac.iitj.instiapp.Tests.InitialiseEntities;

import in.ac.iitj.instiapp.Repository.MediaRepository;
import in.ac.iitj.instiapp.Repository.impl.MediaRepositoryImpl;
import in.ac.iitj.instiapp.Tests.Utilities.InitialiseEntities;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import static in.ac.iitj.instiapp.Tests.EntityTestData.MediaData.*;


@Component
@Import({MediaRepositoryImpl.class})
public class InitialiseMedia implements InitialiseEntities.Initialise {
    private final MediaRepository mediaRepository;

    @Autowired
    public InitialiseMedia(MediaRepository mediaRepository){
        this.mediaRepository = mediaRepository;
    }



    @Transactional
    public void initialise(){

        // For organisation
        mediaRepository.save(MEDIA1.toEntity());
        mediaRepository.save(MEDIA2.toEntity());
        mediaRepository.save(MEDIA3.toEntity());



        // In database but should be unassigned for testing purpose
        mediaRepository.save(MEDIA5.toEntity());
        mediaRepository.save(MEDIA6.toEntity());
        mediaRepository.save(MEDIA7.toEntity());



        mediaRepository.save(MEDIA8.toEntity());
        mediaRepository.save(MEDIA9.toEntity());
        mediaRepository.save(MEDIA10.toEntity());



    }
}
