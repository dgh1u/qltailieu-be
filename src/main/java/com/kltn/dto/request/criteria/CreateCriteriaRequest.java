package com.kltn.dto.request.criteria;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateCriteriaRequest {
    // Loại hình nhà trọ (bắt buộc)
    @NotNull(message = "Thông tin về loại hình là bắt buộc")
    private String motel;

    // Loại cơ sở lưu trú thứ hai
    private String secondMotel;

    // Chuyên ngành/lĩnh vực
    private String major;
}
