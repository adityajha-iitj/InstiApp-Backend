package in.ac.iitj.instiapp.Tests.EntityTestData.Scheduling.Buses;

import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.BusSnippet;
import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.ScheduleType;

import static in.ac.iitj.instiapp.Tests.EntityTestData.Scheduling.Buses.BusSnippetData.BUS_SNIPPET1;
import static in.ac.iitj.instiapp.Tests.EntityTestData.Scheduling.Buses.BusSnippetData.BUS_SNIPPET2;

public enum BusRunData {

    BUS_RUN1(BUS_SNIPPET1,ScheduleType.WEEKDAY),
    BUS_RUN2(BUS_SNIPPET2,ScheduleType.WEEKDAY),
    BUS_RUN3(BUS_SNIPPET1,ScheduleType.WEEKEND),
    BUS_RUN4(BUS_SNIPPET2,ScheduleType.WEEKEND),
    ;



    public final BusSnippetData busSnippet;
    public  final ScheduleType scheduleType;

    BusRunData(BusSnippetData busSnippet , ScheduleType scheduleType){
        this.busSnippet = busSnippet;
        this.scheduleType = scheduleType;
    }
}
