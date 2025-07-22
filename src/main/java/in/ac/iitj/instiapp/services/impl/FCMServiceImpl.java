package in.ac.iitj.instiapp.services.impl;

import com.google.firebase.messaging.*;
import in.ac.iitj.instiapp.Repository.DeviceTokenRepository;
import in.ac.iitj.instiapp.payload.Notification.NotificationRequest;
import in.ac.iitj.instiapp.payload.Notification.NotificationRequestAll;
import org.springframework.stereotype.Service;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.stereotype.Service;
import com.google.firebase.messaging.FirebaseMessaging;
import in.ac.iitj.instiapp.database.entities.DeviceToken;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class FCMServiceImpl {

    private final DeviceTokenRepository deviceTokenRepository;

    public FCMServiceImpl(DeviceTokenRepository deviceTokenRepository) {
        this.deviceTokenRepository = deviceTokenRepository;
    }


    /**
     * Send a push notification to a specific device token.
     * @param request the notification request DTO
     * @return the message ID string if sent successfully
     * @throws FirebaseMessagingException
     */
    // In sendPushNotification(...)
    public String sendPushNotification(NotificationRequest request, String fileUrl, String username) throws FirebaseMessagingException {
        DeviceToken dtEntity = deviceTokenRepository.getAllDeviceTokens(username);
        if (dtEntity == null || dtEntity.getToken() == null || dtEntity.getToken().isEmpty()) {
            return "No registered tokens found for user: " + username;
        }
        Set<String> allTokens = dtEntity.getToken();

        int successCount = 0;
        int failureCount = 0;

        for (String token : allTokens) {
            // --- START OF CHANGES ---
            // We will now build a DATA-ONLY message.
            // The .setNotification() method is removed entirely.
            Message msg = Message.builder()
                    .setToken(token)
                    .putData("title", request.getTitle())       // Move title to data
                    .putData("body", request.getBody())         // Move body to data
                    .putData("image", fileUrl == null ? "" : fileUrl) // Keep image/fileUrl in data
                    .build();
            // --- END OF CHANGES ---

            try {
                FirebaseMessaging.getInstance().send(msg);
                successCount++;
            } catch (FirebaseMessagingException ex) {
                System.out.println("Error sending FCM message to token: " + token);
                System.out.println("Error Message: " + ex.getMessage());
                failureCount++;
            }
        }

        return String.format(
                "Attempted %d sends: %d succeeded, %d failed",
                allTokens.size(), successCount, failureCount
        );
    }



    public List<DeviceToken> sendNotificationToAll(NotificationRequestAll req, String fileUrl) {

        // --- ACCURATE MODIFICATION ---
        // 1. Get the original list of entities first, as we need it for the return statement.
        List<DeviceToken> allTokenEntities = deviceTokenRepository.findAll();

        // 2. Get a flat list of every single device token string from the entities.
        List<String> allDeviceTokens = allTokenEntities
                .stream()
                .flatMap(deviceToken -> deviceToken.getToken().stream())
                .collect(Collectors.toList()); // Use .collect(Collectors.toList()) for broader Java compatibility

        if (allDeviceTokens.isEmpty()) {
            System.out.println("No registered device tokens found in the system to broadcast to.");
            return allTokenEntities; // Return the empty list
        }

        // Initialize success and failure counters for logging (optional but good practice)
        int successCount = 0;
        int failureCount = 0;

        // 3. Loop through every token and attempt to send.
        for (String token : allDeviceTokens) {
            // Build a DATA-ONLY message.
            Message message = Message.builder()
                    .setToken(token)
                    .putData("title", req.getTitle())
                    .putData("body", req.getBody())
                    .putData("image", fileUrl == null ? "" : fileUrl)
                    .build();

            // Use a try-catch block inside the loop for robust error handling.
            try {
                FirebaseMessaging.getInstance().send(message);
                successCount++;
            } catch (FirebaseMessagingException e) {
                System.err.println("Failed to send broadcast to token: " + token + ". Error: " + e.getMessage());
                failureCount++;
            }
        }

        // Log the summary of the operation.
        System.out.printf(
                "Broadcast attempted for %d tokens: %d succeeded, %d failed.%n",
                allDeviceTokens.size(), successCount, failureCount
        );

        // 4. Return the original list of entities as requested.
        return allTokenEntities;
    }


}


