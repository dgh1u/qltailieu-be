package com.kltn.mapper;

import com.kltn.dto.entity.CriteriaDto;
import com.kltn.dto.request.criteria.CreateCriteriaRequest;
import com.kltn.model.Criteria;
import com.kltn.model.District;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {DistrictMapper.class})
public interface CriteriaMapper {
    CriteriaDto toCriteriaDto(Criteria criteria);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "district", source = "idDistrict", qualifiedByName = "mapDistrict")
    @Mapping(target = "kitchen", source = "kitchen")
    @Mapping(target = "security", source = "security")
    Criteria toCriteria(CreateCriteriaRequest criteria);

    @Mapping(target = "kitchen", source = "kitchen")
    @Mapping(target = "security", source = "security")
    Criteria toCriteria(CriteriaDto criteria);

    @Named("mapDistrict")
    default District mapDistrict(Long idDistrict) {
        if (idDistrict == null) {
            return null;
        }
        District district = new District();
        district.setId(idDistrict);
        return district;
    }
}
