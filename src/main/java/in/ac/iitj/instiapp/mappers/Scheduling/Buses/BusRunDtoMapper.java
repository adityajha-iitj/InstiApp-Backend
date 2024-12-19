package in.ac.iitj.instiapp.mappers.Scheduling.Buses;

import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.BusRun;
import in.ac.iitj.instiapp.payload.Scheduling.Buses.BusRunDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = BusLocationDtoMapper.class)
public interface BusRunDtoMapper {


    @Mapping(source = "busSnippet.timeOfDeparture", target = "timeOfDeparture")
    @Mapping(source = "busSnippet.fromLocation", target = "fromLocationName")
    @Mapping(source = "busSnippet.toLocation", target = "toLocationName")
    BusRunDto toDto(BusRun busRun);

    @InheritInverseConfiguration
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "busSchedule", ignore = true)
    BusRun toEntity(BusRunDto busRunDto);

}

