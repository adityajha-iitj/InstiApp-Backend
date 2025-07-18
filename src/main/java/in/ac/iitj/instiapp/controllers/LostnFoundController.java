package in.ac.iitj.instiapp.controllers;

import in.ac.iitj.instiapp.config.JwtProvider;
import in.ac.iitj.instiapp.database.entities.LostnFound.Locations;
import in.ac.iitj.instiapp.database.entities.LostnFound.LostnFoundType;
import in.ac.iitj.instiapp.payload.LostnFound.LostnFoundDto;
import in.ac.iitj.instiapp.payload.User.UserBaseDto;
import in.ac.iitj.instiapp.payload.common.ApiResponse;
import in.ac.iitj.instiapp.services.LostnFoundService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.stream.Location;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lostnfound")
public class LostnFoundController {

    private final LostnFoundService lostnFoundService;
    private final ValidationUtil validationUtil;
    private final JwtProvider jwtProvider;

    @Autowired
    public LostnFoundController(LostnFoundService lostnFoundService, ValidationUtil validationUtil, JwtProvider jwtProvider) {
        this.lostnFoundService = lostnFoundService;
        this.validationUtil = validationUtil;
        this.jwtProvider = jwtProvider;
    }

    /*--------------------------------------------------------LOCATIONS---------------------------------------------------*/

    @GetMapping("/locations")
    public ResponseEntity<ApiResponse<List<String>>> getListOfLocationsName(Pageable pageable) {
        try {
            List<String> locations = lostnFoundService.getListOfLocationsName(pageable);
            return ResponseEntity.ok(
                    new ApiResponse<>(
                            HttpStatus.OK.value(),
                            null,
                            "Locations fetched successfully",
                            locations,
                            null
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "LOCATIONS_FETCH_ERROR",
                            "Error fetching locations: " + e.getMessage(),
                            null,
                            null
                    ));
        }
    }

