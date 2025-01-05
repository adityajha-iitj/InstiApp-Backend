//package in.ac.iitj.instiapp.mappers.User.WellBeingModerator;
//
//import in.ac.iitj.instiapp.database.entities.User.Wellbeingmoderator.WellBeingMember;
//import in.ac.iitj.instiapp.payload.User.WellBeingModerator.WellBeingMemberDtoFull;
//import in.ac.iitj.instiapp.payload.User.WellBeingModerator.WellBeingMemberDtoLimited;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import org.mapstruct.factory.Mappers;
//
//@Mapper
//public interface WellBeingMemberDtoMapper {
//    WellBeingMemberDtoMapper INSTANCE = Mappers.getMapper(WellBeingMemberDtoMapper.class);
//    @Mapping(source = "user.name", target = "userName")
//    @Mapping(source = "user.userName", target = "userUserName")
//    @Mapping(source = "user.email", target = "userEmail")
//    @Mapping(source = "user.calendar.public_id", target = "userCalendarPublic_id")
//    @Mapping(source = "user.avatar.publicId", target = "userAvatarPublicId")
//    @Mapping(source = "qualification", target = "qualification")
//    @Mapping(source = "availability", target = "availability")
//    WellBeingMemberDtoLimited entityToLimitedDto(WellBeingMember wellBeingMember);
//
//    // Mapping from WellBeingMemberDto to WellBeingMember entity
//    @Mapping(source = "userName", target = "user.name")
//    @Mapping(source = "userUserName", target = "user.userName")
//    @Mapping(source = "userEmail", target = "user.email")
//    @Mapping(source = "userCalendarPublic_id", target = "user.calendar.public_id")
//    @Mapping(source = "userAvatarPublicId", target = "user.avatar.publicId")
//    @Mapping(source = "qualification", target = "qualification")
//    @Mapping(source = "availability", target = "availability")
//    WellBeingMember limitedDtoToEntity(WellBeingMemberDtoLimited dto);
//
//    @Mapping(source = "user.name", target = "userName")
//    @Mapping(source = "user.userName", target = "userUserName")
//    @Mapping(source = "user.email", target = "userEmail")
//    @Mapping(source = "user.phoneNumber", target = "userPhoneNumber")
//    @Mapping(source = "user.calendar.public_id", target = "userCalendarPublic_id")
//    @Mapping(source = "user.avatar.publicId", target = "userAvatarPublicId")
//    @Mapping(source = "qualification", target = "qualification")
//    @Mapping(source = "availability", target = "availability")
//    WellBeingMemberDtoFull entityToFullDto(WellBeingMember wellBeingMember);
//
//    @Mapping(source = "userName", target = "user.name")
//    @Mapping(source = "userUserName", target = "user.userName")
//    @Mapping(source = "userEmail", target = "user.email")
//    @Mapping(source = "userPhoneNumber", target = "user.phoneNumber")
//    @Mapping(source = "userCalendarPublic_id", target = "user.calendar.public_id")
//    @Mapping(source = "userAvatarPublicId", target = "user.avatar.publicId")
//    @Mapping(source = "qualification", target = "qualification")
//    @Mapping(source = "availability", target = "availability")
//    WellBeingMember fullDtoToEntity(WellBeingMemberDtoFull dto);
//}
