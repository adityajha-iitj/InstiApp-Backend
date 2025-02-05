package in.ac.iitj.instiapp.Tests.EntityTestData;

import in.ac.iitj.instiapp.database.entities.Media.Media;
import in.ac.iitj.instiapp.database.entities.User.Organisation.Organisation;
import in.ac.iitj.instiapp.database.entities.User.Organisation.OrganisationType;

import static in.ac.iitj.instiapp.Tests.EntityTestData.OrganisationTypeData.*;

public enum OrganisationData {
    ORGANISATION1( "society of alumni affairs", "saa.iitj.ac.in"),
    ORGANISATION2("quant club", "test"),
    ORGANISATION3("bha", "test1"),
    ORGANISATION4("hostel O4", "test2");



    public final String description;
    public final String website;


    OrganisationData( String description, String website) {

        this.description = description;
        this.website = website;
    }


    public Organisation toEntity() {
        return  new Organisation(null, null, null, null, description, null, website);
    }
}
