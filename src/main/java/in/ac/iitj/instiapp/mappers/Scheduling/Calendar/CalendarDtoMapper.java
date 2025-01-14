package in.ac.iitj.instiapp.mappers.Scheduling.Calendar;

import in.ac.iitj.instiapp.database.entities.Scheduling.Calendar.Calendar;
import in.ac.iitj.instiapp.payload.Scheduling.Calendar.CalendarDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses={CalendarEventsDtoMapper.class})
public interface CalendarDtoMapper {


    @Mapping(source="public_id",target="publicId")
    @Mapping(source="userName",target="user.userName")
    @Mapping(source="userEmail",target="user.email")
    Calendar toCalendar(CalendarDto calendarDto);

    @Mapping(source="publicId",target="public_id")
    @Mapping(source="user.userName",target="userName")
    @Mapping(source="user.email",target="userEmail")
    CalendarDto toCalendarDto(Calendar calendar);
}
