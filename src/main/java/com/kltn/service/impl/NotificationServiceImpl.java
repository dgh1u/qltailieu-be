package com.kltn.service.impl;

import com.kltn.dto.entity.NotificationDto;
import com.kltn.mapper.NotificationMapper;
import com.kltn.model.Notification;
import com.kltn.repository.CriteriaRepository;
import com.kltn.repository.NotificationRepository;
import com.kltn.repository.UserRepository;
import com.kltn.service.ImageService;
import com.kltn.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    private final CriteriaRepository criteriaRepository;

    private final UserRepository userRepository;

    private final ImageService imageService;

    NotificationMapper notificationMapper;

    @Override
    public void createNotification(Notification notification) {

    }

    @Override
    public Page<NotificationDto> getNotificationsByEmail(String email, int page, boolean screen) {
        return null;
    }

    @Override
    public Page<NotificationDto> getNotificationsByEmailAndCriteria(String email, Long criteria, int page) {
        return null;
    }

    @Override
    public NotificationDto seenNotification(Long id) {
        return null;
    }
}
