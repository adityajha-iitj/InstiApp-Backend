package in.ac.iitj.instiapp.Tests.Repository;


import in.ac.iitj.instiapp.Repository.User.Organisation.OrganisationRepository;
import in.ac.iitj.instiapp.Repository.User.Student.StudentBranchRepository;
import in.ac.iitj.instiapp.Repository.User.Student.StudentProgramRepository;
import in.ac.iitj.instiapp.Repository.impl.OrganisationRepositoryImpl;
import in.ac.iitj.instiapp.Repository.impl.StudentBranchRepositoryImpl;
import in.ac.iitj.instiapp.Tests.EntityTestData.StudentBranchData;
import in.ac.iitj.instiapp.Tests.EntityTestData.StudentProgramData;
import in.ac.iitj.instiapp.Tests.EntityTestData.UserData;
import in.ac.iitj.instiapp.Tests.Utilities.InitialiseEntities;
import in.ac.iitj.instiapp.database.entities.User.Organisation.Organisation;
import in.ac.iitj.instiapp.database.entities.User.Student.StudentBranch;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import in.ac.iitj.instiapp.payload.User.Student.StudentBranchDto;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static in.ac.iitj.instiapp.Tests.EntityTestData.OrganisationData.*;

@DataJpaTest
@Import({StudentBranchRepositoryImpl.class , InitialiseEntities.InitialiseProgramAndBranch.class , OrganisationRepositoryImpl.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class StudentBranchProgramTest {
    StudentProgramRepository studentProgramRepository;
    StudentBranchRepository studentBranchRepository;
    OrganisationRepository organisationRepository;

    @Autowired
    public StudentBranchProgramTest(StudentBranchRepository studentBranchRepository , StudentProgramRepository studentProgramRepository , OrganisationRepository organisationRepository) {
        this.studentBranchRepository = studentBranchRepository;
        this.studentProgramRepository = studentProgramRepository;
        this.organisationRepository = organisationRepository;
    }


    @BeforeAll
    public static void setup( @Autowired InitialiseEntities.InitialiseProgramAndBranch initialiseProgramAndBranch) {
        initialiseProgramAndBranch.initialise();
    }

    @Order(1)
    @Test
    public void testGetListOfStudentBranch(){
        Pageable pageable = PageRequest.of(0, 10);
        List<StudentBranchDto> list = studentBranchRepository.getListOfStudentBranch(pageable);
        Set<StudentBranchDto> expected = Set.of(
                new StudentBranchDto(
                        StudentBranchData.STUDENT_BRANCH1.name,
                        UserData.USER1.userName,
                        StudentBranchData.STUDENT_BRANCH1.openingYear,
                        StudentBranchData.STUDENT_BRANCH1.closingYear // Closing year is null
                ),
                new StudentBranchDto(
                        StudentBranchData.STUDENT_BRANCH2.name,
                        UserData.USER2.userName,
                        StudentBranchData.STUDENT_BRANCH2.openingYear,
                        StudentBranchData.STUDENT_BRANCH2.closingYear // Closing year is null
                ),
                new StudentBranchDto(
                StudentBranchData.STUDENT_BRANCH3.name,
                UserData.USER3.userName,
                StudentBranchData.STUDENT_BRANCH3.openingYear,
                StudentBranchData.STUDENT_BRANCH3.closingYear
        )
        );

        // Convert actual results into a set
        Set<StudentBranchDto> actual = new HashSet<>(list);

        // Verify
        Assertions.assertEquals(expected, actual);
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
    @Rollback(value = true)
    public void testUpdateStudentBranch(){
        Long id =studentBranchRepository.existsStudentBranch(StudentBranchData.STUDENT_BRANCH1.name);
        StudentBranch studentBranch = StudentBranchData.STUDENT_BRANCH4.toEntity();
        studentBranchRepository.updateStudentBranch(StudentBranchData.STUDENT_BRANCH1.name, studentBranch);

        StudentBranchDto branch = studentBranchRepository.getStudentBranch(StudentBranchData.STUDENT_BRANCH4.name);
        Assertions.assertEquals(null , branch.getOrganisation().getDescription());
        Assertions.assertEquals(StudentBranchData.STUDENT_BRANCH4.openingYear , studentBranch.getOpeningYear());
        Assertions.assertEquals(StudentBranchData.STUDENT_BRANCH4.closingYear , studentBranch.getClosingYear());

        StudentBranch name = new StudentBranch(null , null ,StudentBranchData.STUDENT_BRANCH3.closingYear , new Organisation(organisationRepository.existOrganisation(UserData.USER1.userName)));
        studentBranchRepository.updateStudentBranch(StudentBranchData.STUDENT_BRANCH2.name, name);
        StudentBranchDto check_name = studentBranchRepository.getStudentBranch(StudentBranchData.STUDENT_BRANCH2.name);
        Assertions.assertEquals(StudentBranchData.STUDENT_BRANCH2.name , check_name.getName());
        Assertions.assertEquals(StudentBranchData.STUDENT_BRANCH2.openingYear , check_name.getOpeningYear());
        Assertions.assertEquals(StudentBranchData.STUDENT_BRANCH3.closingYear , check_name.getClosingYear());
        Assertions.assertEquals(organisationRepository.getOrganisation(UserData.USER1.userName).getUser().getUserName(), check_name.getOrganisation().getUser().getUserName());




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
        Assertions.assertEquals(3 , programs.size());
        List<String> programs_all = studentProgramRepository.getListOfStudentPrograms(pageable , false);
        Assertions.assertEquals(StudentProgramData.STUDENT_PROGRAM1.name , programs_all.get(0));
        Assertions.assertEquals(2 , programs_all.size());
        Assertions.assertEquals(StudentProgramData.STUDENT_PROGRAM3.name , programs_all.get(1));
    }

    @Order(7)
    @Test
    @Rollback(value = true)
    public void updateStudentProgram(){
        studentProgramRepository.updateStudentProgram(StudentProgramData.STUDENT_PROGRAM1.name , StudentProgramData.STUDENT_PROGRAM4.name, StudentProgramData.STUDENT_PROGRAM4.isActive);
        Long id = studentProgramRepository.existsStudentProgram(StudentProgramData.STUDENT_PROGRAM4.name);
        Assertions.assertNotEquals(-1 , id);
    }


}
