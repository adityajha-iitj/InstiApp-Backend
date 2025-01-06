package in.ac.iitj.instiapp.Tests.EntityTestData;

import in.ac.iitj.instiapp.database.entities.LostnFound.Locations;



public enum LocationData {
    LOCATION1("LOCATION1"),
    LOCATION2("LOCATION2"),
    LOCATION3("LOCATION3"),
    LOCATION4("LOCATION4");

    public final String name;

    LocationData(String name) {
        this.name = name;
    }

    public Locations toEntity() {
        return new Locations(this.name);
    }
}
