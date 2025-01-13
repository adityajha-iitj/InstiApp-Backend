package in.ac.iitj.instiapp.mappers.User;

import in.ac.iitj.instiapp.database.entities.User.User;
import in.ac.iitj.instiapp.payload.User.UserBaseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel="spring")
public interface UserBaseDtoMapper {

    @Mapping(source="name",target="name")
    @Mapping(source="userName",target="userName")
    @Mapping(source="email",target="email")
    @Mapping(source="userTypeName",target="userType.name")
    @Mapping(source="avatarUrl",target="avatarUrl")
    User toUser(UserBaseDto userBaseDto);

    @Mapping(source="name",target="name")
    @Mapping(source="userName",target="userName")
    @Mapping(source="email",target="email")
    @Mapping(source="userType.name",target="userTypeName")
    @Mapping(source="avatarUrl",target="avatarUrl")
    UserBaseDto toUserDto(User user);


}
