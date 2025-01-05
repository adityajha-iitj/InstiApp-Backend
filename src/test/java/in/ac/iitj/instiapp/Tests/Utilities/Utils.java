package in.ac.iitj.instiapp.Tests.Utilities;

import in.ac.iitj.instiapp.Tests.EntityTestData.OrganisationData;
import in.ac.iitj.instiapp.Tests.EntityTestData.OrganisationTypeData;
import in.ac.iitj.instiapp.Tests.EntityTestData.UserData;
import in.ac.iitj.instiapp.Tests.EntityTestData.UserTypeData;
import in.ac.iitj.instiapp.payload.User.Organisation.OrganisationBaseDto;
import in.ac.iitj.instiapp.payload.User.UserBaseDto;
import org.assertj.core.api.Assertions;

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
}
