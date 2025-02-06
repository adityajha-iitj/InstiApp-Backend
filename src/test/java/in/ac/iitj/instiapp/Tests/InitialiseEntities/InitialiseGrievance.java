package in.ac.iitj.instiapp.Tests.InitialiseEntities;

import in.ac.iitj.instiapp.Repository.GrievanceRepository;
import in.ac.iitj.instiapp.Repository.MediaRepository;
import in.ac.iitj.instiapp.Repository.User.Organisation.OrganisationRoleRepository;
import in.ac.iitj.instiapp.Repository.UserRepository;
import in.ac.iitj.instiapp.Repository.impl.GrievanceRepositoryImpl;
import in.ac.iitj.instiapp.Tests.EntityTestData.GrievanceData;
import in.ac.iitj.instiapp.Tests.EntityTestData.OrganisationRoleData;
import in.ac.iitj.instiapp.Tests.InitialiseEntities.User.InitialiseOrganisationRole;
import in.ac.iitj.instiapp.Tests.Utilities.InitialiseEntities;
import in.ac.iitj.instiapp.database.entities.Grievance;
import in.ac.iitj.instiapp.database.entities.Media.Media;
import in.ac.iitj.instiapp.database.entities.User.Organisation.OrganisationRole;
import in.ac.iitj.instiapp.database.entities.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import static in.ac.iitj.instiapp.Tests.EntityTestData.MediaData.*;
import static in.ac.iitj.instiapp.Tests.EntityTestData.UserData.*;
@Component
@Import({GrievanceRepositoryImpl.class, InitialiseMedia.class, InitialiseOrganisationRole.class})
public  class InitialiseGrievance implements InitialiseEntities.Initialise {
    private final GrievanceRepository grievanceRepository;
    private final UserRepository userRepository;
    private final OrganisationRoleRepository organisationRoleRepository;
    private final MediaRepository mediaRepository;

    @Autowired
    public InitialiseGrievance(GrievanceRepository grievanceRepository , OrganisationRoleRepository organisationRoleRepository, MediaRepository mediaRepository, UserRepository userRepository, InitialiseMedia initialiseMedia, InitialiseOrganisationRole initialiseOrganisationRole) {
        this.grievanceRepository = grievanceRepository;
        this.organisationRoleRepository = organisationRoleRepository;
        this.mediaRepository = mediaRepository;
        this.userRepository = userRepository;

        initialiseMedia.initialise();
        initialiseOrganisationRole.initialise();

    }

    @Override
    public void initialise() {

        Grievance grievance1 = GrievanceData.GRIEVANCE1.toEntity();
        Grievance grievance2 = GrievanceData.GRIEVANCE2.toEntity();
        Grievance grievance3 = GrievanceData.GRIEVANCE3.toEntity();

        grievance1.setUserFrom(new User(userRepository.usernameExists(USER5.userName)));
        grievance2.setUserFrom(new User(userRepository.usernameExists(USER6.userName)));
        grievance3.setUserFrom(new User(userRepository.usernameExists(USER7.userName)));

        grievance1.setOrganisationRole(new OrganisationRole(organisationRoleRepository.existOrganisationRole(USER1.userName, OrganisationRoleData.ORGANISATION_ROLE1.roleName)));
        grievance2.setOrganisationRole(new OrganisationRole(organisationRoleRepository.existOrganisationRole(USER2.userName, OrganisationRoleData.ORGANISATION_ROLE2.roleName)));
        grievance3.setOrganisationRole(new OrganisationRole(organisationRoleRepository.existOrganisationRole(USER3.userName, OrganisationRoleData.ORGANISATION_ROLE3.roleName)));

        grievance1.setMedia(new Media(mediaRepository.getIdByPublicId(MEDIA1.publicId)));
        grievance2.setMedia(new Media(mediaRepository.getIdByPublicId(MEDIA2.publicId)));
        grievance3.setMedia(new Media(mediaRepository.getIdByPublicId(MEDIA3.publicId)));

        grievanceRepository.save(grievance1);
        grievanceRepository.save(grievance2);
        grievanceRepository.save(grievance3);
    }

}


