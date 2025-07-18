package in.ac.iitj.instiapp.services;

import in.ac.iitj.instiapp.database.entities.Scheduling.Calendar.Events;
import in.ac.iitj.instiapp.payload.Scheduling.Calendar.EventsDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface EventsService {

    public Long save(EventsDto eventsDto, String username, List<MultipartFile> files) throws IOException;

    public List<String> getMediaUrlsOfEvent(Long publicId);

    public void update(
            EventsDto               eventsDto,
            String                  username,
            List<MultipartFile>     newFiles
    ) throws IOException;

    public List<EventsDto> getAllEvents();

    public void delete(Long publicId, String username);
}
