package com.kltn.service;

import com.kltn.model.Accomodation;
import com.kltn.repository.custom.CustomAccomodationQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public interface AccomodationService {
    Page<Accomodation> getAllAccomodation(CustomAccomodationQuery.AccomodationFilterParam param, PageRequest pageRequest);
}
