package in.ac.iitj.instiapp.Tests.Repository;


import in.ac.iitj.instiapp.Repository.User.Student.Alumni.AlumniRepository;
import in.ac.iitj.instiapp.Repository.User.Student.StudentBranchRepository;
import in.ac.iitj.instiapp.Repository.User.Student.StudentProgramRepository;
import in.ac.iitj.instiapp.Tests.EntityTestData.AlumniData;
import in.ac.iitj.instiapp.Tests.EntityTestData.StudentBranchData;
import in.ac.iitj.instiapp.Tests.EntityTestData.StudentProgramData;
import in.ac.iitj.instiapp.Tests.EntityTestData.UserData;
import in.ac.iitj.instiapp.Tests.Utilities.InitialiseEntities;
import in.ac.iitj.instiapp.Tests.Utilities.Utils;
import in.ac.iitj.instiapp.database.entities.User.Student.StudentBranch;
import in.ac.iitj.instiapp.database.entities.User.Student.StudentProgram;
import in.ac.iitj.instiapp.payload.User.Alumni.AlumniBaseDto;
import in.ac.iitj.instiapp.payload.User.Alumni.AlumniDetailedDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import in.ac.iitj.instiapp.database.entities.User.Student.Alumni.Alumni;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@Import({InitialiseEntities.InitialiseAlumni.class})
public class AlumniTest {

    private final AlumniRepository alumniRepository;
    private final StudentBranchRepository studentBranchRepository;
    private final StudentProgramRepository studentProgramRepository;

    @Autowired
    public AlumniTest(AlumniRepository alumniRepository, StudentBranchRepository studentBranchRepository,StudentProgramRepository studentProgramRepository){
        this.alumniRepository = alumniRepository;
        this.studentBranchRepository = studentBranchRepository;
        this.studentProgramRepository = studentProgramRepository;
    }


    @BeforeAll
    public static void setUp(@Autowired InitialiseEntities.InitialiseAlumni initialiseAlumni){
        initialiseAlumni.initialise();
    }


    @Test
    @Order(1)
    public void testGetAlumni(){
        // as user7 is not user
        Assertions.assertThatThrownBy(() -> alumniRepository.getAlumni(UserData.USER7.userName))
                .isInstanceOf(EmptyResultDataAccessException.class);



        AlumniBaseDto alumniBaseDto = alumniRepository.getAlumni(UserData.USER8.userName);
        Utils.matchAlumniBaseDto(alumniBaseDto, AlumniData.ALUMNI1, StudentBranchData.STUDENT_BRANCH1, StudentProgramData.STUDENT_PROGRAM1,UserData.USER8);
    }


    @Test
    @Order(2)
    public void testGetDetailedAlumni(){
        Assertions.assertThatThrownBy(() -> alumniRepository.getAlumni(UserData.USER7.userName))
                .isInstanceOf(EmptyResultDataAccessException.class);

        AlumniDetailedDto alumniDetailedDto = alumniRepository.getDetailedAlumni(UserData.USER8.userName);
        Assertions.assertThat(alumniDetailedDto.getUser().getUserName()).isEqualTo(UserData.USER8.userName);
        Assertions.assertThat(alumniDetailedDto.getAdmissionYear()).isEqualTo(AlumniData.ALUMNI1.admissionYear);
        Assertions.assertThat(alumniDetailedDto.getPassOutYear()).isEqualTo(AlumniData.ALUMNI1.passOutYear);
        Assertions.assertThat(alumniDetailedDto.getProgramName()).isEqualTo(StudentProgramData.STUDENT_PROGRAM1.name);
        Assertions.assertThat(alumniDetailedDto.getStudentBranchDto().getName()).isEqualTo(StudentBranchData.STUDENT_BRANCH1.name);
    }



