package in.ac.iitj.instiapp.mappers.User.Organisation;


import in.ac.iitj.instiapp.database.entities.User.Organisation.OrganisationRole;
import in.ac.iitj.instiapp.payload.User.Organisation.OrganisationRoleDto;
import org.mapstruct.*;

@Mapper(componentModel="spring")
public interface OrganisationRoleDtoMapper {

    @Mapping(source="organisationUsername",target="organisation.user.userName")
    @Mapping(source="roleName",target="roleName")
    @Mapping(source="permission",target="permission")
    OrganisationRole toOrganisationRoleDto(OrganisationRoleDto organisationRoleDto);

    @Mapping(source="organisation.user.userName",target="organisationUsername")
    @Mapping(source="roleName",target="roleName")
    @Mapping(source="permission",target="permission")
    OrganisationRoleDto toOrganisationRole(OrganisationRole organisationRole);

}
