package in.ac.iitj.instiapp.mappers;


import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.BusOverride;
import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.BusRun;
import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.BusSchedule;
import in.ac.iitj.instiapp.payload.Scheduling.Buses.BusOverrideDto;
import in.ac.iitj.instiapp.payload.Scheduling.Buses.BusRunDto;
import in.ac.iitj.instiapp.payload.Scheduling.Buses.BusScheduleDto;
import org.mapstruct.Mapper;

@Mapper
public interface BusDtoMappers {


    BusRunDto toBusRunDto(BusRun busRun);
    BusRun fromBusRunDto(BusRunDto busRunDto);

    BusOverrideDto toBusOverrideDto(BusOverride busOverride);
    BusOverride fromBusOverrideDto(BusOverrideDto busOverrideDto);

    BusScheduleDto toBusScheduleDto(BusSchedule busSchedule);
    BusSchedule fromBusScheduleDto(BusScheduleDto busScheduleDto);

}
