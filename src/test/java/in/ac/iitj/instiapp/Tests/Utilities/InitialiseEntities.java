package in.ac.iitj.instiapp.Tests.Utilities;

import in.ac.iitj.instiapp.Repository.CalendarRepository;
import in.ac.iitj.instiapp.Repository.MediaRepository;
import in.ac.iitj.instiapp.Repository.User.Organisation.OrganisationRepository;
import in.ac.iitj.instiapp.Repository.User.Student.Alumni.AlumniRepository;
import in.ac.iitj.instiapp.Repository.User.Student.Student.StudentRepository;
import in.ac.iitj.instiapp.Repository.User.Student.StudentBranchRepository;
import in.ac.iitj.instiapp.Repository.User.Student.StudentProgramRepository;
import in.ac.iitj.instiapp.Repository.UserRepository;
import in.ac.iitj.instiapp.Repository.UserTypeRepository;
import in.ac.iitj.instiapp.Repository.impl.*;
import in.ac.iitj.instiapp.database.entities.Media.Media;
import in.ac.iitj.instiapp.database.entities.Scheduling.Calendar.Calendar;
import in.ac.iitj.instiapp.database.entities.User.Organisation.Organisation;
import in.ac.iitj.instiapp.database.entities.User.Organisation.OrganisationType;
import in.ac.iitj.instiapp.database.entities.User.Student.StudentBranch;
import in.ac.iitj.instiapp.database.entities.User.User;
import in.ac.iitj.instiapp.database.entities.User.Usertype;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import static in.ac.iitj.instiapp.Tests.EntityTestData.CalendarData.*;
import static in.ac.iitj.instiapp.Tests.EntityTestData.MediaData.*;
import static in.ac.iitj.instiapp.Tests.EntityTestData.OrganisationData.*;
import static in.ac.iitj.instiapp.Tests.EntityTestData.OrganisationTypeData.*;

import static in.ac.iitj.instiapp.Tests.EntityTestData.StudentBranchData.*;
import static in.ac.iitj.instiapp.Tests.EntityTestData.UserData.*;
import static in.ac.iitj.instiapp.Tests.EntityTestData.UserTypeData.*;
import static in.ac.iitj.instiapp.Tests.EntityTestData.StudentProgramData.*;

public class InitialiseEntities {

    public  interface Initialise{
       void initialise();
    }

    public static class InitialiseMedia implements Initialise{



    }



    @Component
    @Import({UserRepositoryImpl.class, CalendarRepositoryImpl.class, UserTypeRepositoryImpl.class})
    public static class InitialiseUser implements Initialise {

        private final CalendarRepository calendarRepository;
        private final UserRepository userRepository;
        private final UserTypeRepository userTypeRepository;


        @Autowired
        public InitialiseUser(CalendarRepository calendarRepository, UserRepository userRepository, UserTypeRepository userTypeRepository) {
            this.userRepository = userRepository;
            this.userTypeRepository = userTypeRepository;
            this.calendarRepository = calendarRepository;
        }


        @Transactional
        public void initialise() {
            Long CALENDAR1Id = calendarRepository.save(CALENDAR1.toEntity());
            Long CALENDAR2Id = calendarRepository.save(CALENDAR2.toEntity());
            Long CALENDAR3Id = calendarRepository.save(CALENDAR3.toEntity());
            Long CALENDAR5Id = calendarRepository.save(CALENDAR5.toEntity());
            Long CALENDAR6Id = calendarRepository.save(CALENDAR6.toEntity());
            Long CALENDAR7Id = calendarRepository.save(CALENDAR7.toEntity());
            Long CALENDAR8Id = calendarRepository.save(CALENDAR8.toEntity());
            Long CALENDAR9Id = calendarRepository.save(CALENDAR9.toEntity());
            Long CALENDAR10Id = calendarRepository.save(CALENDAR10.toEntity());

            userTypeRepository.save(USER_TYPE1.toEntity());
            userTypeRepository.save(USER_TYPE2.toEntity());
            userTypeRepository.save(USER_TYPE3.toEntity());
            userTypeRepository.save(USER_TYPE5.toEntity());
            userTypeRepository.save(USER_TYPE6.toEntity());
            userTypeRepository.save(USER_TYPE7.toEntity());


            Long UserType1Id = userRepository.exists(USER_TYPE1.name);
            Long UserType2Id = userRepository.exists(USER_TYPE2.name);
            Long UserType3Id = userRepository.exists(USER_TYPE3.name);
            Long UserType5Id = userRepository.exists(USER_TYPE5.name);
            Long UserType6Id = userRepository.exists(USER_TYPE6.name);
            Long UserType7Id = userRepository.exists(USER_TYPE7.name);



            User user1 = USER1.toEntity();
            User user2 = USER2.toEntity();
            User user3 = USER3.toEntity();
            User user5 = USER5.toEntity();
            User user6 = USER6.toEntity();
            User user7 = USER7.toEntity();
            User user8 = USER8.toEntity();
            User user9 = USER9.toEntity();
            User user10 = USER10.toEntity();

            user1.setCalendar(new Calendar(CALENDAR1Id));
            user2.setCalendar(new Calendar(CALENDAR2Id));
            user3.setCalendar(new Calendar(CALENDAR3Id));
            user5.setCalendar(new Calendar(CALENDAR5Id));
            user6.setCalendar(new Calendar(CALENDAR6Id));
            user7.setCalendar(new Calendar(CALENDAR7Id));
            user8.setCalendar(new Calendar(CALENDAR8Id));
            user9.setCalendar(new Calendar(CALENDAR9Id));
            user10.setCalendar(new Calendar(CALENDAR10Id));

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


            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.save(user3);
            userRepository.save(user5);
            userRepository.save(user6);
            userRepository.save(user7);
        }

    }


