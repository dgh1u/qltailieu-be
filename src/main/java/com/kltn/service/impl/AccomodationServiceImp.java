package com.kltn.service.impl;

import com.kltn.exception.DataNotFoundException;
import com.kltn.model.Accomodation;
import com.kltn.repository.AccomodationRepository;
import com.kltn.repository.custom.CustomAccomodationQuery;
import com.kltn.service.AccomodationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccomodationServiceImp implements AccomodationService {

    private final AccomodationRepository accomodationRepository;

    @Override
    public Page<Accomodation> getAllAccomodation(CustomAccomodationQuery.AccomodationFilterParam param, PageRequest pageRequest) {
        try {
            Specification<Accomodation> specification = CustomAccomodationQuery.getFilterAccomodation(param);
            return accomodationRepository.findAll(specification, pageRequest);
        }catch (Exception e){
            throw new DataNotFoundException("Không có accomodation nào được tìm thấy! " + e.getMessage());
        }
    }
}
