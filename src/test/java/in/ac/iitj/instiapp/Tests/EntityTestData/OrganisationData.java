package in.ac.iitj.instiapp.Tests.EntityTestData;

import in.ac.iitj.instiapp.database.entities.Media.Media;
import in.ac.iitj.instiapp.database.entities.User.Organisation.Organisation;
import in.ac.iitj.instiapp.database.entities.User.Organisation.OrganisationType;

import static in.ac.iitj.instiapp.Tests.EntityTestData.OrganisationTypeData.*;

public enum OrganisationData {
    ORGANISATION1(ORGANISATION_TYPE1, "society of alumni affairs", "saa.iitj.ac.in"),
    ORGANISATION2(ORGANISATION_TYPE2, "quant club", "test"),
    ORGANISATION3(ORGANISATION_TYPE3, "bha", "test1"),
    ORGANISATION4(ORGANISATION_TYPE4, "hostel O4", "test2");


    public final OrganisationTypeData organisationType;
    public final String description;
    public final String website;


    OrganisationData(OrganisationTypeData organisationType, String description, String website) {
        this.organisationType = organisationType;
        this.description = description;
        this.website = website;
    }


    public Organisation toEntity() {
        return  new Organisation(null, null, null, organisationType.toEntity(), description, null, website);
    }
}
