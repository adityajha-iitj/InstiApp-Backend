package in.ac.iitj.instiapp.mappers.User.Student;

import in.ac.iitj.instiapp.database.entities.User.Student.Student.Student;
import in.ac.iitj.instiapp.database.entities.User.Student.StudentProgram;
import in.ac.iitj.instiapp.payload.User.Student.StudentBaseDto;
import in.ac.iitj.instiapp.payload.User.Student.StudentDetailedDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring" )

public interface StudentDtoMapper {

    StudentDtoMapper INSTANCE = Mappers.getMapper(StudentDtoMapper.class);

    @Mapping(source = "user" , target = "user" )
    @Mapping(source = "program.name" , target = "programName")
    @Mapping(source = "branch" , target = "studentBranch")
    @Mapping(source = "admissionYear" , target = "admissionYear")
    StudentBaseDto ToStudentBaseDto(Student student);

    @Mapping(source = "user" , target = "user" )
    @Mapping(source = "program.name" , target = "programName")
    @Mapping(source = "branch" , target = "studentBranch")
    @Mapping(source = "admissionYear" , target = "admissionYear")
    StudentDetailedDto ToStudentDetailedDto(Student student);

    @Mapping(source = "user" , target = "user")
    @Mapping(source = "programName" , target = "program")
    @Mapping(source = "studentBranch" , target = "branch")
    @Mapping(source = "admissionYear" , target = "admissionYear")
    Student baseDtoToStudent(StudentBaseDto studentBaseDto);

    @Mapping(source = "user" , target = "user")
    @Mapping(source = "programName" , target = "program")
    @Mapping(source = "studentBranch" , target = "branch")
    @Mapping(source = "admissionYear" , target = "admissionYear")
    Student detailedDtoToStudent(StudentDetailedDto studentDetailedDto);

    default StudentProgram map(String programName) {
        if (programName == null) {
            return null;
        }
        return new StudentProgram(null, programName, true);  // Assuming constructor takes id, name, isActive
    }
}
