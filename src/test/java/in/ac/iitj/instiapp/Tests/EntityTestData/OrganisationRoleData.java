package in.ac.iitj.instiapp.Tests.EntityTestData;

import in.ac.iitj.instiapp.database.entities.User.Organisation.OrganisationPermission;
import in.ac.iitj.instiapp.database.entities.User.Organisation.OrganisationRole;
import static in.ac.iitj.instiapp.database.entities.User.Organisation.OrganisationPermission.*;



public enum OrganisationRoleData {
    ORGANISATION_ROLE1("Coordinator",MASTER),
    ORGANISATION_ROLE2("Vice President",INTERMEDIATE),
    ORGANISATION_ROLE3("President",READ),
    ORGANISATION_ROLE4("Support Team Member",INTERMEDIATE);


    public final String roleName;
    public final OrganisationPermission organisationPermission;

    OrganisationRoleData(String roleName, OrganisationPermission organisationPermission){
        this.roleName = roleName;
        this.organisationPermission = organisationPermission;
    }

    public OrganisationRole toEntity() {
        return new OrganisationRole(null, null, this.roleName, this.organisationPermission);
    }

}
