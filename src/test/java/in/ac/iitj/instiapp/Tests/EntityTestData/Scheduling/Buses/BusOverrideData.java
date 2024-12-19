package in.ac.iitj.instiapp.Tests.EntityTestData.Scheduling.Buses;

import in.ac.iitj.instiapp.Tests.Utilities.Conversions;
import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.BusOverride;

import java.time.LocalDate;
import java.util.Date;

public enum BusOverrideData {

    BUS_OVERRIDE1(
            "PUBLIC_ID1",
            BusSnippetData.BUS_SNIPPET3,
            Conversions.convertLocalDateToDate(LocalDate.of(2024, 12, 19)),
            "Bus is cancelled due to winter"
    ),

    BUS_OVERRIDE2(
            "PUBLIC_ID2",
            BusSnippetData.BUS_SNIPPET2,
            Conversions.convertLocalDateToDate(LocalDate.of(2024, 12, 15)),
            "Extra Bus is  arranged due to winter"
    ),

    BUS_OVERRIDE3(
            "PUBLIC_ID3",
            BusSnippetData.BUS_SNIPPET3,
            Conversions.convertLocalDateToDate(LocalDate.of(2024,4,2)),
            "Bus is cancelled due to winter"
    ),
    BUS_OVERRIDE4(
            "PUBLIC_ID4",
            BusSnippetData.BUS_SNIPPET3,
            Conversions.convertLocalDateToDate(LocalDate.of(2024,12,2)),
            "Bus is cancelled due to winter"
            );


    public final String publicId;
    public final BusSnippetData busSnippetData;
    public final Date overrideDate;
    public final String description;


    BusOverrideData(String publicId, BusSnippetData busSnippetData, Date overrideDate, String description) {
        this.publicId = publicId;
        this.busSnippetData = busSnippetData;
        this.overrideDate = overrideDate;
        this.description = description;
    }


    public BusOverride toEntity() {
        return new BusOverride(
                this.publicId,
                busSnippetData.toEntity(),
                overrideDate,
                description
        );
    }


}
