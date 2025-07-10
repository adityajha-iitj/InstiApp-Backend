package in.ac.iitj.instiapp.mappers.Scheduling.Buses;

import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.BusRun;
import in.ac.iitj.instiapp.payload.Scheduling.Buses.BusRunDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = BusRouteDtoMapper.class)
public interface BusRunDtoMapper {
    @Mapping(target = "busNumber", source = "busSchedule.busNumber")
    BusRunDto toDto(BusRun busRun);

    @Mapping(target = "busSchedule.busNumber", source="busNumber")
    BusRun toEntity(BusRunDto busRunDto);
}

