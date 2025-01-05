//package in.ac.iitj.instiapp.mappers;
//
//import in.ac.iitj.instiapp.database.entities.Scheduling.Calendar.Calendar;
//import in.ac.iitj.instiapp.database.entities.Scheduling.Calendar.Events;
//import in.ac.iitj.instiapp.payload.Scheduling.Calendar.CalendarDto;
//import in.ac.iitj.instiapp.payload.Scheduling.Calendar.EventsDto;
//import org.mapstruct.*;
//
//@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
//public interface CalendarDtoMapper {
//    @Mapping(source = "userEmail", target = "user.email")
//    @Mapping(source = "userUserName", target = "user.userName")
//    @Mapping(source = "userName", target = "user.name")
//    Calendar toEntity(CalendarDto calendarDto);
//
//    @AfterMapping
//    default void linkEvents(@MappingTarget Calendar calendar) {
//        calendar.getEvents().forEach(event -> event.setCalendar(calendar));
//    }
//
//    @InheritInverseConfiguration(name = "toEntity")
//    CalendarDto toDto(Calendar calendar);
//
//    @InheritConfiguration(name = "toEntity")
//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    Calendar partialUpdate(CalendarDto calendarDto, @MappingTarget Calendar calendar);
//
//    @Mapping(source = "recurrenceInterval", target = "recurrence.interval")
//    @Mapping(source = "recurrenceCount", target = "recurrence.count")
//    @Mapping(source = "recurrenceUntil", target = "recurrence.until")
//    @Mapping(source = "recurrenceFrequency", target = "recurrence.Frequency")
//    @Mapping(source = "calendarUserUserName", target = "user.userName")
//    @Mapping(source = "calendarPublic_id", target = "calendar.public_id")
//    Events toEntity(EventsDto eventsDto);
//
//    @InheritInverseConfiguration(name = "toEntity")
//    EventsDto toDto(Events events);
//
//    @InheritConfiguration(name = "toEntity")
//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    Events partialUpdate(EventsDto eventsDto, @MappingTarget Events events);
//}