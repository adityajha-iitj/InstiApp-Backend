package in.ac.iitj.instiapp.controllers;

import com.cloudinary.Api;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import in.ac.iitj.instiapp.config.JwtProvider;
import in.ac.iitj.instiapp.database.entities.Media.Media;
import in.ac.iitj.instiapp.payload.Auth.SignupDto;
import in.ac.iitj.instiapp.payload.Auth.UpdateUserDto;
import in.ac.iitj.instiapp.payload.User.UserBaseDto;
import in.ac.iitj.instiapp.payload.User.UserDetailedDto;
import in.ac.iitj.instiapp.payload.common.ApiResponse;
import in.ac.iitj.instiapp.services.BucketService;
import in.ac.iitj.instiapp.services.UserService;
import io.jsonwebtoken.io.IOException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import in.ac.iitj.instiapp.database.entities.User.User;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final JwtProvider jwtProvider;

    private BucketService bucketService;

    // Read your bucket name from application.properties
    @Value("${aws.s3.bucket.name}")
    private String bucketName;


    @Autowired
    public UserController(UserService userService, JwtProvider jwtProvider, BucketService bucketService) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
        this.bucketService = bucketService;
    }

    @GetMapping("/getUserLimited")
    public ResponseEntity<UserBaseDto> getUser(@RequestParam String username) {
        return ResponseEntity.ok(userService.getUserLimited(username));
    }

    @GetMapping("/getUserDetailed")
    public ResponseEntity<UserDetailedDto> getUserDetailed(@RequestHeader("Authorization") String jwt) {
        String userName = jwtProvider.getUsernameFromToken(jwt);
        return ResponseEntity.ok(userService.getUserDetailed(userName));
    }

    @PutMapping(
            path = "/updateUserProfile",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Map<String,Object>> updateUserProfile(
            @RequestHeader("Authorization") String jwt,
            @RequestPart("user") UpdateUserDto updateUserDto,
            @RequestPart(value = "avatar", required = false) MultipartFile avatarFile
    ) throws IOException, java.io.IOException {

        // 1. Load the existing user details
        String username = jwtProvider.getUsernameFromToken(jwt);
        System.out.println("The user name is this boy: " + username);
        UserDetailedDto userDetailedDto = userService.getUserDetailed(username);

        String s3Url = "";
        // 2. If a new avatar file was provided, upload it to S3
        if (avatarFile != null && !avatarFile.isEmpty()) {

            // 2.a. Generate a unique key under an "avatars/" folder
            String extension =
                    avatarFile.getOriginalFilename()
                            .substring(avatarFile.getOriginalFilename().lastIndexOf('.'));
            String objectKey = "avatars/" + username + "/"
                    + UUID.randomUUID() + extension;

            // 2.b. Save to a temp file
            File temp = File.createTempFile("avatar-", extension);
            avatarFile.transferTo(temp);

            // 2.c. Upload & get URL
            bucketService.uploadFile(bucketName, objectKey, temp.getAbsolutePath());
            s3Url = bucketService.getFileUrl(bucketName, objectKey);

            // 2.d. Clean up the temp file
            temp.delete();

            // 2.e. Set the S3 URL on your user DTO
        }

        // 3. Update any other fields
        userDetailedDto.setPhoneNumber(updateUserDto.getPhoneNumber());
        userDetailedDto.setAvatarUrl(s3Url);
        // 4. Persist
        Long status = userService.updateUserDetails(userDetailedDto);

        // 5. Build response
        Map<String,Object> resp = new HashMap<>();
        if (status != 0L) {
            resp.put("Message", "User Profile Updated successfully");
            resp.put("Status", "200");
            return ResponseEntity.ok(resp);
        } else {
            resp.put("Message", "User Profile Update failed");
            resp.put("Status", "404");
            return ResponseEntity.status(404).body(resp);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<UserBaseDto>> deleteUser(@RequestHeader("Authorization") String jwt){
        String userName = jwtProvider.getUsernameFromToken(jwt);

        try{
            UserBaseDto dto = userService.deleteUser(userName);
            return ResponseEntity.ok(
                    new ApiResponse<>(
                            HttpStatus.OK.value(),
                            null,
                            "User Deleted successfully",
                            dto,
                            null
                    )
            );

        } catch (Exception e){
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "USER_DELETION_ERROR",
                            "Error deleting User: " + e.getMessage(),
                            null,
                            null
                    ));
        }
    }

}

