package com.Library.restAPI.service.impl;

import com.Library.restAPI.model.Specimen;
import com.Library.restAPI.repository.SpecimenRepository;
import com.Library.restAPI.service.SpecimenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SpecimenServiceImpl implements SpecimenService {

    private final SpecimenRepository specimenRepository;

    @Override
    public Specimen getSpecimenById(Long id) {
        return specimenRepository.findById(id)
                .orElseThrow(RuntimeException::new);    //TODO exception
    }


    @Override
    public List<Specimen> getAllSpecimen() {
        return specimenRepository.findAll();
    }

    @Override
    public List<Specimen> getSpecimenByBookId(Long bookId) {
        return specimenRepository.findAllByBookId(bookId);
    }

    @Override
    public void createSpecimen(Specimen specimen) {
        specimenRepository.save(specimen);
    }

    @Override
    public void deleteSpecimenById(Long id) {
        specimenRepository.deleteById(id);
    }
}
