package in.ac.iitj.instiapp.Tests.Utilities;

import in.ac.iitj.instiapp.Repository.*;
import in.ac.iitj.instiapp.Repository.User.Organisation.OrganisationRepository;
import in.ac.iitj.instiapp.Repository.User.Student.Alumni.AlumniRepository;
import in.ac.iitj.instiapp.Repository.FacultyRepository;
import in.ac.iitj.instiapp.Repository.UserRepository;
import in.ac.iitj.instiapp.Repository.User.Student.Student.StudentRepository;
import in.ac.iitj.instiapp.Repository.User.Student.StudentBranchRepository;
import in.ac.iitj.instiapp.Repository.User.Student.StudentProgramRepository;
import in.ac.iitj.instiapp.Repository.GrievanceRepository;
import in.ac.iitj.instiapp.Repository.impl.*;
import in.ac.iitj.instiapp.Tests.EntityTestData.*;
import in.ac.iitj.instiapp.database.entities.LostnFound.Locations;
import in.ac.iitj.instiapp.database.entities.LostnFound.LostnFound;
import in.ac.iitj.instiapp.database.entities.Grievance;
import in.ac.iitj.instiapp.database.entities.Media.Media;
import in.ac.iitj.instiapp.database.entities.Scheduling.Calendar.Calendar;
import in.ac.iitj.instiapp.database.entities.User.Organisation.Organisation;
import in.ac.iitj.instiapp.database.entities.User.Organisation.OrganisationRole;
import in.ac.iitj.instiapp.database.entities.User.Organisation.OrganisationType;
import in.ac.iitj.instiapp.database.entities.User.Student.Alumni.Alumni;
import in.ac.iitj.instiapp.database.entities.User.Student.Student.Student;
import in.ac.iitj.instiapp.database.entities.User.Student.StudentBranch;
import in.ac.iitj.instiapp.database.entities.User.Student.StudentProgram;
import in.ac.iitj.instiapp.database.entities.User.User;
import in.ac.iitj.instiapp.database.entities.User.Usertype;
import in.ac.iitj.instiapp.database.entities.User.Faculty.Faculty;
import in.ac.iitj.instiapp.Repository.User.Organisation.OrganisationRoleRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static in.ac.iitj.instiapp.Tests.EntityTestData.CalendarData.*;
import static in.ac.iitj.instiapp.Tests.EntityTestData.MediaData.*;
import static in.ac.iitj.instiapp.Tests.EntityTestData.OrganisationData.*;
import static in.ac.iitj.instiapp.Tests.EntityTestData.OrganisationTypeData.*;

import static in.ac.iitj.instiapp.Tests.EntityTestData.StudentBranchData.*;
import static in.ac.iitj.instiapp.Tests.EntityTestData.UserData.*;
import static in.ac.iitj.instiapp.Tests.EntityTestData.UserTypeData.*;
import static in.ac.iitj.instiapp.Tests.EntityTestData.StudentProgramData.*;
import static in.ac.iitj.instiapp.Tests.EntityTestData.StudentData.*;
import static in.ac.iitj.instiapp.Tests.EntityTestData.AlumniData.*;

public class InitialiseEntities {

    public  interface Initialise{
        void initialise();
    }


    @Component
    @Import({MediaRepositoryImpl.class})
    public static class InitialiseMedia implements Initialise{


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



    @Component
    @Import({UserRepositoryImpl.class, CalendarRepositoryImpl.class, UserTypeRepositoryImpl.class, GrievanceRepositoryImpl.class})
    public static class InitialiseUser implements Initialise {

        private final CalendarRepository calendarRepository;
        private final UserRepository userRepository;
        private final UserTypeRepository userTypeRepository;
        private final GrievanceRepository grievanceRepository;


        @Autowired
        public InitialiseUser(CalendarRepository calendarRepository, UserRepository userRepository, UserTypeRepository userTypeRepository,  GrievanceRepository grievanceRepository) {
            this.userRepository = userRepository;
            this.userTypeRepository = userTypeRepository;
            this.calendarRepository = calendarRepository;
            this.grievanceRepository =grievanceRepository;
        }


