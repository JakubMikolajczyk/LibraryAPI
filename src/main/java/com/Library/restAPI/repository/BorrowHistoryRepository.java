package com.Library.restAPI.repository;

import com.Library.restAPI.model.BorrowHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowHistoryRepository extends JpaRepository<BorrowHistory, Long> {
}
