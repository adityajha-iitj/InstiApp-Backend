package in.ac.iitj.instiapp.Tests.EntityTestData;

import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.BusLocation;

public enum BusLocationData {
    BUS_LOCATION_DATA1("MBM College"),
    BUS_LOCATION_DATA2("IIT Jodhpur");

    private final String name;
    private BusLocationData(String name) {
        this.name = name;
    }
    public BusLocation toEntity() {
        return new BusLocation(this.name);
    }
}
