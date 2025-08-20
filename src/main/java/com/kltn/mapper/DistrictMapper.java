package com.kltn.mapper;

import com.kltn.dto.entity.DistrictDto;
import com.kltn.model.District;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DistrictMapper {

    // Ánh xạ từ idDistrict thành District
    District toDistrict(Long idDistrict);

    DistrictDto toDistrictDto(District district);
}
