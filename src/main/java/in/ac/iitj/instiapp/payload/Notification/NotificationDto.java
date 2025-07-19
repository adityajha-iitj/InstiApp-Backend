package in.ac.iitj.instiapp.payload.Notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;


@Value
@Getter
@Setter
@AllArgsConstructor
public class NotificationDto implements Serializable {
    String title;
    String body;
    String username;
    boolean read;
    LocalDateTime createdAt;
    String topic;
}