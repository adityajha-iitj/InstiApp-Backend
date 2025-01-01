package in.ac.iitj.instiapp.Tests.Repository;

import in.ac.iitj.instiapp.Repository.User.Student.Student.StudentRepository;
import in.ac.iitj.instiapp.Repository.UserRepository;
import in.ac.iitj.instiapp.Repository.impl.StudentRepositoryImpl;
import in.ac.iitj.instiapp.Tests.EntityTestData.UserData;
import in.ac.iitj.instiapp.Tests.EntityTestData.StudentData;
import in.ac.iitj.instiapp.Tests.Utilities.InitialiseEntities;
import in.ac.iitj.instiapp.database.entities.User.Student.StudentBranch;
import in.ac.iitj.instiapp.database.entities.User.Student.StudentProgram;
import in.ac.iitj.instiapp.database.entities.User.User;
import in.ac.iitj.instiapp.payload.User.Student.StudentBaseDto;
import in.ac.iitj.instiapp.payload.User.Student.StudentDetailedDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.dao.EmptyResultDataAccessException;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@Import({StudentRepositoryImpl.class, InitialiseEntities.InitialiseUser.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class StudentTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeAll
    public static void setup(@Autowired InitialiseEntities.InitialiseUser initialise,
                             @Autowired StudentRepository studentRepository,
                             @Autowired UserRepository userRepository,
                             @Autowired EntityManager entityManager) {
        initialise.initialise();

        // Create and persist StudentProgram entities
        StudentProgram program1 = new StudentProgram();
        program1.setName("BTech");
        StudentProgram program2 = new StudentProgram();
        program2.setName("MTech");
        entityManager.persist(program1);
        entityManager.persist(program2);

        // Create and persist StudentBranch entities
        StudentBranch branch1 = new StudentBranch();
        branch1.setName("Computer Science");
        StudentBranch branch2 = new StudentBranch();
        branch2.setName("Electrical Engineering");
        entityManager.persist(branch1);
        entityManager.persist(branch2);

        // Set up STUDENT_DATA1 with USER1
        studentRepository.save(
                StudentData.STUDENT_DATA1
                        .setUser(entityManager.find(User.class, userRepository.getUserId(UserData.USER1.userName)))
                        .setProgram(program1)
                        .setBranch(branch1)
                        .toEntity()
        );

        // Set up STUDENT_DATA2 with USER2
        studentRepository.save(
                StudentData.STUDENT_DATA2
                        .setUser(entityManager.find(User.class, userRepository.getUserId(UserData.USER2.userName)))
                        .setProgram(program2)
                        .setBranch(branch2)
                        .toEntity()
        );

        entityManager.flush();
    }

    @Test
    @Order(1)
    void testGetStudent() {
        StudentBaseDto student = studentRepository.getStudent(UserData.USER1.userName);
        Assertions.assertNotNull(student);
        Assertions.assertEquals("BTech", student.getProgramName());
        Assertions.assertEquals(UserData.USER1.name, student.getUser().getName());
        Assertions.assertEquals(UserData.USER1.email, student.getUser().getEmail());
        Assertions.assertEquals(StudentData.STUDENT_DATA1.getAdmissionYear(), student.getAdmissionYear());
    }

    @Test
    @Order(2)
    void testGetStudentByFilter() {
        List<StudentBaseDto> students = studentRepository.getStudentByFilter(
                Optional.of("BTech"),
                Optional.of("Computer Science"),
                Optional.of(StudentData.STUDENT_DATA1.getAdmissionYear()),
                PageRequest.of(0, 10)
        );

        Assertions.assertFalse(students.isEmpty());
        Assertions.assertEquals("BTech", students.get(0).getProgramName());
        Assertions.assertEquals(StudentData.STUDENT_DATA1.getAdmissionYear(), students.get(0).getAdmissionYear());
    }

    @Test
    @Order(3)
    void testExistStudent() {
        Assertions.assertTrue(studentRepository.existStudent(UserData.USER1.userName));
        Assertions.assertFalse(studentRepository.existStudent("nonexistent"));
    }

    @Test
    @Order(4)
    void testGetDetailedStudent() {
        StudentDetailedDto student = studentRepository.getDetailedStudent(UserData.USER1.userName);
        Assertions.assertNotNull(student);
        Assertions.assertEquals("BTech", student.getProgramName());
        Assertions.assertEquals(UserData.USER1.phoneNumber, student.getUser().getPhoneNumber());
        Assertions.assertEquals(StudentData.STUDENT_DATA1.getAdmissionYear(), student.getAdmissionYear());
    }

    @Test
    @Order(5)
    void testDeleteStudent() {
        Long userId = studentRepository.deleteStudent(UserData.USER2.userName);
        Assertions.assertNotNull(userId);
        Assertions.assertThrows(EmptyResultDataAccessException.class, () ->
                studentRepository.getStudent(UserData.USER2.userName)
        );
    }

    @Test
    @Order(6)
    void testGetStudentNotFound() {
        Assertions.assertThrows(EmptyResultDataAccessException.class, () ->
                studentRepository.getStudent("nonexistent")
        );
    }

    @Test
    @Order(7)
    void testGetStudentByFilterNoResults() {
        List<StudentBaseDto> students = studentRepository.getStudentByFilter(
                Optional.of("NonexistentProgram"),
                Optional.empty(),
                Optional.empty(),
                PageRequest.of(0, 10)
        );

        Assertions.assertTrue(students.isEmpty());
    }
}