package in.ac.iitj.instiapp.mappers.Scheduling.Buses;

import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.RouteStop;
import in.ac.iitj.instiapp.payload.Scheduling.Buses.RouteStopDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RouteStopDtoMapper {
    @Mapping(source = "location.name", target = "locationName")
    RouteStopDto toDto(RouteStop routeStop);
    RouteStop toEntity(RouteStopDto dto);
}
