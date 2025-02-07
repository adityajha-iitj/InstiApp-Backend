package in.ac.iitj.instiapp.mappers;

import in.ac.iitj.instiapp.database.entities.LostnFound.LostnFound;
import in.ac.iitj.instiapp.mappers.Scheduling.MessMenu.MessDtoMapper;
import in.ac.iitj.instiapp.payload.LostnFound.LostnFoundDto;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface LostnFoundDtoMapper {

    LostnFoundDtoMapper INSTANCE = Mappers.getMapper(LostnFoundDtoMapper.class);
    @Mapping(source = "media", target = "media")
    @Mapping(source = "publicId", target = "publicId")
    @Mapping(source = "owner", target = "owner")
    @Mapping(source = "finder", target = "finder")
    @Mapping(source = "landmarkName", target = "landmark.name")
    @Mapping(source = "extraInfo", target = "extraInfo")
    @Mapping(source = "status", target = "status")
    LostnFound toEntity(LostnFoundDto lostnFoundDto);

    @InheritInverseConfiguration(name = "toEntity")
    LostnFoundDto toDto(LostnFound lostnFound);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    LostnFound partialUpdate(LostnFoundDto lostnFoundDto, @MappingTarget LostnFound lostnFound);
}