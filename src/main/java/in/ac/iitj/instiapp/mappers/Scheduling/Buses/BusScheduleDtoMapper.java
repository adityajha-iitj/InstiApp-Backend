package in.ac.iitj.instiapp.mappers.Scheduling.Buses;

import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.BusOverride;
import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.BusSchedule;
import in.ac.iitj.instiapp.payload.Scheduling.Buses.BusOverrideDto;
import in.ac.iitj.instiapp.payload.Scheduling.Buses.BusRunDto;
import in.ac.iitj.instiapp.payload.Scheduling.Buses.BusScheduleDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",uses = {BusRunDto.class, BusOverrideDto.class})
public interface BusScheduleDtoMapper {

    final BusOverrideDtoMapper busOverrideDtoMapper = Mappers.getMapper(BusOverrideDtoMapper.class);


    BusScheduleDto toDto(BusSchedule busSchedule);

    BusSchedule toEntity(BusScheduleDto busScheduleDto);

    default Optional<Set<BusOverrideDto>> toBusOverrideDtos(Set<BusOverride> overrides) {
        if(overrides == null || overrides.isEmpty()) {
            return Optional.empty();
        }
        return  Optional.of(overrides.stream().map(busOverrideDtoMapper::toDto)
                .collect(Collectors.toSet()));
    }
}
