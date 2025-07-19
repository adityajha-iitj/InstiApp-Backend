package in.ac.iitj.instiapp.mappers.Notification;

import in.ac.iitj.instiapp.database.entities.Notification;
import in.ac.iitj.instiapp.payload.Notification.NotificationDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class NotificationMapper {

    /**
     * Convert a NotificationDto into a Notification entity.
     * If dto.createdAt is null, sets createdAt to now().
     */
    public Notification toEntity(NotificationDto dto) {
        Notification entity = new Notification();
        entity.setTitle(dto.getTitle());
        entity.setBody(dto.getBody());
        entity.setUsername(dto.getUsername());
        entity.setRead(dto.isRead());
        entity.setTopic(dto.getTopic());
        entity.setCreatedAt(dto.getCreatedAt() != null
                ? dto.getCreatedAt()
                : LocalDateTime.now());
        return entity;
    }

    /**
     * Convert a Notification entity into a NotificationDto.
     */
    public NotificationDto toDto(Notification entity) {
        return new NotificationDto(
                entity.getTitle(),
                entity.getBody(),
                entity.getUsername(),
                entity.isRead(),
                entity.getCreatedAt(),
                entity.getTopic()
        );
    }
}