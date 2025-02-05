package in.ac.iitj.instiapp.Tests.Service;

import in.ac.iitj.instiapp.Repository.User.Student.Student.StudentRepository;
import in.ac.iitj.instiapp.Repository.User.Student.StudentBranchRepository;
import in.ac.iitj.instiapp.Repository.User.Student.StudentProgramRepository;
import in.ac.iitj.instiapp.Tests.EntityTestData.StudentProgramData;
import in.ac.iitj.instiapp.database.entities.User.Student.Student.Student;
import in.ac.iitj.instiapp.database.entities.User.Student.StudentBranch;
import in.ac.iitj.instiapp.database.entities.User.Student.StudentProgram;
import in.ac.iitj.instiapp.payload.User.Organisation.OrganisationBaseDto;
import in.ac.iitj.instiapp.payload.User.Student.StudentBaseDto;
import in.ac.iitj.instiapp.payload.User.Student.StudentBranchDto;
import in.ac.iitj.instiapp.payload.User.Student.StudentDetailedDto;
import in.ac.iitj.instiapp.payload.User.UserBaseDto;
import in.ac.iitj.instiapp.payload.User.UserDetailedDto;
import in.ac.iitj.instiapp.services.StudentService;
import in.ac.iitj.instiapp.services.impl.StudentServiceImpl;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org. springframework. data. domain. Pageable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.ExpectedCount.times;

public class StudentServiceTest {

    @Mock
    private StudentProgramRepository studentProgramRepository;

    @Mock
    private StudentBranchRepository studentBranchRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentServiceImpl studentServiceImpl;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Order(1)
    public void testSaveProgram() {
        Pageable pageable = PageRequest.of(0, 10);
        List<StudentProgram> mockStudentPrograms = new ArrayList<>();
        mockStudentPrograms.add( new StudentProgram("Program1" , false));
        mockStudentPrograms.add(new StudentProgram("Program2", true));
        List<String> mockresult = new ArrayList<>();
        mockresult.add("Program2");

        when(studentServiceImpl.getListOfStudentPrograms(pageable , true)).thenReturn(mockresult);
        List<String> program = studentServiceImpl.getListOfStudentPrograms(pageable , true);
        assertNotNull(program, "The result should not be null");
        assertEquals("Program2" , program.get(0));
    }

    @Test
    @Order(2)
    public void testExistStudentProgram() {
        String programName = "Btech";
        Long expectedCount = 1L;
        when(studentProgramRepository.existsStudentProgram(programName)).thenReturn(expectedCount);
        Long result = studentServiceImpl.existStudentProgram(programName);
        assertEquals(expectedCount , result);
        verify(studentProgramRepository, Mockito.times(1)).existsStudentProgram(programName);
    }

    @Test
    @Order(3)
    public void testSaveStudentBranch() {
        OrganisationBaseDto orgDto = new OrganisationBaseDto("CSE-IITJ");
        StudentBranchDto branchDto = StudentBranchDto.builder()
                .name("Computer Science")
                .organisation(orgDto)
                .openingYear(2020)
                .closingYear(null)
                .build();

        studentServiceImpl.saveStudentBranch(branchDto);

        verify(studentBranchRepository, Mockito.times(1)).saveStudentBranch(any(StudentBranch.class));
    }

    @Test
    @Order(4)
    public void testGetListofStudentBranch() {
        Pageable pageable = PageRequest.of(0, 10);
        OrganisationBaseDto orgDto = new OrganisationBaseDto("CSE-IITJ");

        List<StudentBranchDto> mockBranches = Arrays.asList(
                StudentBranchDto.builder()
                        .name("Computer Science")
                        .organisation(orgDto)
                        .openingYear(2020)
                        .closingYear(null)
                        .build(),
                StudentBranchDto.builder()
                        .name("Electrical Engineering")
                        .organisation(new OrganisationBaseDto("EE-IITJ"))
                        .openingYear(2020)
                        .closingYear(null)
                        .build()
        );

        when(studentBranchRepository.getListOfStudentBranch(pageable)).thenReturn(mockBranches);

        List<StudentBranchDto> result = studentServiceImpl.getListofStudentBranch(pageable);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Computer Science", result.get(0).getName());
        verify(studentBranchRepository, Mockito.times(1)).getListOfStudentBranch(pageable);
    }

    @Test
    @Order(5)
    public void testGetStudentByFilter() {
        Optional<String> programName = Optional.of("BTech");
        Optional<String> branchName = Optional.of("CS");
        Optional<Integer> admissionYear = Optional.of(2023);
        Pageable pageable = PageRequest.of(0, 10);

        List<StudentBaseDto> mockStudents = Arrays.asList(
                new StudentBaseDto(
                        new UserBaseDto("student1", "Student One", "student1@iitj.ac.in", "avatar1.jpg", "Student"),
                        "BTech",
                        new StudentBranchDto("CS", "CSE-IITJ", 2020, null),
                        2023
                ),
                new StudentBaseDto(
                        new UserBaseDto("student2", "Student Two", "student2@iitj.ac.in", "avatar2.jpg", "Student"),
                        "BTech",
                        new StudentBranchDto("CS", "CSE-IITJ", 2020, null),
                        2023
                )
        );

        when(studentRepository.getStudentByFilter(programName, branchName, admissionYear, pageable))
                .thenReturn(mockStudents);

        List<StudentBaseDto> result = studentServiceImpl.getStudentByFilter(
                programName, branchName, admissionYear, pageable);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("BTech", result.get(0).getProgramName());
        assertEquals("CS", result.get(0).getStudentBranch().getName());
        verify(studentRepository, Mockito.times(1))
                .getStudentByFilter(programName, branchName, admissionYear, pageable);
    }

    @Test
    @Order(6)
    public void testGetDetailedStudent() {
        String username = "student1";
        StudentDetailedDto mockDetailedDto = new StudentDetailedDto(
                new UserDetailedDto("Student One", username, "student1@iitj.ac.in", "avatar1.jpg", "Student"),
                "BTech",
                new StudentBranchDto("CS", "CSE-IITJ", 2020, null),
                2023
        );

        when(studentRepository.getDetailedStudent(username)).thenReturn(mockDetailedDto);

        StudentDetailedDto result = studentServiceImpl.getDetailedStudent(username);

        assertNotNull(result);
        assertEquals(username, result.getUser().getUserName());
        assertEquals("BTech", result.getProgramName());
        assertEquals("CS", result.getStudentBranch().getName());
        verify(studentRepository, Mockito.times(1)).getDetailedStudent(username);
    }

    @Test
    @Order(7)
    public void testUpdateStudent() {
        String username = "student1";
        StudentDetailedDto studentDto = new StudentDetailedDto(
                new UserDetailedDto("Student One Updated", username, "student1@iitj.ac.in", "avatar1.jpg", "Student"),
                "BTech",
                new StudentBranchDto("CS", "CSE-IITJ", 2020, null),
                2023
        );

        studentServiceImpl.updateStudent(username, studentDto);

        verify(studentRepository, Mockito.times(1)).save(any(Student.class));
    }

    @Test
    @Order(8)
    public void testDeleteStudent() {
        String username = "student1";
        Long expectedResult = 1L;

        when(studentRepository.deleteStudent(username)).thenReturn(expectedResult);

        Long result = studentServiceImpl.deleteStudent(username);

        assertEquals(expectedResult, result);
        verify(studentRepository, Mockito.times(1)).deleteStudent(username);
    }




}
