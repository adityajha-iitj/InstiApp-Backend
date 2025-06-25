package in.ac.iitj.instiapp.controllers;

import in.ac.iitj.instiapp.database.entities.LostnFound.Locations;
import in.ac.iitj.instiapp.payload.LostnFound.LostnFoundDto;
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

import javax.xml.stream.Location;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lostnfound")
public class LostnFoundController {

    private final LostnFoundService lostnFoundService;
    private final ValidationUtil validationUtil;

    @Autowired
    public LostnFoundController(LostnFoundService lostnFoundService, ValidationUtil validationUtil) {
        this.lostnFoundService = lostnFoundService;
        this.validationUtil = validationUtil;
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
    public ResponseEntity<ApiResponse<Void>> saveLocation(@RequestBody Locations location) {
        try {
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
    public ResponseEntity<ApiResponse<Void>> deleteLocationByName(@RequestBody Locations locationName) {
        try {
            lostnFoundService.deleteLocationByName(locationName.getName());
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
    public ResponseEntity<ApiResponse<Void>> updateLocation(@RequestParam String oldLocationName, @RequestBody Locations location) {
        try {
            lostnFoundService.updateLocation(oldLocationName, location);
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
    public ResponseEntity<ApiResponse<Void>> saveLostAndFound(@Valid @RequestBody LostnFoundDto lostnFoundDto) {
        try {
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
    public ResponseEntity<ApiResponse<Void>> updateLostAndFound(@Valid @RequestBody LostnFoundDto lostnFoundDto) {
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

    @DeleteMapping("/{publicId}")
    public ResponseEntity<ApiResponse<Void>> deleteLostAndFound(@PathVariable String publicId) {
        try {
            lostnFoundService.deleteLostAndFound(publicId);
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
            @RequestParam Optional<String> owner,
            @RequestParam Optional<String> finder,
            @RequestParam Optional<String> landmark,
            Pageable pageable) {
        try {
            List<LostnFoundDto> items = lostnFoundService.getLostAndFoundByFilter(status, owner, finder, landmark, pageable);
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
}
