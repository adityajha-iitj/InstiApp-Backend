package in.ac.iitj.instiapp.Tests.Utilities;

import in.ac.iitj.instiapp.Repository.*;
import in.ac.iitj.instiapp.Repository.User.Organisation.OrganisationRepository;
import in.ac.iitj.instiapp.Repository.User.Student.Alumni.AlumniRepository;
import in.ac.iitj.instiapp.Repository.FacultyRepository;
import in.ac.iitj.instiapp.Repository.UserRepository;
import in.ac.iitj.instiapp.Repository.User.Student.Student.StudentRepository;
import in.ac.iitj.instiapp.Repository.User.Student.StudentBranchRepository;
import in.ac.iitj.instiapp.Repository.User.Student.StudentProgramRepository;
import in.ac.iitj.instiapp.Repository.GrievanceRepository;
import in.ac.iitj.instiapp.Repository.impl.*;
import in.ac.iitj.instiapp.Tests.EntityTestData.*;
import in.ac.iitj.instiapp.Tests.InitialiseEntities.InitialiseMedia;
import in.ac.iitj.instiapp.Tests.InitialiseEntities.User.InitialiseOrganisation;
import in.ac.iitj.instiapp.Tests.InitialiseEntities.User.InitialiseOrganisationRole;
import in.ac.iitj.instiapp.Tests.InitialiseEntities.User.InitialiseProgramAndBranch;
import in.ac.iitj.instiapp.Tests.InitialiseEntities.User.InitialiseUser;
import in.ac.iitj.instiapp.database.entities.LostnFound.Locations;
import in.ac.iitj.instiapp.database.entities.LostnFound.LostnFound;
import in.ac.iitj.instiapp.database.entities.Grievance;
import in.ac.iitj.instiapp.database.entities.Media.Media;
import in.ac.iitj.instiapp.database.entities.Scheduling.Calendar.Calendar;
import in.ac.iitj.instiapp.database.entities.User.Organisation.Organisation;
import in.ac.iitj.instiapp.database.entities.User.Organisation.OrganisationRole;
import in.ac.iitj.instiapp.database.entities.User.Organisation.OrganisationType;
import in.ac.iitj.instiapp.database.entities.User.PermissionsData;
import in.ac.iitj.instiapp.database.entities.User.Student.Alumni.Alumni;
import in.ac.iitj.instiapp.database.entities.User.Student.Student.Student;
import in.ac.iitj.instiapp.database.entities.User.Student.StudentBranch;
import in.ac.iitj.instiapp.database.entities.User.Student.StudentProgram;
import in.ac.iitj.instiapp.database.entities.User.User;
import in.ac.iitj.instiapp.database.entities.User.Usertype;
import in.ac.iitj.instiapp.database.entities.User.Faculty.Faculty;
import in.ac.iitj.instiapp.Repository.User.Organisation.OrganisationRoleRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;


import static in.ac.iitj.instiapp.Tests.EntityTestData.MediaData.*;


import static in.ac.iitj.instiapp.Tests.EntityTestData.UserData.*;


public class InitialiseEntities {

    public  interface Initialise{
        void initialise();
    }











}
