package com.kltn.service;

import com.kltn.dto.entity.NotificationDto;
import com.kltn.model.Notification;
import org.springframework.data.domain.Page;

public interface NotificationService {
    void createNotification(Notification notification);

    Page<NotificationDto> getNotificationsByEmail(String email, int page, boolean screen);

    Page<NotificationDto> getNotificationsByEmailAndCriteria(String email, Long criteria, int page);

    NotificationDto seenNotification(Long id);
}
