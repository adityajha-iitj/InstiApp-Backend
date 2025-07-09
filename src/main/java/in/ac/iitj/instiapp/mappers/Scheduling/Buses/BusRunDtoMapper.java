package in.ac.iitj.instiapp.mappers.Scheduling.Buses;

import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.BusRun;
import in.ac.iitj.instiapp.payload.Scheduling.Buses.BusRunDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = BusRouteDtoMapper.class)
public interface BusRunDtoMapper {
    BusRunDto toDto(BusRun busRun);
    BusRun toEntity(BusRunDto busRunDto);
}

