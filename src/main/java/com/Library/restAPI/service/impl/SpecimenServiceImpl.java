package com.Library.restAPI.service.impl;

import com.Library.restAPI.exception.BookNotExists;
import com.Library.restAPI.exception.SpecimenDeleteException;
import com.Library.restAPI.exception.SpecimenNotFoundException;
import com.Library.restAPI.model.SpecimenBorrow;
import com.Library.restAPI.repository.SpecimenBorrowRepository;
import com.Library.restAPI.service.SpecimenService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SpecimenServiceImpl implements SpecimenService {

    private final SpecimenBorrowRepository specimenBorrowRepository;

    @Override
    public SpecimenBorrow getSpecimenById(Long id) {
        return specimenBorrowRepository.findById(id)
                .orElseThrow(SpecimenNotFoundException::new);
    }

    @Override
    public List<SpecimenBorrow> getAllSpecimen() {
        return specimenBorrowRepository.findAll();
    }

    @Override
    public void createSpecimen(SpecimenBorrow specimenBorrow) {
        try {
            specimenBorrowRepository.save(specimenBorrow);
        }
        catch (DataIntegrityViolationException exception){
            throw new BookNotExists();
        }
    }

    @Override
    public void deleteSpecimenById(Long id) {
        Optional<SpecimenBorrow> specimenFromDb = specimenBorrowRepository.findById(id);

        if (specimenFromDb.isEmpty())
            return;

        if (specimenFromDb.get().getUser() != null)
            throw new SpecimenDeleteException(specimenFromDb.get());

        specimenBorrowRepository.deleteById(id);
    }
}
