package com.kltn.dto.response.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApprovePostResponse {
    private Long postId; // ID của tài liệu
    private String message; // Nội dung thông báo
    private boolean approved;
}
