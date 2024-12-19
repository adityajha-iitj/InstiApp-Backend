package in.ac.iitj.instiapp.mappers.Scheduling.Buses;

import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.BusLocation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BusLocationDtoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name")
    BusLocation toEntity(String name);

    default String toDto(BusLocation busLocation) {
        if (busLocation == null) return null;

        return busLocation.getName();
    }

}
