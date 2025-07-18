package in.ac.iitj.instiapp.services.impl;

import in.ac.iitj.instiapp.Repository.EventsRepository;
import in.ac.iitj.instiapp.Repository.impl.OrganisationRepositoryImpl;
import in.ac.iitj.instiapp.database.entities.Media.Media;
import in.ac.iitj.instiapp.database.entities.Scheduling.Calendar.Events;
import in.ac.iitj.instiapp.database.entities.User.Organisation.Organisation;
import in.ac.iitj.instiapp.mappers.Scheduling.Calendar.EventsMapper;
import in.ac.iitj.instiapp.payload.Scheduling.Calendar.EventsDto;
import in.ac.iitj.instiapp.services.BucketService;
import in.ac.iitj.instiapp.services.EventsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class EventsServiceImpl implements EventsService {

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    private final EventsRepository eventsRepository;
    private final EventsMapper eventsMapper;
    private final OrganisationRepositoryImpl organisationRepositoryImpl;
    private final BucketService bucketService;

    @Autowired
    public EventsServiceImpl(EventsRepository eventsRepository, EventsMapper eventsMapper, OrganisationRepositoryImpl organisationRepositoryImpl, BucketService bucketService) {
        this.eventsRepository = eventsRepository;
        this.eventsMapper = eventsMapper;
        this.organisationRepositoryImpl = organisationRepositoryImpl;
        this.bucketService = bucketService;
    }

    @Transactional
    public Long save(EventsDto eventsDto, String username, List<MultipartFile> files) throws IOException {
        try {
        Events events = eventsMapper.toEntity(eventsDto);
        events.setOrganisation(organisationRepositoryImpl.getOrganisationByUsername(username));

        String s3Url = "";
        Set<Media> mediaSet = new HashSet<>();
        // 2. If a new avatar file was provided, upload it to S3
        for (MultipartFile file : files) {

            // 2.a. Generate a unique key under an "avatars/" folder
            String extension =
                    file.getOriginalFilename()
                            .substring(file.getOriginalFilename().lastIndexOf('.'));
            String objectKey = "events/" + username + "/"
                    + UUID.randomUUID() + extension;

            // 2.b. Save to a temp file
            File temp = File.createTempFile("events-", extension);
            file.transferTo(temp);

            // 2.c. Upload & get URL
            bucketService.uploadFile(bucketName, objectKey, temp.getAbsolutePath());
            s3Url = bucketService.getFileUrl(bucketName, objectKey);

            Media media = new Media();
            media.setPublicUrl(s3Url);

            mediaSet.add(media);

            // 2.d. Clean up the temp file
            temp.delete();

            // 2.e. Set the S3 URL on your user DTO
        }

        events.setMedia(mediaSet);

            Events e = eventsRepository.save(events);
            return e.getPublicId();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public List<String> getMediaUrlsOfEvent(Long publicId){
        return eventsRepository.getEventMediaUrl(publicId);
    }

    @Transactional
    public void update(
            EventsDto               eventsDto,
            String                  username,
            List<MultipartFile>     newFiles   // ← accept any newly uploaded files
    ) throws IOException {
        Organisation org = organisationRepositoryImpl.getOrganisationByUsername(username);
        Events existingEvent = eventsRepository.findByEventByPublicId(eventsDto.getPublicId());
        if (!existingEvent.getOrganisation().getId().equals(org.getId())) {
            throw new AccessDeniedException("You do not have permission to update this event.");
        }

        // 2. Update scalar fields
        existingEvent.setTitle(eventsDto.getTitle());
        existingEvent.setDescription(eventsDto.getDescription());
        existingEvent.setDate(eventsDto.getDate());
        existingEvent.setStartTime(eventsDto.getStartTime());
        existingEvent.setDuration(eventsDto.getDuration());
        existingEvent.setIsAllDay(eventsDto.getIsAllDay());
        existingEvent.setIsRecurring(eventsDto.getIsRecurring());
        existingEvent.setIsHide(eventsDto.getIsHide());

        // 3. Rebuild media set from DTO’s existing URLs
        Set<Media> mediaSet = new HashSet<>();
        if (eventsDto.getEventsMediauRL() != null) {
            for (String url : eventsDto.getEventsMediauRL()) {
                Media m = new Media();
                m.setPublicUrl(url);
                mediaSet.add(m);
            }
        }

        // 4. Upload any new files and add their URLs
        if (newFiles != null) {
            for (MultipartFile file : newFiles) {
                String ext       = file.getOriginalFilename()
                        .substring(file.getOriginalFilename().lastIndexOf('.'));
                String objectKey = "events/" + username + "/" + UUID.randomUUID() + ext;

                File tmp = File.createTempFile("events-", ext);
                file.transferTo(tmp);
                bucketService.uploadFile(bucketName, objectKey, tmp.getAbsolutePath());
                String s3Url = bucketService.getFileUrl(bucketName, objectKey);
                tmp.delete();

                Media m = new Media();
                m.setPublicUrl(s3Url);
                mediaSet.add(m);
            }
        }

        // 5. Replace the entity’s media set & save
        existingEvent.getMedia().clear();          // remove all old entries (orphans get deleted)
        existingEvent.getMedia().addAll(mediaSet); // add back the merged set
        eventsRepository.save(existingEvent);
    }

    public List<EventsDto> getAllEvents(){
        List<Events> eventsList = eventsRepository.findAll();
        List<EventsDto> eventsDtoList = new ArrayList<>();
        for (Events event : eventsList) {
            EventsDto eventsDto = eventsMapper.toDto(event);
            eventsDto.setEventsMediauRL(eventsRepository.getEventMediaUrl(event.getPublicId()));
            eventsDto.setOwnerUsername(event.getOrganisation().getUser().getUserName());
            eventsDtoList.add(eventsDto);
        }
        return eventsDtoList;
    }

    @Transactional
    public void delete(Long publicId, String username){

        Organisation org = organisationRepositoryImpl.getOrganisationByUsername(username);
        Events existingEvent = eventsRepository.findByEventByPublicId(publicId);
        if (!existingEvent.getOrganisation().getId().equals(org.getId())) {
            throw new AccessDeniedException("You do not have permission to delete this event.");
        }
        Events eve = eventsRepository.findByEventByPublicId(publicId);
        eventsRepository.delete(eve);
    }

}
