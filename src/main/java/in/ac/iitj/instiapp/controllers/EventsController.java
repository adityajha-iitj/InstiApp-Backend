package in.ac.iitj.instiapp.controllers;

import in.ac.iitj.instiapp.Repository.UserRepository;
import in.ac.iitj.instiapp.config.JwtProvider;
import in.ac.iitj.instiapp.database.entities.User.Organisation.Organisation;
import in.ac.iitj.instiapp.database.entities.User.User;
import in.ac.iitj.instiapp.mappers.User.Organisation.OrganisationBaseDtoMapper;
import in.ac.iitj.instiapp.payload.Scheduling.Calendar.EventsDto;
import in.ac.iitj.instiapp.payload.User.Organisation.OrganisationBaseDto;
import in.ac.iitj.instiapp.payload.common.ApiResponse;
import in.ac.iitj.instiapp.services.EventsService;
import in.ac.iitj.instiapp.services.OrganisationService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
public class EventsController {

    private final EventsService eventsService;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final OrganisationService organisationService;
    private final OrganisationBaseDtoMapper organisationBaseDtoMapper;

    @Autowired
    public EventsController(EventsService eventsService, JwtProvider jwtProvider, UserRepository userRepository, OrganisationService organisationService, OrganisationBaseDtoMapper organisationBaseDtoMapper) {
        this.eventsService = eventsService;
        this.jwtProvider = jwtProvider;
        this.userRepository = userRepository;
        this.organisationService = organisationService;
        this.organisationBaseDtoMapper = organisationBaseDtoMapper;
    }

    @PostMapping("/save")
    public ApiResponse<EventsDto> saveEvent(@RequestHeader("Authorization") String jwt, @RequestBody EventsDto eventsDto) {
        String username = jwtProvider.getUsernameFromToken(jwt);
        //String username = "IITJ_OFFICIAL";


        Long publicId = eventsService.save(eventsDto, username);
        eventsDto.setPublicId(publicId);

        return new ApiResponse<>(
                HttpStatus.CREATED.value(),
                null,
                "Events Created Successfully",
                eventsDto,
                null
        );

    }

    @PutMapping("/update")
    public ApiResponse<EventsDto> updateEvent(@RequestHeader("Authorization") String jwt, @RequestBody EventsDto eventsDto) {
        String username = jwtProvider.getUsernameFromToken(jwt);

        eventsService.update(eventsDto, username);

        return new ApiResponse<>(
                HttpStatus.CREATED.value(),
                null,
                "Events Updated Successfully",
                eventsDto,
                null
        );

    }
}
