package in.ac.iitj.instiapp.Tests.InitialiseEntities.User;


import in.ac.iitj.instiapp.Repository.FacultyRepository;
import in.ac.iitj.instiapp.Repository.User.Organisation.OrganisationRepository;
import in.ac.iitj.instiapp.Repository.UserRepository;
import in.ac.iitj.instiapp.Repository.impl.FacultyRepositoryImpl;
import in.ac.iitj.instiapp.Repository.impl.OrganisationRepositoryImpl;
import in.ac.iitj.instiapp.Repository.impl.UserRepositoryImpl;
import in.ac.iitj.instiapp.Tests.EntityTestData.FacultyData;
import in.ac.iitj.instiapp.Tests.Utilities.InitialiseEntities;
import in.ac.iitj.instiapp.database.entities.User.Faculty.Faculty;
import in.ac.iitj.instiapp.database.entities.User.Organisation.Organisation;
import in.ac.iitj.instiapp.database.entities.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import static in.ac.iitj.instiapp.Tests.EntityTestData.UserData.*;

@Component
@Import({FacultyRepositoryImpl.class,InitialiseOrganisation.class,  UserRepositoryImpl.class, OrganisationRepositoryImpl.class})
public class InitialiseFaculty implements InitialiseEntities.Initialise {
    private final FacultyRepository facultyRepository;
    private final UserRepository userRepository;
    private final OrganisationRepository organisationRepository;

    @Autowired
    public InitialiseFaculty(FacultyRepository facultyRepository, UserRepository userRepository , OrganisationRepository organisationRepository, InitialiseOrganisation initialiseOrganisation) {
        this.facultyRepository = facultyRepository;
        this.userRepository = userRepository;
        this.organisationRepository = organisationRepository;

        initialiseOrganisation.initialise();
    }

    @Override
    public void initialise() {
        Faculty faculty1 = FacultyData.FACULTY1.toEntity();
        Faculty faculty2 = FacultyData.FACULTY2.toEntity();
        Faculty faculty3 = FacultyData.FACULTY3.toEntity();

        faculty1.setUser(new User(userRepository.usernameExists(USER11.userName)));
        faculty2.setUser(new User(userRepository.usernameExists(USER12.userName)));
        faculty3.setUser(new User(userRepository.usernameExists(USER13.userName)));

        faculty1.setOrganisation(new Organisation(organisationRepository.existOrganisation(USER1.userName)));
        faculty2.setOrganisation(new Organisation(organisationRepository.existOrganisation(USER2.userName)));
        faculty3.setOrganisation(new Organisation(organisationRepository.existOrganisation(USER3.userName)));


        facultyRepository.save(faculty1);
        facultyRepository.save(faculty2);
        facultyRepository.save(faculty3);
    }
}