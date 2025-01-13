package in.ac.iitj.instiapp.mappers.User.Organisation;

import in.ac.iitj.instiapp.database.entities.User.Organisation.Organisation;
import in.ac.iitj.instiapp.mappers.User.UserBaseDtoMapper;
import in.ac.iitj.instiapp.payload.User.Organisation.OrganisationBaseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel="spring",uses={UserBaseDtoMapper.class})
public interface OrganisationBaseDtoMapper {

    @Mapping(source="parentOrganisationUserUserName",target="parentOrganisation.user.userName")
    @Mapping(source="typeName",target="type.name")
    @Mapping(source="description",target="description")
    @Mapping(source="website",target="website")
    Organisation toOrganisation(OrganisationBaseDto organisationBaseDto);


    @Mapping(source="parentOrganisation.user.userName",target="parentOrganisationUserUserName")
    @Mapping(source="type.name",target="typeName")
    @Mapping(source="description",target="description")
    @Mapping(source="website",target="website")
    OrganisationBaseDto toOrganisationBaseDto(Organisation organisation);
}
