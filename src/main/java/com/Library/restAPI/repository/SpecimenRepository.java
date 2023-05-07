package com.Library.restAPI.repository;

import com.Library.restAPI.model.Specimen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecimenRepository extends JpaRepository<Specimen, Long> {
}
