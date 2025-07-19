package in.ac.iitj.instiapp.payload.Notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequestAll {
    private String title;
    private String body;

//    // For optional countdown
//    private Integer countdownDays;
//    private Integer countdownHours;
//    private Integer countdownMinutes;
//    private Integer countdownSeconds;
}
