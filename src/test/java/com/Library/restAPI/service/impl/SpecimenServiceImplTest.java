package com.Library.restAPI.service.impl;

import com.Library.restAPI.exception.BookNotExistsException;
import com.Library.restAPI.exception.SpecimenDeleteException;
import com.Library.restAPI.exception.SpecimenNotFoundException;
import com.Library.restAPI.model.SpecimenBorrow;
import com.Library.restAPI.repository.SpecimenBorrowRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpecimenServiceImplTest {

    @Mock
    private SpecimenBorrowRepository specimenBorrowRepository;

    @InjectMocks
    private SpecimenServiceImpl specimenService;

    @Test
    void getSpecimenById() {
        //given
        SpecimenBorrow specimenBorrow = Instancio.create(SpecimenBorrow.class);
        Long id = specimenBorrow.getId();
        when(specimenBorrowRepository.findById(id)).thenReturn(Optional.of(specimenBorrow));
        //when
        SpecimenBorrow specimenBorrowFromService = specimenService.getSpecimenById(id);
        //then
        assertEquals(specimenBorrow, specimenBorrowFromService);
        verify(specimenBorrowRepository).findById(id);
    }

    @Test
    void getSpecimenByIdNotFound() {
        //given
        Long id = new Random().nextLong();
        when(specimenBorrowRepository.findById(anyLong())).thenReturn(Optional.empty());
        //when
        //then
        assertThrowsExactly(SpecimenNotFoundException.class, () -> specimenService.getSpecimenById(id));
        verify(specimenBorrowRepository).findById(id);
    }

    @Test
    void getAllSpecimen() {
        //given
        List<SpecimenBorrow> specimenBorrowsList = Instancio.ofList(SpecimenBorrow.class).size(10).create();
        when(specimenBorrowRepository.findAll()).thenReturn(specimenBorrowsList);
        //when
        List<SpecimenBorrow> specimenBorrowFromServiceList = specimenService.getAllSpecimen();
        //then
        assertEquals(specimenBorrowsList, specimenBorrowFromServiceList);
        verify(specimenBorrowRepository).findAll();
    }

    @Test
    void createSpecimen() {
        //given
        SpecimenBorrow specimenBorrow = Instancio.of(SpecimenBorrow.class)
                .ignore(field(SpecimenBorrow::getId))
                .ignore(field(SpecimenBorrow::getUser))
                .ignore(field(SpecimenBorrow::getStartTime))
                .create();
        //when
        specimenService.createSpecimen(specimenBorrow);
        //then
        verify(specimenBorrowRepository).save(specimenBorrow);
    }

    @Test
    void createSpecimenBookNotExists() {
        //given
        SpecimenBorrow specimenBorrow = new SpecimenBorrow();
        when(specimenBorrowRepository.save(specimenBorrow)).thenThrow(DataIntegrityViolationException.class);
        //when
        //then
        assertThrowsExactly(BookNotExistsException.class, () -> specimenService.createSpecimen(specimenBorrow));
        verify(specimenBorrowRepository).save(specimenBorrow);
    }

    @Test
    void deleteSpecimenById() {
        //given
        SpecimenBorrow specimenBorrow = Instancio.of(SpecimenBorrow.class)
                .ignore(field(SpecimenBorrow::getUser))
                .create();
        Long id = specimenBorrow.getId();
        when(specimenBorrowRepository.findById(id)).thenReturn(Optional.of(specimenBorrow));
        //when
        specimenService.deleteSpecimenById(id);
        //then
        verify(specimenBorrowRepository).findById(id);
        verify(specimenBorrowRepository).deleteById(id);
    }

    @Test
    void deleteSpecimenByIdNotFound() {
        //given
        Long id = new Random().nextLong();
        when(specimenBorrowRepository.findById(anyLong())).thenReturn(Optional.empty());
        //when
        specimenService.deleteSpecimenById(id);
        //then
        verify(specimenBorrowRepository).findById(id);
        verify(specimenBorrowRepository, never()).deleteById(anyLong());
    }

    @Test
    void deleteSpecimenByIdUserIsNotNull() {
        //given
        SpecimenBorrow specimenBorrow = Instancio.of(SpecimenBorrow.class).create();
        Long id = specimenBorrow.getId();
        when(specimenBorrowRepository.findById(id)).thenReturn(Optional.of(specimenBorrow));
        //when
        //then
        SpecimenDeleteException specimenDeleteException = assertThrowsExactly(SpecimenDeleteException.class, () -> specimenService.deleteSpecimenById(id));
        assertEquals(specimenBorrow, specimenDeleteException.getSpecimenBorrow());
        verify(specimenBorrowRepository).findById(id);
        verify(specimenBorrowRepository, never()).deleteById(anyLong());
    }
}