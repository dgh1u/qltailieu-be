package com.kltn.mapper;

import com.kltn.dto.entity.AccomodationDto;
import com.kltn.dto.request.accommodation.CreateAccommodationRequest;
import com.kltn.model.Accomodation;
import com.kltn.model.District;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {DistrictMapper.class})
public interface AccommodationMapper {
    AccomodationDto toAccomodationDto(Accomodation accomodation);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "district", source = "idDistrict", qualifiedByName = "mapDistrict")
    @Mapping(target = "kitchen", source = "kitchen")
    @Mapping(target = "security", source = "security")
    Accomodation toAccomodation(CreateAccommodationRequest accomodation);

    @Mapping(target = "kitchen", source = "kitchen")
    @Mapping(target = "security", source = "security")
    Accomodation toAccomodation(AccomodationDto accomodation);

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
