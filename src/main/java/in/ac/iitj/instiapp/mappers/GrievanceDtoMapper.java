package in.ac.iitj.instiapp.mappers;

import in.ac.iitj.instiapp.database.entities.Grievance;
import in.ac.iitj.instiapp.payload.GrievanceDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface GrievanceDtoMapper {
    Grievance toEntity(GrievanceDto grievanceDto);

    GrievanceDto toDto(Grievance grievance);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Grievance partialUpdate(GrievanceDto grievanceDto, @MappingTarget Grievance grievance);
}