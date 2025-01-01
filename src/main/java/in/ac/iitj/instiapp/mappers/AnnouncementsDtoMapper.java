package in.ac.iitj.instiapp.mappers;

import in.ac.iitj.instiapp.database.entities.Announcements;
import in.ac.iitj.instiapp.database.entities.User.Groups;
import in.ac.iitj.instiapp.payload.AnnouncementsDto;
import org.mapstruct.*;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface AnnouncementsDtoMapper {
    @Mapping(source = "userAvatarUrl", target = "user.avatarUrl")
    @Mapping(source = "userEmail", target = "user.email")
    @Mapping(source = "userUserName", target = "user.userName")
    @Mapping(source = "userName", target = "user.name")
    Announcements toEntity(AnnouncementsDto announcementsDto);

    @InheritInverseConfiguration(name = "toEntity")
    @Mapping(target = "groupsListNames", expression = "java(groupsListToGroupsListNames(announcements.getGroupsLists()))")
    AnnouncementsDto toDto(Announcements announcements);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Announcements partialUpdate(AnnouncementsDto announcementsDto, @MappingTarget Announcements announcements);

    default Set<String> groupsListToGroupsListNames(Set<Groups> groupsList) {
        return groupsList.stream().map(Groups::getName).collect(Collectors.toSet());
    }
}