    @PostMapping("/locations")
    public ResponseEntity<ApiResponse<Void>> saveLocation(@RequestBody String locationName) {
        try {
            Locations location = new Locations();
            location.setName(locationName);
            lostnFoundService.saveLocation(location);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(
                            HttpStatus.CREATED.value(),
                            null,
                            "Location saved successfully",
                            null,
                            null
                    ));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse<>(
                            HttpStatus.CONFLICT.value(),
                            "LOCATION_CONFLICT",
                            e.getMessage(),
                            null,
                            null
                    ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(
                            HttpStatus.BAD_REQUEST.value(),
                            "LOCATION_SAVE_ERROR",
                            "Error saving location: " + e.getMessage(),
                            null,
                            null
                    ));
        }
    }

    @DeleteMapping("/locations")
    public ResponseEntity<ApiResponse<Void>> deleteLocationByName(@RequestBody String locationName) {
        try {
            Locations location = new Locations();
            location.setName(locationName);
            lostnFoundService.deleteLocationByName(location.getName());
            return ResponseEntity.ok(
                    new ApiResponse<>(
                            HttpStatus.OK.value(),
                            null,
                            "Location deleted successfully",
                            null,
                            null
                    )
            );
        } catch (DataIntegrityViolationException | EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(
                            HttpStatus.NOT_FOUND.value(),
                            "LOCATION_NOT_FOUND",
                            e.getMessage(),
                            null,
                            null
                    ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(
                            HttpStatus.BAD_REQUEST.value(),
                            "LOCATION_DELETE_ERROR",
                            "Error deleting location: " + e.getMessage(),
                            null,
                            null
                    ));
        }
    }

    @PutMapping("/locations")
    public ResponseEntity<ApiResponse<Void>> updateLocation(@RequestParam String oldLocationName, @RequestParam String newLocationName) {
        try {
            lostnFoundService.updateLocation(oldLocationName, newLocationName);
            return ResponseEntity.ok(
                    new ApiResponse<>(
                            HttpStatus.OK.value(),
                            null,
                            "Location updated successfully",
                            null,
                            null
                    )
            );
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse<>(
                            HttpStatus.CONFLICT.value(),
                            "LOCATION_CONFLICT",
                            e.getMessage(),
                            null,
                            null
                    ));
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(
                            HttpStatus.NOT_FOUND.value(),
                            "LOCATION_NOT_FOUND",
                            e.getMessage(),
                            null,
                            null
                    ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(
                            HttpStatus.BAD_REQUEST.value(),
                            "LOCATION_UPDATE_ERROR",
                            "Error updating location: " + e.getMessage(),
                            null,
                            null
                    ));
        }
    }

    /*-----------------------------------------------------LOST AND FOUND-------------------------------------------------*/

    @PostMapping("/")
    public ResponseEntity<ApiResponse<Void>> saveLostAndFound(@RequestHeader("Authorization") String jwt,@Valid @RequestBody LostnFoundDto lostnFoundDto) {
        try {
            String userName = jwtProvider.getUsernameFromToken(jwt);
            UserBaseDto userBaseDto = new UserBaseDto();
            userBaseDto.setUserName(userName);
            if(lostnFoundDto.getType() == LostnFoundType.LOST)
                lostnFoundDto.setOwner(userBaseDto);
            if(lostnFoundDto.getType() == LostnFoundType.FOUND)
                lostnFoundDto.setFinder(userBaseDto);
            lostnFoundService.saveLostAndFound(lostnFoundDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(
                            HttpStatus.CREATED.value(),
                            null,
                            "Lost and Found item saved successfully",
                            null,
                            null
                    ));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse<>(
                            HttpStatus.CONFLICT.value(),
                            "LOSTNFOUND_CONFLICT",
                            e.getMessage(),
                            null,
                            null
                    ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(
                            HttpStatus.BAD_REQUEST.value(),
                            "LOSTNFOUND_SAVE_ERROR",
                            "Error saving lost and found item: " + e.getMessage(),
                            null,
                            null
                    ));
        }
    }

    @PutMapping("/")
    public ResponseEntity<ApiResponse<Void>> updateLostAndFound(
            @RequestHeader("Authorization") String jwt,
            @Valid @RequestBody LostnFoundDto lostnFoundDto
    ) {
        LostnFoundType lostnFoundType = lostnFoundService.findTypeById(lostnFoundDto.getId());
        boolean isOwner = false, isFinder = false;
        try {
            String userName = jwtProvider.getUsernameFromToken(jwt);
            System.out.println("User trying to access : " + userName);
            UserBaseDto userBaseDto = new UserBaseDto();
            userBaseDto.setUserName(userName);

            if (lostnFoundType == LostnFoundType.FOUND) {
                isFinder = lostnFoundService.isFinderById(userName, lostnFoundDto.getId());
                lostnFoundDto.setFinder(userBaseDto);
                lostnFoundDto.setOwner(lostnFoundDto.getOwner());
            }
            if (lostnFoundType == LostnFoundType.LOST) {
                isOwner = lostnFoundService.isOwnerById(userName, lostnFoundDto.getId());
                lostnFoundDto.setOwner(userBaseDto);
                lostnFoundDto.setFinder(lostnFoundDto.getFinder());
            }

        } catch (Exception ex) {
            // If service throws something unexpected during check (e.g., DB error), treat as bad request or internal error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "OWNER_CHECK_ERROR",
                            "Error checking ownership: " + ex.getMessage(),
                            null,
                            null
                    ));
        }
        if (!isOwner && !isFinder) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(
                            HttpStatus.FORBIDDEN.value(),
                            "FORBIDDEN",
                            "You are not authorized to update this item",
                            null,
                            null
                    ));
        }
        try {
            lostnFoundService.updateLostAndFound(lostnFoundDto);
            return ResponseEntity.ok(
                    new ApiResponse<>(
                            HttpStatus.OK.value(),
                            null,
                            "Lost and Found item updated successfully",
                            null,
                            null
                    )
            );
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(
                            HttpStatus.NOT_FOUND.value(),
                            "LOSTNFOUND_NOT_FOUND",
                            e.getMessage(),
                            null,
                            null
                    ));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse<>(
                            HttpStatus.CONFLICT.value(),
                            "LOSTNFOUND_CONFLICT",
                            e.getMessage(),
                            null,
                            null
                    ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(
                            HttpStatus.BAD_REQUEST.value(),
                            "LOSTNFOUND_UPDATE_ERROR",
                            "Error updating lost and found item: " + e.getMessage(),
                            null,
                            null
                    ));
        }
    }

    @DeleteMapping("/{Id}")
    public ResponseEntity<ApiResponse<Void>> deleteLostAndFound(@RequestHeader("Authorization") String jwt, @PathVariable Long Id) {
            String userName = jwtProvider.getUsernameFromToken(jwt);
        boolean isOwner;
        try {
            isOwner = lostnFoundService.isOwnerById(userName, Id);
        } catch (Exception ex) {
            // If service throws something unexpected during check (e.g., DB error), treat as bad request or internal error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "OWNER_CHECK_ERROR",
                            "Error checking ownership: " + ex.getMessage(),
                            null,
                            null
                    ));
        }
        if (!isOwner) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(
                            HttpStatus.FORBIDDEN.value(),
                            "FORBIDDEN",
                            "You are not authorized to delete this item",
                            null,
                            null
                    ));
        }

        try {
            lostnFoundService.deleteLostAndFound(Id);
            return ResponseEntity.ok(
                    new ApiResponse<>(
                            HttpStatus.OK.value(),
                            null,
                            "Lost and Found item deleted successfully",
                            null,
                            null
                    )
            );
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(
                            HttpStatus.NOT_FOUND.value(),
                            "LOSTNFOUND_NOT_FOUND",
                            e.getMessage(),
                            null,
                            null
                    ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(
                            HttpStatus.BAD_REQUEST.value(),
                            "LOSTNFOUND_DELETE_ERROR",
                            "Error deleting lost and found item: " + e.getMessage(),
                            null,
                            null
                    ));
        }
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse<List<LostnFoundDto>>> getLostAndFoundByFilter(
            @RequestParam Optional<Boolean> status,
            @RequestParam(required = true) LostnFoundType type,
            @RequestParam Optional<String> owner,
            @RequestParam Optional<String> finder,
            @RequestParam Optional<String> landmark,
            Pageable pageable) {
        try {
            List<LostnFoundDto> items = lostnFoundService.getLostAndFoundByFilter(type, status, owner, finder, landmark, pageable);
            return ResponseEntity.ok(
                    new ApiResponse<>(
                            HttpStatus.OK.value(),
                            null,
                            "Lost and Found items fetched successfully",
                            items,
                            null
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "LOSTNFOUND_FETCH_ERROR",
                            "Error fetching lost and found items: " + e.getMessage(),
                            null,
                            null
                    ));
        }
    }


    @PostMapping("/image")
    public ResponseEntity<ApiResponse<String>> uploadLostnFoundImage(
            @RequestParam("file") MultipartFile file
    ) {
        try {
            String imageUrl = lostnFoundService.uploadLostnFoundImage(file);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(
                            HttpStatus.CREATED.value(),
                            null,
                            "Image uploaded successfully",
                            imageUrl,
                            null
                    ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "IMAGE_UPLOAD_ERROR",
                            "Error uploading image: " + e.getMessage(),
                            null,
                            null
                    ));
        }
    }


    @GetMapping("/{Id}/image")
    public ResponseEntity<ApiResponse<String>> getLostnFoundImage(
            @PathVariable Long Id
    ) {
        try {
            String imageUrl = lostnFoundService.getLostnFoundImageUrl(Id);
            return ResponseEntity.ok(
                    new ApiResponse<>(
                            HttpStatus.OK.value(),
                            null,
                            "Image URL fetched successfully",
                            imageUrl,
                            null
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(
                            HttpStatus.NOT_FOUND.value(),
                            "IMAGE_NOT_FOUND",
                            "Image not found: " + e.getMessage(),
                            null,
                            null
                    ));
        }
    }
}
