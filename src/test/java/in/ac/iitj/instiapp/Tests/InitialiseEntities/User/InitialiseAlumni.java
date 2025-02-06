package in.ac.iitj.instiapp.Tests.InitialiseEntities.User;


import in.ac.iitj.instiapp.Repository.User.Student.Alumni.AlumniRepository;
import in.ac.iitj.instiapp.Repository.User.Student.StudentBranchRepository;
import in.ac.iitj.instiapp.Repository.User.Student.StudentProgramRepository;
import in.ac.iitj.instiapp.Repository.UserRepository;
import in.ac.iitj.instiapp.Repository.impl.AlumniRepositoryImpl;
import in.ac.iitj.instiapp.Tests.Utilities.InitialiseEntities;
import in.ac.iitj.instiapp.database.entities.User.Student.Alumni.Alumni;
import in.ac.iitj.instiapp.database.entities.User.Student.StudentBranch;
import in.ac.iitj.instiapp.database.entities.User.Student.StudentProgram;
import in.ac.iitj.instiapp.database.entities.User.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import static in.ac.iitj.instiapp.Tests.EntityTestData.AlumniData.*;
import static in.ac.iitj.instiapp.Tests.EntityTestData.StudentProgramData.*;
import  static  in.ac.iitj.instiapp.Tests.EntityTestData.StudentBranchData.*;
import static in.ac.iitj.instiapp.Tests.EntityTestData.UserData.*;

@Component
@Import({AlumniRepositoryImpl.class,  InitialiseProgramAndBranch.class})
public  class InitialiseAlumni implements InitialiseEntities.Initialise {


    private final AlumniRepository alumniRepository;
    private final UserRepository userRepository;
    private final StudentBranchRepository studentBranchRepository;
    private final StudentProgramRepository studentProgramRepository;

    @Autowired
    public InitialiseAlumni(AlumniRepository alumniRepository , UserRepository userRepository, InitialiseProgramAndBranch initialiseProgramAndBranch, StudentProgramRepository studentProgramRepository, StudentBranchRepository studentBranchRepository) {
        initialiseProgramAndBranch.initialise();;
        this.alumniRepository = alumniRepository;
        this.userRepository = userRepository;
        this.studentBranchRepository = studentBranchRepository;
        this.studentProgramRepository = studentProgramRepository;

    }

    @Override
    @Transactional
    public void initialise() {
        Alumni alumni1 = ALUMNI1.toEntity();
        Alumni alumni2 = ALUMNI2.toEntity();
        Alumni alumni3 = ALUMNI3.toEntity();


        alumni1.setUser(new User(userRepository.usernameExists(USER8.userName)));
        alumni2.setUser(new User(userRepository.usernameExists(USER9.userName)));
        alumni3.setUser(new User(userRepository.usernameExists(USER10.userName)));

        alumni1.setBranch(new StudentBranch(studentBranchRepository.existsStudentBranch(STUDENT_BRANCH1.name)));
        alumni2.setBranch(new StudentBranch(studentBranchRepository.existsStudentBranch(STUDENT_BRANCH2.name)));
        alumni3.setBranch(new StudentBranch(studentBranchRepository.existsStudentBranch(STUDENT_BRANCH3.name)));

        alumni1.setProgram(new StudentProgram(studentProgramRepository.existsStudentProgram(STUDENT_PROGRAM1.name)));
        alumni2.setProgram(new StudentProgram(studentProgramRepository.existsStudentProgram(STUDENT_PROGRAM2.name)));
        alumni3.setProgram(new StudentProgram(studentProgramRepository.existsStudentProgram(STUDENT_PROGRAM3.name)));

        alumniRepository.save(alumni1);
        alumniRepository.save(alumni2);
        alumniRepository.save(alumni3);
    }
}

