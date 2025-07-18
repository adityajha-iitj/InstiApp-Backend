package in.ac.iitj.instiapp.controllers;

import in.ac.iitj.instiapp.Repository.AnnouncementsRepository;
import in.ac.iitj.instiapp.Repository.UserRepository;
import in.ac.iitj.instiapp.config.JwtProvider;
import in.ac.iitj.instiapp.database.entities.User.Organisation.Organisation;
import in.ac.iitj.instiapp.database.entities.User.User;
import in.ac.iitj.instiapp.mappers.User.Organisation.OrganisationBaseDtoMapper;
import in.ac.iitj.instiapp.payload.AnnouncementsDto;
import in.ac.iitj.instiapp.payload.Scheduling.Calendar.EventsDto;
import in.ac.iitj.instiapp.payload.User.Organisation.OrganisationBaseDto;
import in.ac.iitj.instiapp.payload.common.ApiResponse;
import in.ac.iitj.instiapp.services.AnnouncementsService;
import in.ac.iitj.instiapp.services.EventsService;
import in.ac.iitj.instiapp.services.OrganisationService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventsController {

    private final EventsService eventsService;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final OrganisationService organisationService;
    private final OrganisationBaseDtoMapper organisationBaseDtoMapper;
    private final AnnouncementsService announcementsService;

    @Autowired
    public EventsController(EventsService eventsService, JwtProvider jwtProvider, UserRepository userRepository, OrganisationService organisationService, OrganisationBaseDtoMapper organisationBaseDtoMapper, AnnouncementsService announcementsService) {
        this.eventsService = eventsService;
        this.jwtProvider = jwtProvider;
        this.userRepository = userRepository;
        this.organisationService = organisationService;
        this.organisationBaseDtoMapper = organisationBaseDtoMapper;
        this.announcementsService = announcementsService;
    }

    @PostMapping(
            path     = "/save",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ApiResponse<EventsDto>> saveEvent(@RequestHeader("Authorization") String jwt, @RequestPart("eventsDto") EventsDto eventsDto,
                                               @RequestPart(value = "media", required = false) List<MultipartFile> files,
                                                            @RequestParam("shouldAnnounce") Boolean shouldAnnounce) throws IOException {
        String username = jwtProvider.getUsernameFromToken(jwt);
        //String username = "IITJ_OFFICIAL";



        Long publicId = eventsService.save(eventsDto, username, files);
        eventsDto.setPublicId(publicId);

        List<String> mediaUrls = eventsService.getMediaUrlsOfEvent(publicId);
        eventsDto.setEventsMediauRL(mediaUrls);


        if(shouldAnnounce) {
            AnnouncementsDto ann = new AnnouncementsDto();
            ann.setTitle(eventsDto.getTitle());
            ann.setDescription(eventsDto.getDescription());
            ann.setDateOfAnnouncement(eventsDto.getDate());
            ann.setTimeOfAccouncement(Time.valueOf(LocalTime.now()));
            announcementsService.saveForEventAnnouncement(ann,username,mediaUrls);
        }
        return ResponseEntity.ok(
                new ApiResponse<>(
                        HttpStatus.CREATED.value(),
                        null,
                        "Events Saved Successfully",
                        eventsDto,
                        null
                )
        );

    }

    @PutMapping(
            path     = "/update",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ApiResponse<EventsDto>> updateEvent(
            @RequestHeader("Authorization")     String jwt,
            @RequestPart("eventsDto")           EventsDto           eventsDto,
            @RequestPart(value = "media", required = false)
            List<MultipartFile> newFiles
    ) throws IOException {
        String username = jwtProvider.getUsernameFromToken(jwt);

        // call your updated service method that merges existing URLs + new files
        eventsService.update(eventsDto, username, newFiles);

        // fetch the post‑update list of URLs and set on DTO
        List<String> updatedUrls = eventsService.getMediaUrlsOfEvent(eventsDto.getPublicId());
        eventsDto.setEventsMediauRL(updatedUrls);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        HttpStatus.OK.value(),
                        null,
                        "Events updated Successfully",
                        eventsDto,
                        null
                )
        );
    }

    @GetMapping("/getAllEvents")
    public ResponseEntity<ApiResponse<List<EventsDto>>> getAllEvents(@RequestHeader("Authorization") String jwt) throws IOException {
        try{
            String username = jwtProvider.getUsernameFromToken(jwt);
            List<EventsDto> events = eventsService.getAllEvents();

            return ResponseEntity.ok(
                    new ApiResponse<>(
                            HttpStatus.OK.value(),
                            null,
                            "Events fetched Successfully",
                            events,
                            null
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "EVENTS_FETCH_ERROR",
                            "Error fetching the events " + e.getMessage(),
                            null,
                            null
                    ));
        }


    }

    @DeleteMapping("/delete/{publicId}")
    public ResponseEntity<String> deleteEvent(@RequestHeader("Authorization") String jwt ,@PathVariable("publicId") Long publicId) {
        String username = jwtProvider.getUsernameFromToken(jwt);

        try{
            eventsService.delete(publicId, username);
            return ResponseEntity.ok("Event deleted Successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("ERROR DELETING EVENT");
        }
    }
}
