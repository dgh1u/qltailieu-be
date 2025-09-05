package com.kltn.mapper;

import com.kltn.dto.entity.CriteriaDto;
import com.kltn.dto.request.criteria.CreateCriteriaRequest;
import com.kltn.model.Criteria;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CriteriaMapper {
    CriteriaDto toCriteriaDto(Criteria criteria);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "post", ignore = true)
    Criteria toCriteria(CreateCriteriaRequest criteria);

    @Mapping(target = "post", ignore = true)
    Criteria toCriteria(CriteriaDto criteria);
}
