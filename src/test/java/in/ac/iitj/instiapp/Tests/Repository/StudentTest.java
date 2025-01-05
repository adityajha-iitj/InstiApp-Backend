package in.ac.iitj.instiapp.Tests.Repository;

import in.ac.iitj.instiapp.Repository.User.Student.Student.StudentRepository;
import in.ac.iitj.instiapp.Repository.impl.StudentRepositoryImpl;
import in.ac.iitj.instiapp.Tests.EntityTestData.StudentBranchData;
import in.ac.iitj.instiapp.Tests.EntityTestData.StudentBranchData.*;
import in.ac.iitj.instiapp.Tests.EntityTestData.StudentData;
import in.ac.iitj.instiapp.Tests.EntityTestData.StudentData.*;
import in.ac.iitj.instiapp.Tests.EntityTestData.StudentProgramData;
import in.ac.iitj.instiapp.Tests.EntityTestData.StudentProgramData.*;
import in.ac.iitj.instiapp.Tests.EntityTestData.UserData;
import in.ac.iitj.instiapp.Tests.EntityTestData.UserData.*;
import in.ac.iitj.instiapp.Tests.Utilities.InitialiseEntities;
import in.ac.iitj.instiapp.payload.User.Student.StudentBaseDto;
import org.junit.jupiter.api.*;
import org. junit. jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


@DataJpaTest
@Import({StudentRepositoryImpl.class, InitialiseEntities.InitialiseStudent.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class StudentTest {

    @Autowired
    private StudentRepository studentRepository;


    @BeforeAll
    public static void setup(@Autowired InitialiseEntities.InitialiseStudent initialiseStudent) {

        initialiseStudent.initialise();

    }

    @Order(1)
    @Test
    public void testGetStudent(){
        StudentBaseDto student1 = studentRepository.getStudent(UserData.USER1.userName);
        Assertions.assertEquals(StudentBranchData.STUDENT_BRANCH1.name , student1.getStudentBranch().getName());
        Assertions.assertEquals(StudentProgramData.STUDENT_PROGRAM1.name , student1.getProgramName());
        Assertions.assertEquals(StudentData.STUDENT1.admissionYear , student1.getAdmissionYear());

    }

    @Order(2)
    @Test
    public void testGetStudentByFilter(){
        Pageable pageable = PageRequest.of(0, 10);
        List<StudentBaseDto> program_filter = studentRepository.getStudentByFilter(Optional.of( StudentProgramData.STUDENT_PROGRAM1.name ), Optional.empty() , Optional.empty() , pageable);
        Assertions.assertEquals(1 , program_filter.size());
        Assertions.assertEquals(UserData.USER5.name , program_filter.get(0).getUser().getName());
        Assertions.assertEquals(StudentBranchData.STUDENT_BRANCH1.name , program_filter.get(0).getStudentBranch().getName());
        Assertions.assertEquals(StudentData.STUDENT1.admissionYear , program_filter.get(0).getAdmissionYear());

        List<StudentBaseDto> branch_filter = studentRepository.getStudentByFilter(Optional.empty() , Optional.of(StudentBranchData.STUDENT_BRANCH2.name) , Optional.empty() , pageable);
        Assertions.assertEquals(1 , branch_filter.size());
        Assertions.assertEquals(UserData.USER6.name , program_filter.get(0).getUser().getName());
        Assertions.assertEquals(StudentProgramData.STUDENT_PROGRAM2.name , program_filter.get(0).getProgramName());
        Assertions.assertEquals(StudentData.STUDENT2.admissionYear , program_filter.get(0).getAdmissionYear());

        List<StudentBaseDto> ad_filter = studentRepository.getStudentByFilter(Optional.empty() , Optional.empty() , Optional.of(StudentData.STUDENT3.name) , pageable);
        Assertions.assertEquals(1 , branch_filter.size());
        Assertions.assertEquals(UserData.USER7.name , program_filter.get(0).getUser().getName());
        Assertions.assertEquals(StudentProgramData.STUDENT_PROGRAM3.name , program_filter.get(0).getProgramName());
        Assertions.assertEquals(StudentBranchData.STUDENT_BRANCH3.name , program_filter.get(0).getStudentBranch().getName());



    }



}