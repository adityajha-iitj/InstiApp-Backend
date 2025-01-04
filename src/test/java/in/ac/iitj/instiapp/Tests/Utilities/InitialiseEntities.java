package in.ac.iitj.instiapp.Tests.Utilities;

import in.ac.iitj.instiapp.Repository.CalendarRepository;
import in.ac.iitj.instiapp.Repository.UserRepository;
import in.ac.iitj.instiapp.Repository.UserTypeRepository;
import in.ac.iitj.instiapp.Repository.impl.CalendarRepositoryImpl;
import in.ac.iitj.instiapp.Repository.impl.UserRepositoryImpl;
import in.ac.iitj.instiapp.Repository.impl.UserTypeRepositoryImpl;
import in.ac.iitj.instiapp.database.entities.Scheduling.Calendar.Calendar;
import in.ac.iitj.instiapp.database.entities.User.User;
import in.ac.iitj.instiapp.database.entities.User.Usertype;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import static in.ac.iitj.instiapp.Tests.EntityTestData.CalendarData.*;
import static in.ac.iitj.instiapp.Tests.EntityTestData.UserData.*;
import static in.ac.iitj.instiapp.Tests.EntityTestData.UserTypeData.*;

public class InitialiseEntities {

    public  interface Initialise{
       void initialise();
    }

    @Component
    @Import({UserRepositoryImpl.class, CalendarRepositoryImpl.class, UserTypeRepositoryImpl.class})
    public static class InitialiseUser implements Initialise{

        private final CalendarRepository calendarRepository;
        private  final UserRepository userRepository;
        private final UserTypeRepository userTypeRepository;


        @Autowired
        public InitialiseUser(CalendarRepository calendarRepository, UserRepository userRepository,UserTypeRepository userTypeRepository) {
                this.userRepository = userRepository;
                this.userTypeRepository = userTypeRepository;
                this.calendarRepository = calendarRepository;
        }


        @Transactional
        public void initialise(){
            Long CALENDAR1Id = calendarRepository.save(CALENDAR1.toEntity());
            Long CALENDAR2Id = calendarRepository.save(CALENDAR2.toEntity());
            Long CALENDAR3Id = calendarRepository.save(CALENDAR3.toEntity());


             userTypeRepository.save(USER_TYPE1.toEntity());
            userTypeRepository.save(USER_TYPE2.toEntity());
            userTypeRepository.save(USER_TYPE3.toEntity());


            Long UserType1Id = userRepository.exists(USER_TYPE1.name);
            Long UserType2Id = userRepository.exists(USER_TYPE2.name);
            Long UserType3Id = userRepository.exists(USER_TYPE3.name);


            User user1 = USER1.toEntity();
            User user2 = USER2.toEntity();
            User user3 = USER3.toEntity();

            user1.setCalendar(new Calendar(CALENDAR1Id));
            user2.setCalendar(new Calendar(CALENDAR2Id));
            user3.setCalendar(new Calendar(CALENDAR3Id));

            user1.setUserType(new Usertype(UserType1Id));
            user2.setUserType(new Usertype(UserType2Id));
            user3.setUserType(new Usertype(UserType3Id));




            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.save(user3);
        }


    }






}
