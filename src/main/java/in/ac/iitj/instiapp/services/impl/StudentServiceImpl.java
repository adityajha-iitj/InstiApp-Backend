package in.ac.iitj.instiapp.services.impl;

import in.ac.iitj.instiapp.Repository.User.Student.Student.StudentRepository;
import in.ac.iitj.instiapp.Repository.User.Student.StudentBranchRepository;
import in.ac.iitj.instiapp.Repository.User.Student.StudentProgramRepository;
import in.ac.iitj.instiapp.database.entities.User.Student.Student.Student;
import in.ac.iitj.instiapp.database.entities.User.Student.StudentBranch;
import in.ac.iitj.instiapp.database.entities.User.Student.StudentProgram;
import in.ac.iitj.instiapp.mappers.User.Student.StudentBranchDtoMapper;
import in.ac.iitj.instiapp.mappers.User.Student.StudentDtoMapper;
import in.ac.iitj.instiapp.payload.User.Student.StudentBaseDto;
import in.ac.iitj.instiapp.payload.User.Student.StudentBranchDto;
import in.ac.iitj.instiapp.payload.User.Student.StudentDetailedDto;
import in.ac.iitj.instiapp.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org. springframework. data. domain. Pageable;
import java.util.List;
import java.util.Optional;
@Service
public class StudentServiceImpl implements StudentService {

    private final StudentProgramRepository studentProgramRepository;
    private final StudentBranchRepository studentBranchRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository , StudentProgramRepository studentProgramRepository, StudentBranchRepository studentBranchRepository) {
        this.studentProgramRepository = studentProgramRepository;
        this.studentBranchRepository = studentBranchRepository;
        this.studentRepository = studentRepository;

    }

    @Override
    public void saveProgram(String program, Boolean isActive) {
        studentProgramRepository.save(new StudentProgram(null , program, isActive));
    }

    @Override
    public Long existStudentProgram(String program) {
        return studentProgramRepository.existsStudentProgram(program);
    }

    @Override
    public List<String> getListOfStudentPrograms(Pageable pageable, Boolean all) {
        return studentProgramRepository.getListOfStudentPrograms(pageable, all);

    }

    @Override
    public void updateStudentProgram(String oldProgram, String newProgram, Boolean isActive) {
        studentProgramRepository.updateStudentProgram(oldProgram, newProgram, isActive);

    }

    @Override
    public void deleteStudentProgram(String program) {
        studentProgramRepository.deleteStudentProgram(program);

    }

    @Override
    public void saveStudentBranch(StudentBranchDto studentBranchDto) {
        StudentBranch branch = StudentBranchDtoMapper.INSTANCE.toEntity(studentBranchDto);
        studentBranchRepository.saveStudentBranch(branch);


    }

    @Override
    public List<StudentBranchDto> getListofStudentBranch(Pageable pageable) {
        return studentBranchRepository.getListOfStudentBranch(pageable);

    }

    @Override
    public StudentBranchDto getStudentBranch(String branch) {
        return studentBranchRepository.getStudentBranch(branch);

    }

    @Override
    public void updateStudentBranch(String branch, StudentBranchDto studentBranchDto) {
        StudentBranch studentBranch = StudentBranchDtoMapper.INSTANCE.toEntity(studentBranchDto);
        studentBranchRepository.updateStudentBranch(branch, studentBranch);

    }

    @Override
    public void deleteStudentBranch(String branch) {
        studentBranchRepository.deleteStudentBranch(branch);


    }

    @Override
    public void saveStudent(StudentDetailedDto studentDto) {
        Student student = StudentDtoMapper.INSTANCE.detailedDtoToStudent(studentDto);
        studentRepository.save(student);
    }

    @Override
    public StudentBaseDto getStudent(String username) {
        return studentRepository.getStudent(username);
    }

    @Override
    public List<StudentBaseDto> getStudentByFilter(Optional<String> programName, Optional<String> branchName, Optional<Integer> admissionYear, Pageable pageable) {
        return studentRepository.getStudentByFilter(programName, branchName, admissionYear, pageable);
    }

    @Override
    public Long existStudent(String username) {
        return studentRepository.existStudent(username);
    }

    @Override
    public StudentDetailedDto getDetailedStudent(String username) {
        return studentRepository.getDetailedStudent(username);
    }

    @Override
    public void updateStudent(String username, StudentDetailedDto studentDetailedDto) {
        Student student = StudentDtoMapper.INSTANCE.detailedDtoToStudent(studentDetailedDto);
        studentRepository.save(student);

    }

    @Override
    public Long deleteStudent(String username) {
        return studentRepository.deleteStudent(username);
    }
}
