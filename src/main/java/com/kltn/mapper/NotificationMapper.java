package com.kltn.mapper;

import com.kltn.dto.entity.NotificationDto;
import com.kltn.model.Notification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    NotificationDto toNotificationDto(Notification notification);
    Notification toNotification(NotificationDto notificationDto);
}
