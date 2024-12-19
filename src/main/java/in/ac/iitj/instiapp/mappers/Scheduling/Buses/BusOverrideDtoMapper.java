package in.ac.iitj.instiapp.mappers.Scheduling.Buses;

import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.BusOverride;
import in.ac.iitj.instiapp.payload.Scheduling.Buses.BusOverrideDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.sql.Date;
import java.sql.Time;

@Mapper(componentModel = "spring",uses = {BusLocationDtoMapper.class})
public interface BusOverrideDtoMapper {



    @Mapping(source = "busSchedule.busNumber",target = "busScheduleBusNumber")
    @Mapping(source = "busSnippet.timeOfDeparture", target = "timeOfDeparture")
    @Mapping(source = "busSnippet.fromLocation", target = "fromLocationName")
    @Mapping(source = "busSnippet.toLocation", target = "toLocationName")
    BusOverrideDto toDto(BusOverride busOverride);

    @InheritInverseConfiguration
    @Mapping(ignore = true,target = "id")
    BusOverride toEntity(BusOverrideDto busOverrideDto);
}