        @Transactional
        public void initialise() {
            calendarRepository.save(CALENDAR1.toEntity());
            calendarRepository.save(CALENDAR2.toEntity());
            calendarRepository.save(CALENDAR3.toEntity());
            calendarRepository.save(CALENDAR5.toEntity());
            calendarRepository.save(CALENDAR6.toEntity());
            calendarRepository.save(CALENDAR7.toEntity());
            calendarRepository.save(CALENDAR8.toEntity());
            calendarRepository.save(CALENDAR9.toEntity());
            calendarRepository.save(CALENDAR10.toEntity());
            calendarRepository.save(CALENDAR11.toEntity());
            calendarRepository.save(CALENDAR12.toEntity());
            calendarRepository.save(CALENDAR13.toEntity());
            calendarRepository.save(CALENDAR14.toEntity());
            calendarRepository.save(CALENDAR15.toEntity());
            calendarRepository.save(CALENDAR16.toEntity());

            userTypeRepository.save(USER_TYPE1.toEntity());
            userTypeRepository.save(USER_TYPE2.toEntity());
            userTypeRepository.save(USER_TYPE3.toEntity());
            userTypeRepository.save(USER_TYPE5.toEntity());
            userTypeRepository.save(USER_TYPE6.toEntity());
            userTypeRepository.save(USER_TYPE7.toEntity());




            Long UserType1Id = userRepository.userTypeExists(USER_TYPE1.name);
            Long UserType2Id = userRepository.userTypeExists(USER_TYPE2.name);
            Long UserType3Id = userRepository.userTypeExists(USER_TYPE3.name);
            Long UserType5Id = userRepository.userTypeExists(USER_TYPE5.name);
            Long UserType6Id = userRepository.userTypeExists(USER_TYPE6.name);
            Long UserType7Id = userRepository.userTypeExists(USER_TYPE7.name);




            User user1 = USER1.toEntity();
            User user2 = USER2.toEntity();
            User user3 = USER3.toEntity();
            User user5 = USER5.toEntity();
            User user6 = USER6.toEntity();
            User user7 = USER7.toEntity();
            User user8 = USER8.toEntity();
            User user9 = USER9.toEntity();
            User user10 = USER10.toEntity();
            User user11 = USER11.toEntity();
            User user12 = USER12.toEntity();
            User user13 = USER13.toEntity();
            User user14 = USER14.toEntity();
            User user15 = USER15.toEntity();
            User user16 = USER16.toEntity();

            user1.setCalendar(new Calendar(calendarRepository.calendarExists(CALENDAR1.publicId)));
            user2.setCalendar(new Calendar(calendarRepository.calendarExists(CALENDAR2.publicId)));
            user3.setCalendar(new Calendar(calendarRepository.calendarExists(CALENDAR3.publicId)));
            user5.setCalendar(new Calendar(calendarRepository.calendarExists(CALENDAR5.publicId)));
            user6.setCalendar(new Calendar(calendarRepository.calendarExists(CALENDAR6.publicId)));
            user7.setCalendar(new Calendar(calendarRepository.calendarExists(CALENDAR7.publicId)));
            user8.setCalendar(new Calendar(calendarRepository.calendarExists(CALENDAR8.publicId)));
            user9.setCalendar(new Calendar(calendarRepository.calendarExists(CALENDAR9.publicId)));
            user10.setCalendar(new Calendar(calendarRepository.calendarExists(CALENDAR10.publicId)));
            user11.setCalendar(new Calendar(calendarRepository.calendarExists(CALENDAR11.publicId)));
            user12.setCalendar(new Calendar(calendarRepository.calendarExists(CALENDAR12.publicId)));
            user13.setCalendar(new Calendar(calendarRepository.calendarExists(CALENDAR13.publicId)));
            user14.setCalendar(new Calendar(calendarRepository.calendarExists(CALENDAR14.publicId)));
            user15.setCalendar(new Calendar(calendarRepository.calendarExists(CALENDAR15.publicId)));
            user16.setCalendar(new Calendar(calendarRepository.calendarExists(CALENDAR16.publicId)));

//            For organisation
            user1.setUserType(new Usertype(UserType1Id));
            user2.setUserType(new Usertype(UserType2Id));
            user3.setUserType(new Usertype(UserType3Id));



//            For student
            user5.setUserType(new Usertype(UserType5Id));
            user6.setUserType(new Usertype(UserType5Id));
            user7.setUserType(new Usertype(UserType5Id));


//            For alumni
            user8.setUserType(new Usertype(UserType6Id));
            user9.setUserType(new Usertype(UserType6Id));
            user10.setUserType(new Usertype(UserType6Id));

//            For Faculty
            user11.setUserType(new Usertype(UserType7Id));
            user12.setUserType(new Usertype(UserType7Id));
            user13.setUserType(new Usertype(UserType7Id));

//          for lostandfound
            user14.setUserType(new Usertype(UserType7Id));
            user15.setUserType(new Usertype(UserType7Id));
            user16.setUserType(new Usertype(UserType7Id));

            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.save(user3);
            userRepository.save(user5);
            userRepository.save(user6);
            userRepository.save(user7);
            userRepository.save(user8);
            userRepository.save(user9);
            userRepository.save(user10);
            userRepository.save(user11);
            userRepository.save(user12);
            userRepository.save(user13);
            userRepository.save(user14);
            userRepository.save(user15);
            userRepository.save(user16);

        }

    }


