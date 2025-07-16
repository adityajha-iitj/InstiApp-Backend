package in.ac.iitj.instiapp.services.impl;

import in.ac.iitj.instiapp.Repository.EventsRepository;
import in.ac.iitj.instiapp.Repository.impl.OrganisationRepositoryImpl;
import in.ac.iitj.instiapp.database.entities.Scheduling.Calendar.Events;
import in.ac.iitj.instiapp.database.entities.User.Organisation.Organisation;
import in.ac.iitj.instiapp.mappers.Scheduling.Calendar.EventsMapper;
import in.ac.iitj.instiapp.payload.Scheduling.Calendar.EventsDto;
import in.ac.iitj.instiapp.services.EventsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventsServiceImpl implements EventsService {

    private final EventsRepository eventsRepository;
    private final EventsMapper eventsMapper;
    private final OrganisationRepositoryImpl organisationRepositoryImpl;

    @Autowired
    public EventsServiceImpl(EventsRepository eventsRepository, EventsMapper eventsMapper, OrganisationRepositoryImpl organisationRepositoryImpl) {
        this.eventsRepository = eventsRepository;
        this.eventsMapper = eventsMapper;
        this.organisationRepositoryImpl = organisationRepositoryImpl;
    }

    @Transactional
    public Long save(EventsDto eventsDto, String username) {
        Events events = eventsMapper.toEntity(eventsDto);
        events.setOrganisation(organisationRepositoryImpl.getOrganisationByUsername(username));

        try {
            Events e = eventsRepository.save(events);
            return e.getPublicId();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Transactional
    public void update(EventsDto eventsDto, String username) {
        Organisation organisation = organisationRepositoryImpl.getOrganisationByUsername(username); // your existing function


        Events existingEvent = eventsRepository.findByEventByPublicId(eventsDto.getPublicId());

        if (!existingEvent.getOrganisation().getId().equals(organisation.getId())) {
            throw new AccessDeniedException("You do not have permission to update this event.");
        }

        // Perform update (map values from DTO to existingEvent)
        existingEvent.setTitle(eventsDto.getTitle());
        existingEvent.setDescription(eventsDto.getDescription());
        existingEvent.setDate(eventsDto.getDate());
        existingEvent.setStartTime(eventsDto.getStartTime());
        existingEvent.setDuration(eventsDto.getDuration());
        existingEvent.setIsAllDay(eventsDto.getIsAllDay());
        existingEvent.setIsRecurring(eventsDto.getIsRecurring());
        existingEvent.setIsHide(eventsDto.getIsHide());



        eventsRepository.save(existingEvent);
    }

}
