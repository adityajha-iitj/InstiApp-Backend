package in.ac.iitj.instiapp.services;

import in.ac.iitj.instiapp.payload.Scheduling.Calendar.EventsDto;

public interface EventsService {

    public Long save(EventsDto eventsDto, String username);

    public void update(EventsDto eventsDto, String username);

}
