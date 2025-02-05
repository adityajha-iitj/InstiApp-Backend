package in.ac.iitj.instiapp.mappers.User;

import in.ac.iitj.instiapp.database.entities.User.User;
import in.ac.iitj.instiapp.mappers.Scheduling.Calendar.CalendarDtoMapper;
import in.ac.iitj.instiapp.mappers.User.Organisation.OrganisationRoleDtoMapper;
import in.ac.iitj.instiapp.payload.User.Organisation.OrganisationRoleDto;
import in.ac.iitj.instiapp.payload.User.UserDetailedDto;
import in.ac.iitj.instiapp.mappers.Scheduling.Calendar.CalendarEventsDtoMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses={OrganisationRoleDtoMapper.class})
public interface UserDetailedDtoMapper {

    @Mapping(source="name",target="name")
    @Mapping(source="userName",target="userName")
    @Mapping(source="email",target="email")
    @Mapping(source="userType.name",target="userTypeName")
    @Mapping(source="avatarUrl",target="avatarUrl")
    @Mapping(source="phoneNumber",target="phoneNumber")
    @Mapping(source="calendar.publicId",target="calendarPublicId")
    UserDetailedDto toUserDetailedDto(User user);

    @Mapping(source="name",target="name")
    @Mapping(source="userName",target="userName")
    @Mapping(source="email",target="email")
    @Mapping(source="userTypeName",target="userType.name")
    @Mapping(source="avatarUrl",target="avatarUrl")
    @Mapping(source="phoneNumber",target="phoneNumber")
    @Mapping(source="calendarPublicId",target="calendar.publicId")
    User toUser(UserDetailedDto userDetailedDto);

}
