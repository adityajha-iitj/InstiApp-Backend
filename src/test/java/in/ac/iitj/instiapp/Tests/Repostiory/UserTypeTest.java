package in.ac.iitj.instiapp.Tests.Repostiory;


import in.ac.iitj.instiapp.Repository.UserTypeRepository;
import in.ac.iitj.instiapp.Repository.impl.UserTypeRepositoryImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import java.util.Arrays;

import static in.ac.iitj.instiapp.Tests.EntityTestData.UserTypeData.*;

@DataJpaTest
@Import({UserTypeRepositoryImpl.class})
public class UserTypeTest {


    @Autowired
    UserTypeRepository userTypeRepository;


    @BeforeAll
    public static void setUp(@Autowired UserTypeRepository userTypeRepository) {
        userTypeRepository.save(USER_TYPE1.toEntity());
        userTypeRepository.save(USER_TYPE2.toEntity());

    }


    @Test
    @Order(1)
    public  void saveTest(){
        Assertions.assertThatThrownBy(() -> userTypeRepository.save(USER_TYPE1.toEntity()))
                .isInstanceOf(DataIntegrityViolationException.class);

    }

    @Test
    @Order(2)
    @Rollback(value = true)
    public void updateTest(){
        Assertions.assertThatThrownBy(() -> userTypeRepository.update(USER_TYPE4.name,USER_TYPE3.toEntity()))
                .isInstanceOf(EmptyResultDataAccessException.class);

        Assertions.assertThatThrownBy(() -> userTypeRepository.update(USER_TYPE1.name,USER_TYPE2.toEntity()))
                .isInstanceOf(DataIntegrityViolationException.class);

        userTypeRepository.update(USER_TYPE1.name,USER_TYPE3.toEntity());

        Assertions.assertThat(userTypeRepository.exists(USER_TYPE3.name)).isTrue();
        Assertions.assertThat(userTypeRepository.exists(USER_TYPE1.name)).isFalse();
    }

    @Test
    @Order(3)
    @Rollback(value = true)
    public void deleteTest(){
        Assertions.assertThat(userTypeRepository.exists(USER_TYPE1.name)).isTrue();
        userTypeRepository.delete(USER_TYPE1.name);
        Assertions.assertThat(userTypeRepository.exists(USER_TYPE1.name)).isFalse();
    }

    @Test
    @Order(4)
    public void testExists(){
        Assertions.assertThat(userTypeRepository.exists(USER_TYPE1.name)).isTrue();
        Assertions.assertThat(userTypeRepository.exists(USER_TYPE4.name)).isFalse();
    }

    @Test
    @Order(5)
    public void testGetAllUserTypes(){
        Pageable pageable = PageRequest.of(0,10);
        Assertions.assertThat(userTypeRepository.getAllUserTypes(pageable))
                .containsExactlyElementsOf(Arrays.asList(USER_TYPE1.name, USER_TYPE2.name));

    }

}
