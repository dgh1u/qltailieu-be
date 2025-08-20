package com.kltn.dto.response.post;

import com.kltn.dto.entity.AccomodationDto;
import com.kltn.dto.entity.UserDto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdatePostResponse {
    private long id;
    private String title;
    private String content;
    private boolean approved;
    private boolean notApproved;
    private LocalDateTime createAt;
    private LocalDateTime lastUpdate;
    private boolean del;
    private AccomodationDto accomodationDTO;
    private UserDto userDTO;
    private String type;
}

