package com.kltn.dto.response.post;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeletePostResponse {
    private Long postId;
    private String message;
    private boolean deleted;
}