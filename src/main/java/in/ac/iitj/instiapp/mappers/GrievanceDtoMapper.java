package in.ac.iitj.instiapp.mappers;

import in.ac.iitj.instiapp.mappers.User.Media.MediaBaseDtoMapper;
import in.ac.iitj.instiapp.mappers.User.Organisation.OrganisationRoleDtoMapper;
import in.ac.iitj.instiapp.mappers.User.UserBaseDtoMapper;
import in.ac.iitj.instiapp.payload.GrievanceDto;
import in.ac.iitj.instiapp.database.entities.Grievance;


import in.ac.iitj.instiapp.payload.User.Organisation.OrganisationRoleDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring",uses = {UserBaseDtoMapper.class, OrganisationRoleDtoMapper.class, MediaBaseDtoMapper.class})
public interface GrievanceDtoMapper {

    @Mapping(source="publicId",target="publicId")
    @Mapping(source="title",target="title")
    @Mapping(source="description",target="description")
    @Mapping(source="userFrom",target="userFrom")
    @Mapping(source="resolved",target="resolved")
    GrievanceDto toGrievanceDto(Grievance grievance);

    @Mapping(source="publicId",target="publicId")
    @Mapping(source="title",target="title")
    @Mapping(source="description",target="description")
    @Mapping(source="userFrom",target="userFrom")
    @Mapping(source="resolved",target="resolved")
    Grievance toGrievance(GrievanceDto grievanceDto);
}