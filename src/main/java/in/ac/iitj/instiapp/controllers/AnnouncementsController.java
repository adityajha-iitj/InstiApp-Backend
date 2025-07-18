package in.ac.iitj.instiapp.controllers;

import in.ac.iitj.instiapp.config.JwtProvider;
import in.ac.iitj.instiapp.payload.AnnouncementsDto;
import in.ac.iitj.instiapp.payload.Scheduling.Calendar.EventsDto;
import in.ac.iitj.instiapp.payload.common.ApiResponse;
import in.ac.iitj.instiapp.services.AnnouncementsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/announcements")
public class AnnouncementsController {


    private final AnnouncementsService announcementsService;
    private final JwtProvider jwtProvider;

    @Autowired
    public AnnouncementsController(AnnouncementsService announcementsService, JwtProvider jwtProvider) {
        this.announcementsService = announcementsService;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping(path = "/save",
    consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<AnnouncementsDto>> createAnnouncement(@RequestHeader("Authorization") String jwt ,
                                                                            @RequestPart("dto") AnnouncementsDto announcementsDto,
                                                                            @RequestPart("media") List<MultipartFile> files) {

        String username = jwtProvider.getUsernameFromToken(jwt);

        try{
            Long publicId = announcementsService.save(announcementsDto,username,files);
            announcementsDto.setPublicId(publicId);
            announcementsDto.setOrganisationUserUserName(username);

            List<String> mediaUrls = announcementsService.getMediaUrlsOfAnnouncement(publicId);
            announcementsDto.setMediaPublicUrls(mediaUrls);

            return ResponseEntity.ok(
                    new ApiResponse<>(
                            HttpStatus.CREATED.value(),
                            null,
                            "Announcements Saved Successfully",
                            announcementsDto,
                            null
                    )
            );
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "ANNOUNCEMENTS_CREATION_ERROR",
                            "Error creating announcement " + e.getMessage(),
                            null,
                            null
                    ));
        }
    }

    @PutMapping(path = "/update",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<AnnouncementsDto>> updateAnnouncement(@RequestHeader("Authorization") String jwt ,
                                                                            @RequestPart("dto") AnnouncementsDto announcementsDto,
                                                                            @RequestPart("media") List<MultipartFile> files) {

        String username = jwtProvider.getUsernameFromToken(jwt);

        try{
            Long publicId = announcementsService.update(announcementsDto,username,files);

            List<String> mediaUrls = announcementsService.getMediaUrlsOfAnnouncement(publicId);
            announcementsDto.setMediaPublicUrls(mediaUrls);

            return ResponseEntity.ok(
                    new ApiResponse<>(
                            HttpStatus.OK.value(),
                            null,
                            "Announcements updated Successfully",
                            announcementsDto,
                            null
                    )
            );
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "ANNOUNCEMENTS_UPDATION_ERROR",
                            "Error updating announcement " + e.getMessage(),
                            null,
                            null
                    ));
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<ApiResponse<List<AnnouncementsDto>>> getAllAnnouncements(@RequestHeader("Authorization") String jwt) throws IOException {
        try{
            String username = jwtProvider.getUsernameFromToken(jwt);
            List<AnnouncementsDto> annList = announcementsService.getAllAnnouncements();

            return ResponseEntity.ok(
                    new ApiResponse<>(
                            HttpStatus.OK.value(),
                            null,
                            "Announcements fetched Successfully",
                            annList,
                            null
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "ANNOUNCEMENT_FETCH_ERROR",
                            "Error fetching the announcements " + e.getMessage(),
                            null,
                            null
                    ));
        }
    }

    @DeleteMapping("/delete/{publicId}")
    public ResponseEntity<String> deleteEvent(@RequestHeader("Authorization") String jwt ,@PathVariable("publicId") Long publicId) {
        String username = jwtProvider.getUsernameFromToken(jwt);

        try{
            announcementsService.delete(publicId, username);
            return ResponseEntity.ok("Event deleted Successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("ERROR DELETING EVENT");
        }
    }
}
