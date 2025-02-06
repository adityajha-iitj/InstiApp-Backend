package in.ac.iitj.instiapp.Tests.InitialiseEntities.User;

import in.ac.iitj.instiapp.Repository.User.Student.Student.StudentRepository;
import in.ac.iitj.instiapp.Repository.User.Student.StudentBranchRepository;
import in.ac.iitj.instiapp.Repository.User.Student.StudentProgramRepository;
import in.ac.iitj.instiapp.Repository.UserRepository;
import in.ac.iitj.instiapp.Repository.impl.StudentRepositoryImpl;
import in.ac.iitj.instiapp.Tests.Utilities.InitialiseEntities;
import in.ac.iitj.instiapp.database.entities.User.Student.Student.Student;
import in.ac.iitj.instiapp.database.entities.User.Student.StudentBranch;
import in.ac.iitj.instiapp.database.entities.User.Student.StudentProgram;
import in.ac.iitj.instiapp.database.entities.User.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import static in.ac.iitj.instiapp.Tests.EntityTestData.StudentBranchData.*;
import static in.ac.iitj.instiapp.Tests.EntityTestData.StudentData.*;
import static in.ac.iitj.instiapp.Tests.EntityTestData.StudentProgramData.*;
import static in.ac.iitj.instiapp.Tests.EntityTestData.UserData.*;

@Component
@Import({StudentRepositoryImpl.class  , InitialiseProgramAndBranch.class })
public  class InitialiseStudent implements InitialiseEntities.Initialise {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final StudentProgramRepository studentProgramRepository;
    private final StudentBranchRepository studentBranchRepository;

    @Autowired
    public InitialiseStudent(StudentRepository studentRepository, UserRepository userRepository , StudentBranchRepository studentBranchRepository, StudentProgramRepository studentProgramRepository, InitialiseProgramAndBranch initialiseProgramAndBranch) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.studentBranchRepository = studentBranchRepository;
        this.studentProgramRepository = studentProgramRepository;
        initialiseProgramAndBranch.initialise();
    }
    @Transactional
    @Override
    public void initialise() {

        Student student1 = STUDENT1.toEntity();
        student1.setUser(new User(userRepository.usernameExists(USER5.userName)));
        student1.setBranch(new StudentBranch(studentBranchRepository.existsStudentBranch(STUDENT_BRANCH1.name)));
        student1.setProgram(new StudentProgram(studentProgramRepository.existsStudentProgram(STUDENT_PROGRAM1.name)));

        Student student2 = STUDENT2.toEntity();
        student2.setUser(new User(userRepository.usernameExists(USER6.userName)));
        student2.setBranch(new StudentBranch(studentBranchRepository.existsStudentBranch(STUDENT_BRANCH2.name)));
        student2.setProgram(new StudentProgram(studentProgramRepository.existsStudentProgram(STUDENT_PROGRAM2.name)));

        Student student3 = STUDENT3.toEntity();
        student3.setUser(new User(userRepository.usernameExists(USER7.userName)));
        student3.setBranch(new StudentBranch(studentBranchRepository.existsStudentBranch(STUDENT_BRANCH3.name)));
        student3.setProgram(new StudentProgram(studentProgramRepository.existsStudentProgram(STUDENT_PROGRAM3.name)));

        studentRepository.save(student1);
        studentRepository.save(student2);
        studentRepository.save(student3);


    }
}

