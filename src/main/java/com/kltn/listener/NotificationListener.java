package com.kltn.listener;

import com.kltn.model.enums.NotificationName;
import com.kltn.event.NotificationEvent;
import com.kltn.model.Notification;
import com.kltn.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener implements ApplicationListener<NotificationEvent> {

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public void onApplicationEvent(NotificationEvent event) {
        // Tạo một thông báo mới từ event
        Notification notification = new Notification();
        notification.setPost(event.getPost());
        notification.setUser(event.getUser());
        notification.setSeen(false);
        notification.setCreateAt(java.time.Instant.now());
        notification.setNotificationName(NotificationName.valueOf(event.getMessage()));

        // Lưu thông báo vào cơ sở dữ liệu
        notificationRepository.save(notification);
    }
}
