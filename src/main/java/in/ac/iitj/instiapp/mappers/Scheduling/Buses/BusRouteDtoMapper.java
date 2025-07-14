package in.ac.iitj.instiapp.mappers.Scheduling.Buses;

import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.BusRoute;
import in.ac.iitj.instiapp.payload.Scheduling.Buses.BusRouteDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = RouteStopDtoMapper.class)
public interface BusRouteDtoMapper {
    BusRouteDto toDto(BusRoute busRoute);
    BusRoute toEntity(BusRouteDto dto);
}
