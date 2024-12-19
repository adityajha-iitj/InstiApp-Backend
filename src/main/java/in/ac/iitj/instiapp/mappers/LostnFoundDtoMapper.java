package in.ac.iitj.instiapp.mappers;

import in.ac.iitj.instiapp.database.entities.LostnFound.LostnFound;
import in.ac.iitj.instiapp.payload.LostnFound.LostnFoundDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface LostnFoundDtoMapper {
    @Mapping(source = "mediaPublicUrl", target = "media.publicUrl")
    @Mapping(source = "LandmarkName", target = "Landmark.name")
    @Mapping(source = "ownerPhoneNumber", target = "owner.phoneNumber")
    @Mapping(source = "ownerEmail", target = "owner.email")
    @Mapping(source = "ownerUserName", target = "owner.userName")
    @Mapping(source = "ownerName", target = "owner.name")
    @Mapping(source = "finderPhoneNumber", target = "finder.phoneNumber")
    @Mapping(source = "finderEmail", target = "finder.email")
    @Mapping(source = "finderUserName", target = "finder.userName")
    @Mapping(source = "finderName", target = "finder.name")
    LostnFound toEntity(LostnFoundDto lostnFoundDto);

    @InheritInverseConfiguration(name = "toEntity")
    LostnFoundDto toDto(LostnFound lostnFound);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    LostnFound partialUpdate(LostnFoundDto lostnFoundDto, @MappingTarget LostnFound lostnFound);
}