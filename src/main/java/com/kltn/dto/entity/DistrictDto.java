package com.kltn.dto.entity;

import com.kltn.model.District;
import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link District}
 */
@Data
public class DistrictDto implements Serializable {
    private Long id;

    private String name;
}