    @Component
    @Import({OrganisationRepositoryImpl.class, InitialiseEntities.InitialiseUser.class})
    public static class InitialiseOrganisation implements Initialise{

        private final OrganisationRepository organisationRepository;
        private final UserRepository userRepository;
        private final MediaRepository mediaRepository;



        @Autowired
        public InitialiseOrganisation(OrganisationRepository organisationRepository, UserRepository userRepository, MediaRepository mediaRepository, InitialiseUser initialiseUser) {
            this.organisationRepository = organisationRepository;
            this.userRepository = userRepository;
            this.mediaRepository = mediaRepository;

            initialiseUser.initialise();
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

            Long organisationId1 = organisationRepository.existOrganisation(USER1.userName);

            organisation3.setParentOrganisation(new Organisation(organisationId1));

            Media media1 = MEDIA_DATA1.toEntity();
            Media media2 = MEDIA_DATA2.toEntity();
            Media media3 = MEDIA_DATA3.toEntity();

            Long mediaId1 = mediaRepository.getIdByPublicId(MEDIA_DATA1.publicId);
            Long mediaId2 = mediaRepository.getIdByPublicId(MEDIA_DATA2.publicId);
            Long mediaId3 = mediaRepository.getIdByPublicId(MEDIA_DATA3.publicId);

            organisation1.setMedia(new Media(mediaId1));
            organisation2.setMedia(new M)



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
            studentBranch3.setOrganisation(new Organisation(organisationRepository.existOrganisation(USER1.userName)));

            studentBranchRepository.saveStudentBranch(studentBranch1);
            studentBranchRepository.saveStudentBranch(studentBranch2);
            studentBranchRepository.saveStudentBranch(studentBranch3);

            studentProgramRepository.save(STUDENT_PROGRAM1.toEntity());
            studentProgramRepository.save(STUDENT_PROGRAM2.toEntity());
            studentProgramRepository.save(STUDENT_PROGRAM3.toEntity());

        }
    }


    @Component
    @Import({AlumniRepositoryImpl.class, UserRepositoryImpl.class, InitialiseProgramAndBranch.class})
    public static class InitialiseAlumni implements Initialise{


        private final AlumniRepository alumniRepository;
        private final UserRepository userRepository;

        @Autowired
        public InitialiseAlumni(AlumniRepository alumniRepository , UserRepository userRepository, InitialiseProgramAndBranch initialiseProgramAndBranch) {
            initialiseProgramAndBranch.initialise();;
            this.alumniRepository = alumniRepository;
            this.userRepository = userRepository;

        }

        @Override
        public void initialise() {



        }
    }

    @Component
    @Import({StudentRepositoryImpl.class , UserRepositoryImpl.class , InitialiseProgramAndBranch.class})
    public static class InitialiseStudent implements Initialise{

        private final StudentRepository studentRepository;
        private final UserRepository userRepository;

        @Autowired
        public InitialiseStudent(StudentRepository studentRepository, UserRepository userRepository , InitialiseProgramAndBranch initialiseProgramAndBranch) {
            this.studentRepository = studentRepository;
            this.userRepository = userRepository;
            initialiseProgramAndBranch.initialise();
        }
        @Transactional
        @Override
        public void initialise() {
            User user1 = USER1
        }
    }






}
