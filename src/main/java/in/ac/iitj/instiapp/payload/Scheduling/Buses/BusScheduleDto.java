package in.ac.iitj.instiapp.payload.Scheduling.Buses;

import com.fasterxml.jackson.annotation.JsonProperty;
import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.BusSchedule;
import lombok.Value;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * DTO for {@link BusSchedule}
 */
@Value
public class BusScheduleDto implements Serializable {
    String busNumber;
    Set<BusRunDto> runs;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Set<BusOverrideDto> busOverrides;

    public BusScheduleDto(String busNumber, Set<BusRunDto> busRunDtos, Optional<Set<BusOverrideDto>> busOverrides ) {
        this.busNumber = busNumber;
        this.runs = busRunDtos;
        this.busOverrides = busOverrides.orElseGet(HashSet::new);
    }





}