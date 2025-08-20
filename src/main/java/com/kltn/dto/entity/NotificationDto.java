package com.kltn.dto.entity;

import com.kltn.model.enums.NotificationName;
import lombok.Data;

import java.time.Instant;

@Data
public class NotificationDto {
    private Long id;

    private PostDto postDTO;

    private boolean seen;

    private Instant createAt;

    private NotificationName notificationName;
}