    @Test
    @Order(3)
    public void testGetAlumniByFilter(){
        Pageable pageable = PageRequest.of(0,10);


        List<AlumniBaseDto> alumniBaseDtoList = alumniRepository.getAlumniByFilter(Optional.of(StudentProgramData.STUDENT_PROGRAM1.name), Optional.empty(),Optional.empty(),Optional.empty(),pageable);
        Assertions.assertThat(alumniBaseDtoList.size()).isEqualTo(1);
        Utils.matchAlumniBaseDto(alumniBaseDtoList.get(0),AlumniData.ALUMNI1,StudentBranchData.STUDENT_BRANCH1,StudentProgramData.STUDENT_PROGRAM1,UserData.USER8);


        alumniBaseDtoList = alumniRepository.getAlumniByFilter(Optional.empty(),Optional.of(StudentBranchData.STUDENT_BRANCH1.name),Optional.empty(),Optional.empty(),pageable);
        Assertions.assertThat(alumniBaseDtoList.size()).isEqualTo(1);
        Utils.matchAlumniBaseDto(alumniBaseDtoList.get(0),AlumniData.ALUMNI1,StudentBranchData.STUDENT_BRANCH1,StudentProgramData.STUDENT_PROGRAM1,UserData.USER8);

        alumniBaseDtoList = alumniRepository.getAlumniByFilter(Optional.empty(),Optional.empty(),Optional.of(AlumniData.ALUMNI1.admissionYear),Optional.empty(),pageable);
        Assertions.assertThat(alumniBaseDtoList.size()).isEqualTo(1);
        Utils.matchAlumniBaseDto(alumniBaseDtoList.get(0),AlumniData.ALUMNI1,StudentBranchData.STUDENT_BRANCH1,StudentProgramData.STUDENT_PROGRAM1,UserData.USER8);

        alumniBaseDtoList = alumniRepository.getAlumniByFilter(Optional.empty(),Optional.empty(),Optional.empty(),Optional.of(AlumniData.ALUMNI1.passOutYear),pageable);
        Assertions.assertThat(alumniBaseDtoList.size()).isEqualTo(2);
        Utils.matchAlumniBaseDto(alumniBaseDtoList.get(0),AlumniData.ALUMNI1,StudentBranchData.STUDENT_BRANCH1,StudentProgramData.STUDENT_PROGRAM1,UserData.USER8);
    }


    @Test
    @Order(4)
    public void testAlumniExists(){
        Assertions.assertThat(alumniRepository.alumniExists(UserData.USER8.userName)).isNotNull().isNotEqualTo(-1L);
        Assertions.assertThat(alumniRepository.alumniExists(UserData.USER9.userName)).isNotNull().isNotEqualTo(-1L);
        Assertions.assertThat(alumniRepository.alumniExists(UserData.USER10.userName)).isNotNull().isNotEqualTo(-1L);
        Assertions.assertThat(alumniRepository.alumniExists(UserData.USER7.userName)).isEqualTo(-1L);
    }


    @Test
    @Order(5)
    @Rollback(value = true)
    public void testUpdateAlumni(){
        Alumni alumni = AlumniData.ALUMNI2.toEntity();
        alumni.setProgram(new StudentProgram(studentProgramRepository.existsStudentProgram(StudentProgramData.STUDENT_PROGRAM2.name)));
        alumni.setId(alumniRepository.alumniExists(UserData.USER8.userName));
        alumni.setBranch(new StudentBranch(studentBranchRepository.existsStudentBranch(StudentBranchData.STUDENT_BRANCH2.name)));

        alumniRepository.updateAlumni(alumni);

        Utils.matchAlumniBaseDto(
                alumniRepository.getAlumni(UserData.USER8.userName),
                AlumniData.ALUMNI2,
                StudentBranchData.STUDENT_BRANCH2,
                StudentProgramData.STUDENT_PROGRAM2,
                UserData.USER8
        );
    }


    @Test
    @Order(6)
    public void testDeleteAlumni(){
        //TODO
    }


}
