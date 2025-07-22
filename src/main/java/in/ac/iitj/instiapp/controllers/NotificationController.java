package in.ac.iitj.instiapp.controllers;

import com.google.firebase.messaging.FirebaseMessagingException;
import in.ac.iitj.instiapp.config.JwtProvider;
import in.ac.iitj.instiapp.database.entities.DeviceToken;
import in.ac.iitj.instiapp.database.entities.Notification;
import in.ac.iitj.instiapp.mappers.Notification.NotificationMapper;
import in.ac.iitj.instiapp.payload.Notification.DeviceTokenRequest;
import in.ac.iitj.instiapp.payload.Notification.NotificationRequest;
import in.ac.iitj.instiapp.payload.Notification.NotificationRequestAll;
import in.ac.iitj.instiapp.payload.Notification.NotificationResponse;
import in.ac.iitj.instiapp.services.BucketService;
import in.ac.iitj.instiapp.services.NotificationService;
import in.ac.iitj.instiapp.services.UserService;
import in.ac.iitj.instiapp.services.impl.DeviceTokenServiceImpl;
import in.ac.iitj.instiapp.services.impl.FCMServiceImpl;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    private final JwtProvider jwtProvider;
    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    private final FCMServiceImpl fcmService;
    private final DeviceTokenServiceImpl deviceTokenService;
    private final NotificationService notificationService;
    private final UserService userService;
    private final NotificationMapper notificationMapper;
    private final BucketService bucketService;

    @Autowired
    public NotificationController(FCMServiceImpl fcmService, DeviceTokenServiceImpl deviceTokenService,
                                  NotificationService notificationService, UserService userService,
                                  NotificationMapper notificationMapper, BucketService bucketService, JwtProvider jwtProvider) {
        this.fcmService = fcmService;
        this.deviceTokenService = deviceTokenService;
        this.notificationService = notificationService;
        this.userService = userService;
        this.notificationMapper = notificationMapper;
        this.bucketService = bucketService;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping(value = "/single", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<NotificationResponse> sendNotification(@RequestPart(value = "file", required = false) MultipartFile file,
                                                                 @RequestPart("request") NotificationRequest request,
                                                                 @RequestHeader("Authorization") String jwt) {
        try {
            String username = jwtProvider.getUsernameFromToken(jwt);
            DeviceToken deviceToken = deviceTokenService.getAllDeviceTokens(username);
            String fileUrl = "";

            // --- 1. If a file was sent, upload it and set mediaLink ---
            if (file != null && !file.isEmpty()) {
                try {
                    // (a) convert to a temp File
                    File converted = convertMultiPartToFile(file);

                    // (b) give it a unique name (timestamp + original name)
                    String objectName = System.currentTimeMillis()
                            + "_" + file.getOriginalFilename();

                    // (c) upload to S3
                    bucketService.uploadFile(bucketName, objectName, converted.getAbsolutePath());

                    // (d) delete the temp file
                    converted.delete();

                    // (e) retrieve the public URL
                    fileUrl = bucketService.getFileUrl(bucketName, objectName);

                } catch (Exception e) {
                    NotificationResponse notificationResponse = new NotificationResponse();
                    int statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
                    String errorMessage = e.getMessage();
                    return ResponseEntity.status(statusCode).body(notificationResponse);

                }
            }
            else
                System.out.println("No file detected!!!");





            Notification notification = new Notification();
            notification.setTitle(request.getTitle());
            notification.setBody(request.getBody());
            notification.setRead(false);
            notification.setTopic(request.getTopic());
            notification.setUsername(username);
            LocalDateTime now = LocalDateTime.now();
            notification.setCreatedAt(now);
            notification.setImageUrl(fileUrl);

            notificationService.saveNotification(notification);

            String response = fcmService.sendPushNotification(request,fileUrl, username);

            // Return success response
            return ResponseEntity.ok(new NotificationResponse(HttpStatus.OK.value(),
                    "Notification sent, ID: " + response));
        } catch (FirebaseMessagingException ex) {
            // Handle error and return failure response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new NotificationResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
        }
    }

    /** Utility: convert Spring MultipartFile → java.io.File */
    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = File.createTempFile("upload-", "-" + file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(file.getBytes());
        }
        return convFile;
    }

    @PostMapping("/register-token")
    public ResponseEntity<Map<String, Object>> registerToken(@RequestBody DeviceTokenRequest request,
                                                             @RequestHeader("Authorization") String jwt) {
        // 1. Try to load an existing DeviceToken by userId (username)
        String username = jwtProvider.getUsernameFromToken(jwt);
        DeviceToken deviceToken = deviceTokenService.getAllDeviceTokens(username);
        if (deviceToken == null) {
            deviceToken = new DeviceToken();
            deviceToken.setUsername(username);
            deviceToken.setToken(new HashSet<>());
        }

        // 2. Only add & save if this token isn’t already registered
        String newToken = request.getToken();
        if (!deviceToken.getToken().contains(newToken)) {
            deviceToken.getToken().add(newToken);
            deviceToken = deviceTokenService.save(deviceToken);
        }

        // 3. Build response
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Token successfully " +
                (deviceToken.getToken().contains(newToken) ? "registered" : "already present"));
        response.put("deviceToken", deviceToken);
        response.put("id", deviceToken.getId());
        return ResponseEntity.ok(response);
    }


    @PostMapping(value ="/notifyAll", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> sendToAll(@RequestPart(value = "file", required = false)MultipartFile file,
                                                         @RequestPart("request") NotificationRequestAll request) {
        Map<String, Object> response = new HashMap<>();

        String fileUrl = "";

        // --- 1. If a file was sent, upload it and set mediaLink ---
        if (file != null && !file.isEmpty()) {
            try {
                // (a) convert to a temp File
                File converted = convertMultiPartToFile(file);

                // (b) give it a unique name (timestamp + original name)
                String objectName = System.currentTimeMillis()
                        + "_" + file.getOriginalFilename();

                // (c) upload to S3
                bucketService.uploadFile(bucketName, objectName, converted.getAbsolutePath());

                // (d) delete the temp file
                converted.delete();

                // (e) retrieve the public URL
                fileUrl = bucketService.getFileUrl(bucketName, objectName);

            } catch (Exception e) {
                NotificationResponse notificationResponse = new NotificationResponse();
                response.put("Status",HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.put("message",e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

            }
        }

        // 1️⃣ send to all and get back every DeviceToken entity
        List<DeviceToken> allDeviceTokens = fcmService.sendNotificationToAll(request, fileUrl);

        // prepare a flat list of (username, token) pairs
        List<DeviceTokenRequest> deviceTokens = new ArrayList<>();
        List<String> users = new ArrayList<>();

        // 2️⃣ persist one Notification per actual token
        for (DeviceToken entity : allDeviceTokens) {
            String username = entity.getUsername();

            for (String token : entity.getToken()) {
                // persist a Notification per device token
                Notification notification = new Notification();
                notification.setTitle(request.getTitle());
                notification.setBody(request.getBody());
                notification.setRead(false);
                notification.setTopic("All");
                notification.setUsername(username);
                notification.setCreatedAt(LocalDateTime.now());
                notification.setImageUrl(fileUrl);

                notificationService.saveNotification(notification);

                // collect for response
                deviceTokens.add(new DeviceTokenRequest(token));
                users.add(username);
            }
        }

        response.put("message", "Notification sent to " + deviceTokens.size() + " devices");
        response.put("deviceTokens", deviceTokens);
        response.put("users", users);
        return ResponseEntity.ok(response);

    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Notification>> getAllNotifications(@RequestHeader("Authorization") String jwt) throws ExecutionControl.UserException {
        String username = jwtProvider.getUsernameFromToken(jwt);

        return ResponseEntity.ok(notificationService.getAllNotifications(username));
    }

    @PutMapping("/update/{notificationId}")
    public ResponseEntity<NotificationResponse> updateNotification(@PathVariable("notificationId") Long Id, @RequestBody NotificationRequestAll request) {
        NotificationResponse notificationResponse;

        Notification entity = notificationMapper.toEntity(notificationService.getNotification(Id));
        entity.setTitle(request.getTitle());
        entity.setBody(request.getBody());
        entity.setRead(false);
        entity.setId(Id);
        notificationService.updateNotification(entity);

        notificationResponse = new NotificationResponse(HttpStatus.OK.value(), "Notification updated successfully");
        return ResponseEntity.ok(notificationResponse);
    }

    @PutMapping("/updateRead")
    public ResponseEntity<Map<String,Object>> updateReadStatus(@RequestBody Long notificationId){
        Map<String, Object> response = new HashMap<>();
        int status = notificationService.updateReadStatus(notificationId);

        if(status == 0){
            response.put("Message: ", "Notification ID: " + notificationId + " not found");
        }
        else{
            response.put("Message: ", "Notification saved as read");
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{notificationId}")
    public ResponseEntity<NotificationResponse> deleteNotification(@PathVariable("notificationId") Long notificationId) {
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.ok(new NotificationResponse(HttpStatus.OK.value(), "Notification deleted successfully"));
    }
}


