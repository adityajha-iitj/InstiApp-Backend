package in.ac.iitj.instiapp.Tests.InitialiseEntities.User;

import in.ac.iitj.instiapp.Repository.*;
import in.ac.iitj.instiapp.Repository.impl.*;
import in.ac.iitj.instiapp.Tests.Utilities.InitialiseEntities;
import in.ac.iitj.instiapp.database.entities.Scheduling.Calendar.Calendar;
import in.ac.iitj.instiapp.database.entities.User.PermissionsData;
import in.ac.iitj.instiapp.database.entities.User.User;
import in.ac.iitj.instiapp.database.entities.User.Usertype;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import static in.ac.iitj.instiapp.Tests.EntityTestData.CalendarData.*;
import static in.ac.iitj.instiapp.Tests.EntityTestData.UserData.*;
import static in.ac.iitj.instiapp.Tests.EntityTestData.UserTypeData.*;

@Component
@Import({UserRepositoryImpl.class, CalendarRepositoryImpl.class, UserTypeRepositoryImpl.class, GrievanceRepositoryImpl.class})
public class InitialiseUser implements InitialiseEntities.Initialise {

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

