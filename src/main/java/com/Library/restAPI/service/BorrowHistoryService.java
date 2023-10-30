package com.Library.restAPI.service;

import com.Library.restAPI.model.BorrowHistory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BorrowHistoryService {

    BorrowHistory getHistoryById(Long id);
    BorrowHistory getHistoryByIdAndUserId(Long id, Long userId);
    List<BorrowHistory> getAllHistory();
    List<BorrowHistory> getAllHistoryByUserId(Long userId, boolean showHidden);
    List<BorrowHistory> getAllHistoryByBookId(Long bookId);
    void editHistory(BorrowHistory borrowHistory, Long userId);
}
