package in.ac.iitj.instiapp.Tests.EntityTestData.Scheduling.Buses;

import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.BusRun;
import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.BusSnippet;
import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.ScheduleType;

import static in.ac.iitj.instiapp.Tests.EntityTestData.Scheduling.Buses.BusSnippetData.BUS_SNIPPET1;
import static in.ac.iitj.instiapp.Tests.EntityTestData.Scheduling.Buses.BusSnippetData.BUS_SNIPPET2;

public enum BusRunData {


    BUS_RUN1("PUBLIC_ID1",BUS_SNIPPET1,ScheduleType.WEEKDAY),
    BUS_RUN2("PUBLIC_ID2",BUS_SNIPPET2,ScheduleType.WEEKDAY),
    BUS_RUN3("PUBLIC_ID3",BUS_SNIPPET1,ScheduleType.WEEKEND),
    BUS_RUN4("PUBLIC_ID4",BUS_SNIPPET2,ScheduleType.WEEKEND),
    ;



    public final String publicId;
    public final BusSnippetData busSnippet;
    public  final ScheduleType scheduleType;

    BusRunData(String publicId,BusSnippetData busSnippet , ScheduleType scheduleType){
        this.publicId = publicId;
        this.busSnippet = busSnippet;
        this.scheduleType = scheduleType;
    }

    public BusRun toEntity(){
        return new BusRun(publicId,busSnippet.toEntity(),scheduleType);
    }
}
