package in.ac.iitj.instiapp.Tests.EntityTestData;

import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.BusSchedule;

public enum BusScheduleData {
    BUS_SCHEDULE_DATA1("B1");

    private final String busname;

    BusScheduleData(String busname) {
        this.busname = busname;
    }

    public BusSchedule toEntity() {
        return new BusSchedule(busname);
    }
}

