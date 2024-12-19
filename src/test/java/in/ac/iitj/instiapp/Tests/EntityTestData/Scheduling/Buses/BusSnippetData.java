package in.ac.iitj.instiapp.Tests.EntityTestData.Scheduling.Buses;

import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.BusSnippet;

import java.sql.Time;
import java.time.LocalTime;

import static in.ac.iitj.instiapp.Tests.EntityTestData.Scheduling.Buses.BusLocationData.*;

public enum BusSnippetData {
    BUS_SNIPPET1(
            Time.valueOf(LocalTime.of(10, 00)),
            BUS_LOCATION1, BUS_LOCATION2
    ),
    BUS_SNIPPET2(
            Time.valueOf(LocalTime.of(16, 20)),
            BUS_LOCATION2, BUS_LOCATION1
    ),
    BUS_SNIPPET3(Time.valueOf(LocalTime.of(11,00)),
            BUS_LOCATION2,BUS_LOCATION3),
    BUS_SNIPPET4(Time.valueOf(LocalTime.of(17,30)),
            BUS_LOCATION3,BUS_LOCATION2
            );



    public final Time timeOfDeparture;

    public final BusLocationData fromBusLocation;
    public final BusLocationData toBusLocation;

    BusSnippetData(Time timeOfDeparture, BusLocationData fromBusLocation, BusLocationData toBusLocation) {
        this.timeOfDeparture = timeOfDeparture;
        this.fromBusLocation = fromBusLocation;
        this.toBusLocation = toBusLocation;

    }

    public BusSnippet toEntity(){
        return  new BusSnippet(this.timeOfDeparture, this.fromBusLocation.toEntity(), this.toBusLocation.toEntity());
    }
}
