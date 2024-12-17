package in.ac.iitj.instiapp.Tests.Repository;


import in.ac.iitj.instiapp.Repository.CalendarRepository;
import in.ac.iitj.instiapp.Repository.UserAvatarRepository;
import in.ac.iitj.instiapp.Repository.UserRepository;
import in.ac.iitj.instiapp.Repository.UserTypeRepository;
import in.ac.iitj.instiapp.Repository.impl.CalendarRepositoryImpl;
import in.ac.iitj.instiapp.Repository.impl.UserAvatarRepositoryImpl;
import in.ac.iitj.instiapp.Repository.impl.UserRepositoryImpl;
import in.ac.iitj.instiapp.Repository.impl.UserTypeRepositoryImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static in.ac.iitj.instiapp.Tests.EntityTestData.CalendarData.*;
import static in.ac.iitj.instiapp.Tests.EntityTestData.UserAvatarData.*;
import static in.ac.iitj.instiapp.Tests.EntityTestData.UserData.*;
import static in.ac.iitj.instiapp.Tests.EntityTestData.UserTypeData.*;

@DataJpaTest
@Import({UserRepositoryImpl.class, UserAvatarRepositoryImpl.class, CalendarRepositoryImpl.class, UserTypeRepositoryImpl.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserTest {


    private final UserRepository userRepository;
    private final UserAvatarRepository userAvatarRepository;
    private final CalendarRepository calendarRepository;
    private final UserTypeRepository userTypeRepository;

    @Autowired
    public UserTest(UserRepository userRepository, UserAvatarRepository userAvatarRepository, CalendarRepository calendarRepository,UserTypeRepository userTypeRepository) {
        this.userRepository = userRepository;
        this.userAvatarRepository = userAvatarRepository;
        this.calendarRepository = calendarRepository;
        this.userTypeRepository = userTypeRepository;
    }

    @BeforeAll
    public static void setUp(@Autowired UserAvatarRepository userAvatarRepository, @Autowired CalendarRepository calendarRepository, @Autowired UserRepository userRepository,@Autowired UserTypeRepository userTypeRepository) {
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

    @Test
    @Order(1)
    public void testExistByEmail(){
        Assertions.assertThat(userRepository.existsByEmail(USER1.email)).isTrue();
        Assertions.assertThat(userRepository.existsByEmail(USER4.email)).isFalse();
    }

    @Test
    @Order(2)
    public void testExistByUsername(){
        Assertions.assertThat(userRepository.existsByUsername(USER1.email)).isTrue();
        Assertions.assertThat(userRepository.existsByUsername(USER4.email)).isFalse();
    }

    @Test
    @Order(3)
    public void testExistByPhoneNumber(){
        Assertions.assertThat(userRepository.existsByPhoneNumber(USER1.phoneNumber)).isTrue();
        Assertions.assertThat(userRepository.existsByPhoneNumber(USER4.phoneNumber)).isFalse();
    }

    @Test
    @Order(4)
    public void testUpdatePhoneNumber(){
        String newPhoneNumber = "111111111";
        String username = USER1.userName;

        userRepository.updatePhoneNumber(newPhoneNumber, username);
        Assertions.assertThat(userRepository.existsByPhoneNumber(newPhoneNumber)).isTrue();
        Assertions.assertThat(userRepository.existsByPhoneNumber(USER1.phoneNumber)).isFalse();
    }




}