    @Component
    @Import({OrganisationRepositoryImpl.class, InitialiseEntities.InitialiseUser.class,InitialiseMedia.class})
    public static class InitialiseOrganisation implements Initialise{

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

    @Component
    @Import({OrganisationRoleRepositoryImpl.class , InitialiseOrganisation.class})
    public static class InitialiseOrganisationRole implements Initialise{


        private final OrganisationRoleRepository organisationRoleRepository;
        private final OrganisationRepository organisationRepository;



        @Autowired
        public InitialiseOrganisationRole(OrganisationRoleRepository organisationRoleRepository, OrganisationRepository organisationRepository, InitialiseOrganisation initialiseOrganisation) {
            this.organisationRoleRepository = organisationRoleRepository;
            this.organisationRepository = organisationRepository;

            initialiseOrganisation.initialise();

        }


        @Transactional
        public void initialise(){
            OrganisationRole organisationRole1 = OrganisationRoleData.ORGANISATION_ROLE1.toEntity();
            OrganisationRole organisationRole2 = OrganisationRoleData.ORGANISATION_ROLE2.toEntity();
            OrganisationRole organisationRole3 = OrganisationRoleData.ORGANISATION_ROLE3.toEntity();

            organisationRole1.setOrganisation(new Organisation(organisationRepository.existOrganisation(USER1.userName)));
            organisationRole2.setOrganisation(new Organisation(organisationRepository.existOrganisation(USER2.userName)));
            organisationRole3.setOrganisation(new Organisation(organisationRepository.existOrganisation(USER3.userName)));

            organisationRoleRepository.saveOrganisationRole(organisationRole1);
            organisationRoleRepository.saveOrganisationRole(organisationRole2);
            organisationRoleRepository.saveOrganisationRole(organisationRole3);

        }

    }



    @Component
    @Import({InitialiseOrganisation.class, StudentBranchRepositoryImpl.class, StudentProgramRepositoryImpl.class})
    public static class InitialiseProgramAndBranch implements Initialise{

        private final StudentBranchRepository studentBranchRepository;
        private final StudentProgramRepository studentProgramRepository;
        private final OrganisationRepository organisationRepository;

        public InitialiseProgramAndBranch(StudentBranchRepository studentBranchRepository, StudentProgramRepository studentProgramRepository, OrganisationRepository organisationRepository,InitialiseOrganisation initialiseOrganisation) {
            this.studentBranchRepository = studentBranchRepository;
            this.studentProgramRepository = studentProgramRepository;
            this.organisationRepository = organisationRepository;


            initialiseOrganisation.initialise();
        }

        @Override
        @Transactional
        public void initialise() {
            StudentBranch studentBranch1 = STUDENT_BRANCH1.toEntity();
            studentBranch1.setOrganisation(new Organisation(organisationRepository.existOrganisation(USER1.userName)));

            StudentBranch studentBranch2 = STUDENT_BRANCH2.toEntity();
            studentBranch2.setOrganisation(new Organisation(organisationRepository.existOrganisation(USER2.userName)));

            StudentBranch studentBranch3 = STUDENT_BRANCH3.toEntity();
            studentBranch3.setOrganisation(new Organisation(organisationRepository.existOrganisation(USER3.userName)));

            studentBranchRepository.saveStudentBranch(studentBranch1);
            studentBranchRepository.saveStudentBranch(studentBranch2);
            studentBranchRepository.saveStudentBranch(studentBranch3);

            studentProgramRepository.save(STUDENT_PROGRAM1.toEntity());
            studentProgramRepository.save(STUDENT_PROGRAM2.toEntity());
            studentProgramRepository.save(STUDENT_PROGRAM3.toEntity());

        }
    }


    @Component
    @Import({AlumniRepositoryImpl.class,  InitialiseProgramAndBranch.class})
    public static class InitialiseAlumni implements Initialise{


        private final AlumniRepository alumniRepository;
        private final UserRepository userRepository;
        private final StudentBranchRepository studentBranchRepository;
        private final StudentProgramRepository studentProgramRepository;

        @Autowired
        public InitialiseAlumni(AlumniRepository alumniRepository , UserRepository userRepository, InitialiseProgramAndBranch initialiseProgramAndBranch, StudentProgramRepository studentProgramRepository, StudentBranchRepository studentBranchRepository) {
            initialiseProgramAndBranch.initialise();;
            this.alumniRepository = alumniRepository;
            this.userRepository = userRepository;
            this.studentBranchRepository = studentBranchRepository;
            this.studentProgramRepository = studentProgramRepository;

        }

