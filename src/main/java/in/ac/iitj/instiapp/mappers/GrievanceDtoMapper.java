package in.ac.iitj.instiapp.mappers;

import in.ac.iitj.instiapp.payload.GrievanceDto;
import in.ac.iitj.instiapp.database.entities.Grievance;


import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface GrievanceDtoMapper {

    /*@Mapping(source="publicId",target="publicId")
    @Mapping(source="Title",target="Title")
    @Mapping(source="Description",target="Description")
    @Mapping(source="userFrom",target="userFrom")
    @Mapping(source="organisationRole",target="organisationRole")
    @Mapping(source="resolved",target="resolved")
    @Mapping(source="media",target="media")
    GrievanceDto toGrievanceDto(Grievance grievance);*/

    @Mapping(source="publicId",target="publicId")
    @Mapping(source="title",target="title")
    @Mapping(source="description",target="description")
    @Mapping(source="userFrom",target="userFrom")
    @Mapping(source="organisationRole",target="organisationRole")
    @Mapping(source="resolved",target="resolved")
    @Mapping(source="media",target="media")
    Grievance toGrievance(GrievanceDto grievanceDto);
}