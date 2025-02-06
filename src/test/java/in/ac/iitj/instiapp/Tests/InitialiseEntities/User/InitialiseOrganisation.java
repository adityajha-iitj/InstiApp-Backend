package in.ac.iitj.instiapp.Tests.InitialiseEntities.User;

import in.ac.iitj.instiapp.Repository.MediaRepository;
import in.ac.iitj.instiapp.Repository.User.Organisation.OrganisationRepository;
import in.ac.iitj.instiapp.Repository.UserRepository;
import in.ac.iitj.instiapp.Repository.impl.OrganisationRepositoryImpl;
import in.ac.iitj.instiapp.Tests.InitialiseEntities.InitialiseMedia;
import in.ac.iitj.instiapp.Tests.Utilities.InitialiseEntities;
import in.ac.iitj.instiapp.database.entities.Media.Media;
import in.ac.iitj.instiapp.database.entities.User.Organisation.Organisation;
import in.ac.iitj.instiapp.database.entities.User.Organisation.OrganisationType;
import in.ac.iitj.instiapp.database.entities.User.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static in.ac.iitj.instiapp.Tests.EntityTestData.MediaData.*;
import static in.ac.iitj.instiapp.Tests.EntityTestData.OrganisationData.*;
import static in.ac.iitj.instiapp.Tests.EntityTestData.OrganisationTypeData.*;
import static in.ac.iitj.instiapp.Tests.EntityTestData.UserData.*;

@Component
@Import({OrganisationRepositoryImpl.class, InitialiseUser.class, InitialiseMedia.class})
public class InitialiseOrganisation implements InitialiseEntities.Initialise {

    private final OrganisationRepository organisationRepository;
    private final UserRepository userRepository;
    private final MediaRepository mediaRepository;



    @Autowired
    public InitialiseOrganisation(OrganisationRepository organisationRepository, UserRepository userRepository, MediaRepository mediaRepository, InitialiseUser initialiseUser,InitialiseMedia initialiseMedia) {
        this.organisationRepository = organisationRepository;
        this.userRepository = userRepository;
        this.mediaRepository = mediaRepository;

        initialiseUser.initialise();
        initialiseMedia.initialise();
    }


    @Transactional
    public void initialise(){



        organisationRepository.saveOrganisationType(ORGANISATION_TYPE1.toEntity());
        organisationRepository.saveOrganisationType(ORGANISATION_TYPE2.toEntity());
        organisationRepository.saveOrganisationType(ORGANISATION_TYPE3.toEntity());



        Long OrganisationType1Id = organisationRepository.existsOrganisationType(ORGANISATION_TYPE1.name);
        Long OrganisationType2Id = organisationRepository.existsOrganisationType(ORGANISATION_TYPE2.name);
        Long OrganisationType3Id = organisationRepository.existsOrganisationType(ORGANISATION_TYPE3.name);

        Organisation organisation1 = ORGANISATION1.toEntity();
        Organisation organisation2 = ORGANISATION2.toEntity();
        Organisation organisation3 = ORGANISATION3.toEntity();


        Long userId1 = userRepository.usernameExists(USER1.userName);
        Long userId2 = userRepository.usernameExists(USER2.userName);
        Long userId3 = userRepository.usernameExists(USER3.userName);


        organisation1.setType(new OrganisationType(OrganisationType1Id));
        organisation2.setType(new OrganisationType(OrganisationType2Id));
        organisation3.setType(new OrganisationType(OrganisationType3Id));

        organisation1.setUser(new User(userId1));
        organisation2.setUser(new User(userId2));
        organisation3.setUser(new User(userId3));



        organisation1.setParentOrganisation(new Organisation(null));
        organisation2.setParentOrganisation(new Organisation(null));


        Long mediaId1 = mediaRepository.getIdByPublicId(MEDIA1.publicId);
        Long mediaId2 = mediaRepository.getIdByPublicId(MEDIA2.publicId);
        Long mediaId3 = mediaRepository.getIdByPublicId(MEDIA3.publicId);

        organisation1.setMedia(Arrays.asList(new Media(mediaId1)));
        organisation2.setMedia(Arrays.asList(new Media(mediaId2)));
        organisation3.setMedia(Arrays.asList(new Media(mediaId3)));


        organisationRepository.save(organisation1);

        organisation3.setParentOrganisation(new Organisation(organisationRepository.existOrganisation(USER1.userName)));

        organisationRepository.save(organisation2);
        organisationRepository.save(organisation3);
    }
}
