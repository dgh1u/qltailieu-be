package com.kltn.repository;

import com.kltn.model.Accomodation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AccomodationRepository extends JpaRepository<Accomodation, Long> , JpaSpecificationExecutor<Accomodation> {

}
