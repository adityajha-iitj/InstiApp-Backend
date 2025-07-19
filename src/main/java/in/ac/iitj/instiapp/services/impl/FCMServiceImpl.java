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



    public List<DeviceToken> sendNotificationToAll(NotificationRequestAll req, String fileUrl) throws FirebaseMessagingException {
        List<DeviceToken> allTokens = deviceTokenRepository.findAll();

        for (DeviceToken tokenEntity : allTokens) {
            // Assume tokenEntity.getTokens() returns a Collection<String> of all device tokens for this user
            for (String token : tokenEntity.getToken()) {
                com.google.firebase.messaging.Notification.Builder notificationBuilder =
                        com.google.firebase.messaging.Notification.builder()
                                .setTitle(req.getTitle())
                                .setBody(req.getBody());
                if (fileUrl != null && !fileUrl.isEmpty()) {
                    notificationBuilder.setImage(fileUrl);
                }

                Message message = Message.builder()
                        .setToken(token)
                        .setNotification(notificationBuilder.build())
                        .putData("fileUrl", fileUrl == null ? "" : fileUrl)
                        .build();

                FirebaseMessaging.getInstance().send(message);
            }
        }
        return allTokens;
    }


}


