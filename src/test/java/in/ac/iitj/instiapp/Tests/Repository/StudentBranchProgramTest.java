package in.ac.iitj.instiapp.Tests.Repository;


import in.ac.iitj.instiapp.Repository.User.Student.StudentBranchRepository;
import in.ac.iitj.instiapp.Repository.User.Student.StudentProgramRepository;
import in.ac.iitj.instiapp.Repository.impl.StudentBranchRepositoryImpl;
import in.ac.iitj.instiapp.Tests.EntityTestData.StudentBranchData;
import in.ac.iitj.instiapp.Tests.EntityTestData.StudentProgramData;
import in.ac.iitj.instiapp.Tests.Utilities.InitialiseEntities;
import in.ac.iitj.instiapp.database.entities.User.Student.StudentBranch;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import in.ac.iitj.instiapp.payload.User.Student.StudentBranchDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static in.ac.iitj.instiapp.Tests.EntityTestData.OrganisationData.*;

@DataJpaTest
@Import({StudentBranchRepositoryImpl.class , StudentProgramRepository.class , InitialiseEntities.InitialiseProgramAndBranch.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class StudentBranchProgramTest {
    @Autowired
    private StudentProgramRepository studentProgramRepository;
    private StudentBranchRepository studentBranchRepository;


    @BeforeAll
    public static void setup(@Autowired StudentBranchRepository studentBranchRepository , @Autowired StudentProgramRepository studentProgramRepository , @Autowired InitialiseEntities.InitialiseProgramAndBranch initialiseProgramAndBranch) {

        initialiseProgramAndBranch.initialise();

    }

    @Order(1)
    @Test
    public void testGetListOfStudentBranch(){
        Pageable pageable = PageRequest.of(0, 10);
        List<StudentBranchDto> list = studentBranchRepository.getListOfStudentBranch(pageable);
        Assertions.assertEquals(2 , list.size());
        Assertions.assertEquals(StudentBranchData.STUDENT_BRANCH1.name , list.get(0).getName());
        Assertions.assertEquals(StudentBranchData.STUDENT_BRANCH1.openingYear , list.get(0).getOpeningYear());
        Assertions.assertEquals(ORGANISATION1.description , list.get(0).getOrganisation().getDescription());
        Assertions.assertNull(StudentBranchData.STUDENT_BRANCH1.closingYear);

        Assertions.assertEquals(StudentBranchData.STUDENT_BRANCH2.name, list.get(1).getName());
        Assertions.assertEquals(StudentBranchData.STUDENT_BRANCH2.openingYear , list.get(1).getOpeningYear());
        Assertions.assertEquals(ORGANISATION2.description, list.get(1).getOrganisation().getDescription());

    }

    @Order(2)
    @Test
    public void testGetStudentBranch(){
        StudentBranchDto branch = studentBranchRepository.getStudentBranch(StudentBranchData.STUDENT_BRANCH1.name);
        Assertions.assertEquals(StudentBranchData.STUDENT_BRANCH1.openingYear, branch.getOpeningYear());
        Assertions.assertNull(StudentBranchData.STUDENT_BRANCH1.closingYear);
    }

    @Order(3)
    @Test
    public void testExistStudentBranch(){
        long id1 = studentBranchRepository.existsStudentBranch(StudentBranchData.STUDENT_BRANCH1.name);
        Assertions.assertNotEquals(-1 , id1);
        long id2 = studentBranchRepository.existsStudentBranch(StudentBranchData.STUDENT_BRANCH4.name);
        Assertions.assertEquals(-1 , id2);

    }

    @Order(4)
    @Test
    public void testUpdateStudentBranch(){
        Long id =studentBranchRepository.existsStudentBranch(StudentBranchData.STUDENT_BRANCH1.name);
        StudentBranch studentBranch = StudentBranchData.STUDENT_BRANCH4.toEntity();
        studentBranchRepository.updateStudentBranch(StudentBranchData.STUDENT_BRANCH1.name, studentBranch);

        StudentBranchDto branch = studentBranchRepository.getStudentBranch(StudentBranchData.STUDENT_BRANCH4.name);
        Assertions.assertEquals(ORGANISATION2.description , branch.getOrganisation().getDescription());
        Assertions.assertEquals(StudentBranchData.STUDENT_BRANCH4.openingYear , studentBranch.getOpeningYear());
        Assertions.assertEquals(StudentBranchData.STUDENT_BRANCH4.closingYear , studentBranch.getClosingYear());

    }

    @Order(5)
    @Test
    public void testExistStudentProgram(){
        Long id1 = studentProgramRepository.existsStudentProgram(StudentProgramData.STUDENT_PROGRAM1.name);
        Assertions.assertNotEquals(-1 , id1);
        Long id2 = studentProgramRepository.existsStudentProgram(StudentProgramData.STUDENT_PROGRAM4.name);
        Assertions.assertEquals(-1 , id2);

    }

    @Order(6)
    @Test
    public void testGetListOfStudentProgram(){
        Pageable pageable = PageRequest.of(0, 10);
        List<String> programs = studentProgramRepository.getListOfStudentPrograms(pageable , true);
        Assertions.assertEquals(StudentProgramData.STUDENT_PROGRAM1.name , programs.get(0));
        Assertions.assertEquals(2 , programs.size());
        List<String> programs_all = studentProgramRepository.getListOfStudentPrograms(pageable , false);
        Assertions.assertEquals(StudentProgramData.STUDENT_PROGRAM1.name , programs_all.get(0));
        Assertions.assertEquals(4 , programs_all.size());
        Assertions.assertEquals(StudentProgramData.STUDENT_PROGRAM2.name , programs_all.get(1));
    }

    @Order(7)
    @Test
    public void updateStudentProgram(){
        studentProgramRepository.updateStudentProgram(StudentProgramData.STUDENT_PROGRAM1.name , StudentProgramData.STUDENT_PROGRAM4.name, StudentProgramData.STUDENT_PROGRAM4.isActive);
        Long id = studentProgramRepository.existsStudentProgram(StudentProgramData.STUDENT_PROGRAM4.name);
        Assertions.assertNotEquals(-1 , id);
    }


}
