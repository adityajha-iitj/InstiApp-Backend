package in.ac.iitj.instiapp.Tests.Utilities;

import in.ac.iitj.instiapp.Tests.EntityTestData.UserData;
import in.ac.iitj.instiapp.Tests.EntityTestData.UserTypeData;
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
}
