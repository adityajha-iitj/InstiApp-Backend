package in.ac.iitj.instiapp.mappers.Scheduling.Calendar;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import in.ac.iitj.instiapp.database.entities.Scheduling.Calendar.Events;
import in.ac.iitj.instiapp.payload.Scheduling.Calendar.EventsDto;

@Mapper(
        componentModel       = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface EventsMapper {

    /** Entity → DTO: will pick up Title, Description, Date, startTime, Duration, isAllDay, isRecurring, isHide */
    EventsDto toDto(Events entity);

    /**
     * DTO → Entity: will set only the 8 matching fields; everything else
     * (id, calendar, user, recurrence, etc.) is ignored.
     */
    @Mapping(target = "id",         ignore = true)
    @Mapping(target = "calendar",   ignore = true)
    @Mapping(target = "organisation",       ignore = true)
    @Mapping(target = "recurrence", ignore = true)
    Events toEntity(EventsDto dto);
}
