package in.ac.iitj.instiapp.Tests.EntityTestData;

import in.ac.iitj.instiapp.database.entities.User.Organisation.OrganisationType;

public enum OrganisationTypeData {
    ORGANISATION_TYPE1("Society"),
    ORGANISATION_TYPE2("Club"),
    ORGANISATION_TYPE3("Board"),
    ORGANISATION_TYPE4("Hostel");


    public final String name;



    OrganisationTypeData(String name) {
        this.name = name;
    }


    public OrganisationType toEntity() {
        return  new OrganisationType(null, this.name);
    }
}
