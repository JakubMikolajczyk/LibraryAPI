package com.Library.restAPI.service;

import com.Library.restAPI.model.Specimen;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SpecimenService {

    public Specimen getSpecimenById(Long id);
    public List<Specimen> getAllSpecimen();

    public List<Specimen> getSpecimenByBookId(Long bookId);

    public void createSpecimen(Specimen specimen);
    public void deleteSpecimenById(Long id);
}
