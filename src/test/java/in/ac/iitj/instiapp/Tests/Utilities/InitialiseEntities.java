package in.ac.iitj.instiapp.Tests.Utilities;

import in.ac.iitj.instiapp.Repository.CalendarRepository;
import in.ac.iitj.instiapp.Repository.UserRepository;
import in.ac.iitj.instiapp.Repository.UserTypeRepository;
import in.ac.iitj.instiapp.Repository.impl.CalendarRepositoryImpl;
import in.ac.iitj.instiapp.Repository.impl.UserAvatarRepositoryImpl;
import in.ac.iitj.instiapp.Repository.impl.UserRepositoryImpl;
import in.ac.iitj.instiapp.Repository.impl.UserTypeRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import static in.ac.iitj.instiapp.Tests.EntityTestData.CalendarData.*;
import static in.ac.iitj.instiapp.Tests.EntityTestData.UserAvatarData.*;
import static in.ac.iitj.instiapp.Tests.EntityTestData.UserData.*;
import static in.ac.iitj.instiapp.Tests.EntityTestData.UserTypeData.*;
import static in.ac.iitj.instiapp.Tests.EntityTestData.UserTypeData.USER_TYPE3;

public class InitialiseEntities {

    public  interface Initialise{
       void initialise();
    }

    @Component
    @Import({UserRepositoryImpl.class, UserAvatarRepositoryImpl.class, CalendarRepositoryImpl.class, UserTypeRepositoryImpl.class})
    public static class InitialiseUser implements Initialise{

        private final CalendarRepository calendarRepository;
        private final  UserAvatarRepository userAvatarRepository;
        private  final UserRepository userRepository;
        private final UserTypeRepository userTypeRepository;


        @Autowired
        public InitialiseUser(UserAvatarRepository userAvatarRepository, CalendarRepository calendarRepository, UserRepository userRepository,UserTypeRepository userTypeRepository) {
                this.userRepository = userRepository;
                this.userTypeRepository = userTypeRepository;
                this.userAvatarRepository = userAvatarRepository;
                this.calendarRepository = calendarRepository;
        }


        public void initialise(){
            Long CALENDAR1Id = calendarRepository.save(CALENDAR1.toEntity());
            Long CALENDAR2Id = calendarRepository.save(CALENDAR2.toEntity());
            Long CALENDAR3Id = calendarRepository.save(CALENDAR3.toEntity());

            Long AVATAR1Id = userAvatarRepository.save(USER_AVATAR1.toEntity());
            Long AVATAR2Id = userAvatarRepository.save(USER_AVATAR2.toEntity());
            Long AVATAR3Id = userAvatarRepository.save(USER_AVATAR3.toEntity());

            userTypeRepository.save(USER_TYPE1.toEntity());
            userTypeRepository.save(USER_TYPE2.toEntity());
            userTypeRepository.save(USER_TYPE3.toEntity());

            userRepository.save(USER1.toEntity(),CALENDAR1Id,AVATAR1Id,USER_TYPE1.name);
            userRepository.save(USER2.toEntity(),CALENDAR2Id,AVATAR2Id,USER_TYPE2.name);
            userRepository.save(USER3.toEntity(),CALENDAR3Id,AVATAR3Id,USER_TYPE3.name);
        }


    }






}
