package in.ac.iitj.instiapp.controllers;

import in.ac.iitj.instiapp.config.JwtProvider;
import in.ac.iitj.instiapp.database.entities.Grievance;
import in.ac.iitj.instiapp.mappers.GrievanceDtoMapper;
import in.ac.iitj.instiapp.payload.GrievanceDto;
import in.ac.iitj.instiapp.payload.User.UserBaseDto;
import in.ac.iitj.instiapp.payload.common.ApiResponse;
import in.ac.iitj.instiapp.services.GrievanceService;
import in.ac.iitj.instiapp.services.OrganisationRoleService;
import in.ac.iitj.instiapp.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authorization.method.AuthorizeReturnObject;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/grievance")
@Slf4j
public class GrievanceController {

    public final GrievanceService grievanceService;
    private final JwtProvider jwtProvider;
    private final GrievanceDtoMapper grievanceDtoMapper;
    private final UserService userService;
    private final OrganisationRoleService organisationRoleService;

    public GrievanceController(
            GrievanceService grievanceService, JwtProvider jwtProvider, GrievanceDtoMapper grievanceDtoMapper, UserService userService,
            OrganisationRoleService organisationRoleService) {
        this.grievanceService = grievanceService;
        this.jwtProvider = jwtProvider;
        this.grievanceDtoMapper = grievanceDtoMapper;
        this.userService = userService;
        this.organisationRoleService = organisationRoleService;
    }

    @PostMapping("/create")
    public ApiResponse<GrievanceDto> createGrievance(
            @RequestHeader("Authorization") String jwt,
            @RequestBody GrievanceDto grievanceDto
    ) {
        try {
            // 1. Populate the DTO
            String userName = jwtProvider.getUsernameFromToken(jwt);
            log.debug("Looking up userName=[{}] (length={})", userName, userName.length());
            UserBaseDto user = userService.getUserLimited(userName);
            grievanceDto.setUserFrom(user);
            grievanceDto.setPublicId(null);  // let service generate it
            // 2. Persist
            String status = grievanceService.save(grievanceDto);

            // 3a. Success branch
            if (!Objects.equals(status, "")) {
                // (Optionally) set any returned identifiers back on the DTO:
                grievanceDto.setPublicId(status);
                // grievanceDto.setPublicId(...) if you have it from service

                return new ApiResponse<>(
                        HttpStatus.CREATED.value(),
                        null,
                        "Grievance created successfully",
                        grievanceDto,
                        null
                );
            }

            // 3b. Failure branch — service returned an error code
            return ApiResponse.error(
                    "Failed to create grievance",
                    "GRIEVANCE_CREATION_FAILED",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null
            );

        } catch (Exception ex) {
            // 4. Unexpected exception
            return ApiResponse.error(
                    ex,
                    "GRIEVANCE_CREATE_EXCEPTION",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    null
            );
        }
    }

    @GetMapping("/{publicId}")
    public ApiResponse<GrievanceDto> getGrievance(@PathVariable String publicId) {
        try{
            GrievanceDto grievanceDto = grievanceService.getGrievance(publicId);

            return new ApiResponse<>(
                    HttpStatus.OK.value(),
                    null,
                    "Grievance fetched successfully",
                    grievanceDto,
                    null
            );

        } catch (Exception ex){
            return ApiResponse.error(
                    ex,
                    "GRIEVANCE_GET_EXCEPTION",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    null
            );
        }
    }

    @PutMapping("/update")
    public ApiResponse<GrievanceDto> updateGrievance(@RequestHeader("Authorization") String jwt,
                                                     @RequestBody GrievanceDto grievanceDto) {

        String userName = jwtProvider.getUsernameFromToken(jwt);
        GrievanceDto grievance = grievanceService.getGrievance(grievanceDto.getPublicId());
        if(!grievance.getUserFrom().getUserName().equals(userName)) {
            return ApiResponse.error(
                    "The Grievance you want to update is not yours",
                    "GRIEVANCE_NOT_YOURS",
                    HttpStatus.FORBIDDEN,
                    null
            );
        }
        try{
            UserBaseDto user = userService.getUserLimited(userName);
            grievanceDto.setUserFrom(user);
            grievanceService.updateGrievance(grievanceDto.getPublicId(),grievanceDto);


            return new ApiResponse<>(
                    HttpStatus.OK.value(),
                    null,
                    "Grievance updated successfully",
                    grievanceDto,
                    null
            );
        }
        catch(Exception ex){
            return ApiResponse.error(
                    ex,
                    "GRIEVANCE_UPDATION_EXCEPTION",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    null
            );
        }

    }

    @DeleteMapping("/delete/{publicId}")
    public ApiResponse<GrievanceDto> deleteGrievance(@RequestHeader("Authorization") String jwt, @PathVariable String publicId) {
        String userName = jwtProvider.getUsernameFromToken(jwt);

        boolean doesOwn = grievanceService.doesOwn(publicId,userName);

        if(!doesOwn) {
            return ApiResponse.error(
                    "This grievance is not yours to delete",
                    "GRIEVANCE_ACCESS_FORBIDDEN_EXCEPTION",
                    HttpStatus.FORBIDDEN,
                    null
            );
        }

        try{
            grievanceService.deleteGrievance(publicId);

            return new ApiResponse<>(
                    HttpStatus.OK.value(),
                    null,
                    "Grievance Deleted Successfully",
                    null,
                    null
            );
        } catch (Exception ex){
            return ApiResponse.error(
                    ex,
                    "GRIEVANCE_DELETION_EXCEPTION",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    null
            );
        }




    }


}
