package in.ac.iitj.instiapp.Tests.InitialiseEntities.User;


import in.ac.iitj.instiapp.Repository.User.Organisation.OrganisationRepository;
import in.ac.iitj.instiapp.Repository.User.Organisation.OrganisationRoleRepository;
import in.ac.iitj.instiapp.Repository.User.Student.StudentBranchRepository;
import in.ac.iitj.instiapp.Repository.User.Student.StudentProgramRepository;
import in.ac.iitj.instiapp.Repository.impl.OrganisationRoleRepositoryImpl;
import in.ac.iitj.instiapp.Repository.impl.StudentBranchRepositoryImpl;
import in.ac.iitj.instiapp.Repository.impl.StudentProgramRepositoryImpl;
import in.ac.iitj.instiapp.Tests.EntityTestData.OrganisationRoleData;
import in.ac.iitj.instiapp.Tests.Utilities.InitialiseEntities;
import in.ac.iitj.instiapp.database.entities.User.Organisation.Organisation;
import in.ac.iitj.instiapp.database.entities.User.Organisation.OrganisationRole;
import in.ac.iitj.instiapp.database.entities.User.Student.StudentBranch;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import static in.ac.iitj.instiapp.Tests.EntityTestData.StudentBranchData.*;
import static in.ac.iitj.instiapp.Tests.EntityTestData.StudentProgramData.*;
import static in.ac.iitj.instiapp.Tests.EntityTestData.UserData.*;



@Component
@Import({InitialiseOrganisation.class, StudentBranchRepositoryImpl.class, StudentProgramRepositoryImpl.class})
public class InitialiseProgramAndBranch implements InitialiseEntities.Initialise {

    private final StudentBranchRepository studentBranchRepository;
    private final StudentProgramRepository studentProgramRepository;
    private final OrganisationRepository organisationRepository;

    public InitialiseProgramAndBranch(StudentBranchRepository studentBranchRepository, StudentProgramRepository studentProgramRepository, OrganisationRepository organisationRepository, InitialiseOrganisation initialiseOrganisation) {
        this.studentBranchRepository = studentBranchRepository;
        this.studentProgramRepository = studentProgramRepository;
        this.organisationRepository = organisationRepository;


        initialiseOrganisation.initialise();
    }

    @Override
    @Transactional
    public void initialise() {
        StudentBranch studentBranch1 = STUDENT_BRANCH1.toEntity();
        studentBranch1.setOrganisation(new Organisation(organisationRepository.existOrganisation(USER1.userName)));

        StudentBranch studentBranch2 = STUDENT_BRANCH2.toEntity();
        studentBranch2.setOrganisation(new Organisation(organisationRepository.existOrganisation(USER2.userName)));

        StudentBranch studentBranch3 = STUDENT_BRANCH3.toEntity();
        studentBranch3.setOrganisation(new Organisation(organisationRepository.existOrganisation(USER3.userName)));

        studentBranchRepository.saveStudentBranch(studentBranch1);
        studentBranchRepository.saveStudentBranch(studentBranch2);
        studentBranchRepository.saveStudentBranch(studentBranch3);

        studentProgramRepository.save(STUDENT_PROGRAM1.toEntity());
        studentProgramRepository.save(STUDENT_PROGRAM2.toEntity());
        studentProgramRepository.save(STUDENT_PROGRAM3.toEntity());

    }
}
