package in.ac.iitj.instiapp.Tests.InitialiseEntities;

import in.ac.iitj.instiapp.Repository.LostnFoundRepository;
import in.ac.iitj.instiapp.Repository.MediaRepository;
import in.ac.iitj.instiapp.Repository.UserRepository;
import in.ac.iitj.instiapp.Repository.impl.LostnFoundRepositoryImpl;
import in.ac.iitj.instiapp.Repository.impl.MediaRepositoryImpl;
import in.ac.iitj.instiapp.Repository.impl.UserRepositoryImpl;
import in.ac.iitj.instiapp.Tests.EntityTestData.LocationData;
import in.ac.iitj.instiapp.Tests.EntityTestData.LostnFoundData;
import in.ac.iitj.instiapp.Tests.InitialiseEntities.User.InitialiseUser;
import in.ac.iitj.instiapp.Tests.Utilities.InitialiseEntities;
import in.ac.iitj.instiapp.database.entities.LostnFound.Locations;
import in.ac.iitj.instiapp.database.entities.LostnFound.LostnFound;
import in.ac.iitj.instiapp.database.entities.Media.Media;
import in.ac.iitj.instiapp.database.entities.User.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import static in.ac.iitj.instiapp.Tests.EntityTestData.MediaData.*;
import static in.ac.iitj.instiapp.Tests.EntityTestData.UserData.*;

@Component
@Import({LostnFoundRepositoryImpl.class , MediaRepositoryImpl.class , UserRepositoryImpl.class , InitialiseUser.class , InitialiseMedia.class})
public class InitialiseLostnFound implements InitialiseEntities.Initialise {
    private final LostnFoundRepository lostnFoundRepository;
    private final UserRepository userRepository;
    private final MediaRepository mediaRepository;

    @Autowired
    public InitialiseLostnFound(LostnFoundRepository lostnFoundRepository, UserRepository userRepository , MediaRepository mediaRepository , InitialiseUser initialiseUser , InitialiseMedia initialiseMedia) {
        this.lostnFoundRepository = lostnFoundRepository;
        this.userRepository = userRepository;
        this.mediaRepository = mediaRepository;
        initialiseUser.initialise();
        initialiseMedia.initialise();


    }

    @Override
    @Transactional
    public void initialise() {
        lostnFoundRepository.saveLocation(LocationData.LOCATION1.toEntity());
        lostnFoundRepository.saveLocation(LocationData.LOCATION2.toEntity());
        lostnFoundRepository.saveLocation(LocationData.LOCATION3.toEntity());

        LostnFound lost1 = LostnFoundData.LOST_N_FOUND1.toEntity();
        LostnFound lost2 = LostnFoundData.LOST_N_FOUND2.toEntity();
        LostnFound lost3 = LostnFoundData.LOST_N_FOUND3.toEntity();

        lost3.setFinder(new User(userRepository.usernameExists(USER14.userName)));

        lost1.setOwner(new User(userRepository.usernameExists(USER14.userName)));
        lost2.setOwner(new User(userRepository.usernameExists(USER15.userName)));
        lost3.setOwner(new User(userRepository.usernameExists(USER16.userName)));

        lost1.setLandmark(new Locations(lostnFoundRepository.existLocation(LocationData.LOCATION1.name)));
        lost2.setLandmark(new Locations(lostnFoundRepository.existLocation(LocationData.LOCATION2.name)));
        lost3.setLandmark(new Locations(lostnFoundRepository.existLocation(LocationData.LOCATION3.name)));

        lost1.setMedia(new Media(mediaRepository.getIdByPublicId(MEDIA1.publicId)));
        lost2.setMedia(new Media(mediaRepository.getIdByPublicId(MEDIA2.publicId)));
        lost3.setMedia(new Media(mediaRepository.getIdByPublicId(MEDIA3.publicId)));

        lostnFoundRepository.saveLostnFoundDetails(lost1);
        lostnFoundRepository.saveLostnFoundDetails(lost2);
        lostnFoundRepository.saveLostnFoundDetails(lost3);

    }
}
