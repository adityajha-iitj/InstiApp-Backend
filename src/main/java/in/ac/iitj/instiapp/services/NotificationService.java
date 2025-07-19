package in.ac.iitj.instiapp.services;


import in.ac.iitj.instiapp.database.entities.Notification;
import in.ac.iitj.instiapp.payload.Notification.NotificationDto;

import java.util.List;


public interface NotificationService {

    public void saveNotification(Notification notification);

    public void updateNotification(Notification notification);

    public NotificationDto getNotification(Long id);

    public List<Notification> getAllNotifications(String username);

    public void deleteNotification(Long id);

    public int updateReadStatus(Long notificationId);
}

