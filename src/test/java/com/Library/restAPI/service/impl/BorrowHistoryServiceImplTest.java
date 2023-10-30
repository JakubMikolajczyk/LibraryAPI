package com.Library.restAPI.service.impl;

import com.Library.restAPI.exception.BorrowHistoryNotFoundException;
import com.Library.restAPI.model.BorrowHistory;
import com.Library.restAPI.model.User;
import com.Library.restAPI.repository.BorrowHistoryRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BorrowHistoryServiceImplTest {

    @Mock
    private BorrowHistoryRepository borrowHistoryRepository;

    @InjectMocks
    private BorrowHistoryServiceImpl borrowHistoryService;

    @Test
    void getHistoryById() {
        //given
        BorrowHistory borrowHistory = Instancio.create(BorrowHistory.class);
        Long id = borrowHistory.getId();
        when(borrowHistoryRepository.findById(id)).thenReturn(Optional.of(borrowHistory));
        //when
        BorrowHistory borrowHistoryFromService = borrowHistoryService.getHistoryById(id);
        //then
        assertEquals(borrowHistory, borrowHistoryFromService);
        verify(borrowHistoryRepository).findById(id);
    }

    @Test
    void getHistoryByIdNotFound(){
        //given
        Long id = new Random().nextLong();
        when(borrowHistoryRepository.findById(anyLong())).thenReturn(Optional.empty());
        //when
        //then
        assertThrowsExactly(BorrowHistoryNotFoundException.class, () -> borrowHistoryService.getHistoryById(id));
        verify(borrowHistoryRepository).findById(id);
    }

    @Test
    void getHistoryByIdAndUserId() {
        //given
        BorrowHistory borrowHistory = Instancio.create(BorrowHistory.class);
        Long id = borrowHistory.getId();
        Long userId = borrowHistory.getUser().getId();
        when(borrowHistoryRepository.findByIdAndUserId(id, userId)).thenReturn(Optional.of(borrowHistory));
        //when
        BorrowHistory borrowHistoryFromService = borrowHistoryService.getHistoryByIdAndUserId(id, userId);
        //then
        assertEquals(borrowHistory, borrowHistoryFromService);
        verify(borrowHistoryRepository).findByIdAndUserId(id, userId);
    }

    @Test
    void getHistoryByIdAndUserIdNotFound() {
        //given
        Long id = new Random().nextLong();
        Long userId = new Random().nextLong();
        when(borrowHistoryRepository.findByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.empty());
        //when
        //then
        assertThrowsExactly(BorrowHistoryNotFoundException.class, () -> borrowHistoryService.getHistoryByIdAndUserId(id, userId));
        verify(borrowHistoryRepository).findByIdAndUserId(id, userId);
    }

    @Test
    void getAllHistoryHiddenTrue() {
        //given
        Long userId = new Random().nextLong();
        List<BorrowHistory> borrowHistoryList = Instancio.ofList(BorrowHistory.class)
                .size(10)
                .set(field(BorrowHistory::isHidden), true)
                .create();
        when(borrowHistoryRepository.findAllByUserId(userId)).thenReturn(borrowHistoryList);
        //when
        List<BorrowHistory> borrowHistoryListFromService = borrowHistoryService.getAllHistoryByUserId(userId, true);
        //then
        assertEquals(borrowHistoryList, borrowHistoryListFromService);
        verify(borrowHistoryRepository).findAllByUserId(userId);
    }

    @Test
    void getAllHistoryHiddenTrueFalse() {
        //given
        Long userId = new Random().nextLong();
        List<BorrowHistory> borrowHistoryList = Instancio.ofList(BorrowHistory.class)
                .size(10)
                .set(field(BorrowHistory::isHidden), false)
                .create();
        when(borrowHistoryRepository.findAllByUserIdAndHiddenIsFalse(userId)).thenReturn(borrowHistoryList);
        //when
        List<BorrowHistory> borrowHistoryListFromService = borrowHistoryService.getAllHistoryByUserId(userId, false);
        //then
        assertEquals(borrowHistoryList, borrowHistoryListFromService);
        verify(borrowHistoryRepository).findAllByUserIdAndHiddenIsFalse(userId);
    }

    @Test
    void getAllHistoryByUserId() {
        //given
        //when
        //then
    }

    @Test
    void getAllHistoryByBookId() {
        //given
        //when
        //then
    }

    @Test
    void editHistory() {
        //given
        //when
        //then                      
    }
}