package in.ac.iitj.instiapp.services.impl;

import in.ac.iitj.instiapp.Repository.NotificationRepository;
import in.ac.iitj.instiapp.database.entities.Notification;
import in.ac.iitj.instiapp.mappers.Notification.NotificationMapper;
import in.ac.iitj.instiapp.payload.Notification.NotificationDto;
import in.ac.iitj.instiapp.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository, NotificationMapper notificationMapper) {
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
    }


    public void saveNotification(Notification notification){
        Notification notification1 = notificationRepository.save(notification);
        notificationMapper.toDto(notification1);
    }

    public void updateNotification(Notification notification){
        Notification notification1 = notificationRepository.save(notification);
        notificationMapper.toDto(notification1);
    }

    public NotificationDto getNotification(Long id){
        return notificationMapper.toDto(notificationRepository.findById(id).orElse(null));
    }

    public List<Notification> getAllNotifications(String username) {
        return notificationRepository.getAllNotifications(username);
    }

    @Override
    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }

    public int updateReadStatus(Long notificationId){
        return notificationRepository.updateReadStatus(notificationId);
    }

}

