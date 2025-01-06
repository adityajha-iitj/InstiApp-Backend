package in.ac.iitj.instiapp.Tests.Utilities;

import in.ac.iitj.instiapp.Tests.EntityTestData.*;
import in.ac.iitj.instiapp.payload.User.Alumni.AlumniBaseDto;
import in.ac.iitj.instiapp.payload.User.Organisation.OrganisationBaseDto;
import in.ac.iitj.instiapp.payload.User.UserBaseDto;
import org.assertj.core.api.Assertions;
import in.ac.iitj.instiapp.payload.User.Faculty.FacultyBaseDto;
import in.ac.iitj.instiapp.payload.User.Faculty.FacultyDetailedDto;

public class Utils {



    public static void matchUserBaseDto(UserBaseDto userBaseDto, UserData userData, UserTypeData userTypeData) {
        Assertions.assertThat(userBaseDto.getUserTypeName()).isEqualTo(userTypeData.name);
        Assertions.assertThat(userBaseDto.getUserName()).isEqualTo(userData.userName);
        Assertions.assertThat(userBaseDto.getAvatarUrl()).isEqualTo(userData.avatarUrl);
        Assertions.assertThat(userBaseDto.getEmail()).isEqualTo(userData.email);
        Assertions.assertThat(userBaseDto.getName()).isEqualTo(userData.name);
    }

    public static void matchOrganisationBaseDto(OrganisationBaseDto organisationBaseDto, OrganisationData organisationData, OrganisationTypeData organisationTypeData) {
        Assertions.assertThat(organisationBaseDto.getTypeName()).isEqualTo(organisationData.organisationType);
        Assertions.assertThat(organisationBaseDto.getDescription()).isEqualTo(organisationData.description);
        Assertions.assertThat(organisationBaseDto.getWebsite()).isEqualTo(organisationData.website);
    }


    public static void matchAlumniBaseDto(AlumniBaseDto alumniBaseDto, AlumniData alumniData, StudentBranchData studentBranchData, StudentProgramData studentProgramData,UserData userData){
        Assertions.assertThat(alumniBaseDto.getUser().getUserName()).isEqualTo(userData.userName);
        Assertions.assertThat(alumniBaseDto.getAdmissionYear()).isEqualTo(alumniData.admissionYear);
        Assertions.assertThat(alumniBaseDto.getStudentBranchDto().getName()).isEqualTo(studentBranchData.name);
        Assertions.assertThat(alumniBaseDto.getProgramName()).isEqualTo(studentProgramData.name);

    }

    public static void matchFacultyBaseDto(FacultyBaseDto facultyBaseDto, FacultyData facultyData,OrganisationData organisationData, UserData userData){
        Assertions.assertThat(facultyBaseDto.getUser().getUserName()).isEqualTo(userData.userName);
        Assertions.assertThat(facultyBaseDto.getOrganisation().getUser().getUserName()).isEqualTo(userData.userName);
        Assertions.assertThat(facultyBaseDto.getDescription().equals(facultyData.description));
        Assertions.assertThat(facultyBaseDto.getWebsiteUrl()).isEqualTo(facultyData.websiteUrl);

    }

}
