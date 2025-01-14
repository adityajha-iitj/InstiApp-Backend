package in.ac.iitj.instiapp.mappers.Scheduling.Calendar;

import in.ac.iitj.instiapp.database.entities.Scheduling.Calendar.Events;
import in.ac.iitj.instiapp.payload.Scheduling.Calendar.CalendarDto.CalendarEventsDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CalendarEventsDtoMapper {

    @Mapping(source="title",target="title")
    @Mapping(source="description",target="description")
    @Mapping(source="date",target="date")
    @Mapping(source="startTime",target="startTime")
    @Mapping(source="duration",target="duration")
    @Mapping(source="isAllDay",target="isAllDay")
    @Mapping(source="isRecurring",target="isRecurring")
    @Mapping(source="recurrenceFrequency",target="recurrence.frequency")
    @Mapping(source="recurrenceUntil",target="recurrence.until")
    @Mapping(source="recurrenceCount",target="recurrence.count")
    @Mapping(source="recurrenceInterval",target="recurrence.interval")
    @Mapping(source="isHide",target="isHide")
    Events toEvents(CalendarEventsDto calendarEventsDto);

    @Mapping(source="title",target="title")
    @Mapping(source="description",target="description")
    @Mapping(source="date",target="date")
    @Mapping(source="startTime",target="startTime")
    @Mapping(source="duration",target="duration")
    @Mapping(source="isAllDay",target="isAllDay")
    @Mapping(source="isRecurring",target="isRecurring")
    @Mapping(source="recurrence.frequency",target="recurrenceFrequency")
    @Mapping(source="recurrence.until",target="recurrenceUntil")
    @Mapping(source="recurrence.count",target="recurrenceCount")
    @Mapping(source="recurrence.interval",target="recurrenceInterval")
    @Mapping(source="isHide",target="isHide")
    CalendarEventsDto toCalendarEventsDto(Events events);

}