        @Override
        @Transactional
        public void initialise() {
            Alumni alumni1 = ALUMNI1.toEntity();
            Alumni alumni2 = ALUMNI2.toEntity();
            Alumni alumni3 = ALUMNI3.toEntity();


            alumni1.setUser(new User(userRepository.usernameExists(USER8.userName)));
            alumni2.setUser(new User(userRepository.usernameExists(USER9.userName)));
            alumni3.setUser(new User(userRepository.usernameExists(USER10.userName)));

            alumni1.setBranch(new StudentBranch(studentBranchRepository.existsStudentBranch(STUDENT_BRANCH1.name)));
            alumni2.setBranch(new StudentBranch(studentBranchRepository.existsStudentBranch(STUDENT_BRANCH2.name)));
            alumni3.setBranch(new StudentBranch(studentBranchRepository.existsStudentBranch(STUDENT_BRANCH3.name)));

            alumni1.setProgram(new StudentProgram(studentProgramRepository.existsStudentProgram(STUDENT_PROGRAM1.name)));
            alumni2.setProgram(new StudentProgram(studentProgramRepository.existsStudentProgram(STUDENT_PROGRAM2.name)));
            alumni3.setProgram(new StudentProgram(studentProgramRepository.existsStudentProgram(STUDENT_PROGRAM3.name)));

            alumniRepository.save(alumni1);
            alumniRepository.save(alumni2);
            alumniRepository.save(alumni3);
        }
    }

    @Component
    @Import({StudentRepositoryImpl.class  , InitialiseProgramAndBranch.class })
    public static class InitialiseStudent implements Initialise{

        private final StudentRepository studentRepository;
        private final UserRepository userRepository;
        private final StudentProgramRepository studentProgramRepository;
        private final StudentBranchRepository studentBranchRepository;

        @Autowired
        public InitialiseStudent(StudentRepository studentRepository, UserRepository userRepository , StudentBranchRepository studentBranchRepository, StudentProgramRepository studentProgramRepository, InitialiseProgramAndBranch initialiseProgramAndBranch) {
            this.studentRepository = studentRepository;
            this.userRepository = userRepository;
            this.studentBranchRepository = studentBranchRepository;
            this.studentProgramRepository = studentProgramRepository;
            initialiseProgramAndBranch.initialise();
        }
        @Transactional
        @Override
        public void initialise() {

            Student student1 = STUDENT1.toEntity();
            student1.setUser(new User(userRepository.usernameExists(USER5.userName)));
            student1.setBranch(new StudentBranch(studentBranchRepository.existsStudentBranch(STUDENT_BRANCH1.name)));
            student1.setProgram(new StudentProgram(studentProgramRepository.existsStudentProgram(STUDENT_PROGRAM1.name)));

            Student student2 = STUDENT2.toEntity();
            student2.setUser(new User(userRepository.usernameExists(USER6.userName)));
            student2.setBranch(new StudentBranch(studentBranchRepository.existsStudentBranch(STUDENT_BRANCH2.name)));
            student2.setProgram(new StudentProgram(studentProgramRepository.existsStudentProgram(STUDENT_PROGRAM2.name)));

            Student student3 = STUDENT3.toEntity();
            student3.setUser(new User(userRepository.usernameExists(USER7.userName)));
            student3.setBranch(new StudentBranch(studentBranchRepository.existsStudentBranch(STUDENT_BRANCH3.name)));
            student3.setProgram(new StudentProgram(studentProgramRepository.existsStudentProgram(STUDENT_PROGRAM3.name)));

            studentRepository.save(student1);
            studentRepository.save(student2);
            studentRepository.save(student3);


        }
    }

    @Component
    @Import({FacultyRepositoryImpl.class,InitialiseOrganisation.class,  UserRepositoryImpl.class, OrganisationRepositoryImpl.class})
    public static class InitialiseFaculty implements Initialise{
        private final FacultyRepository facultyRepository;
        private final UserRepository userRepository;
        private final OrganisationRepository organisationRepository;

        @Autowired
        public InitialiseFaculty(FacultyRepository facultyRepository, UserRepository userRepository , OrganisationRepository organisationRepository,InitialiseOrganisation initialiseOrganisation) {
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


    @Component
    @Import({LostnFoundRepositoryImpl.class , MediaRepositoryImpl.class , UserRepositoryImpl.class , InitialiseUser.class , InitialiseMedia.class})
    public static class InitialiseLostnFound implements Initialise{
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

    @Component
    @Import({GrievanceRepositoryImpl.class, InitialiseMedia.class, InitialiseOrganisationRole.class})
    public static class InitialiseGrievance implements Initialise{
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






}
