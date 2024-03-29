package com.Library.restAPI.controller;

import com.Library.restAPI.dto.response.BorrowHistoryDto;
import com.Library.restAPI.mapper.BorrowHistoryMapper;
import com.Library.restAPI.service.BorrowHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/")
public class BorrowHistoryController {

    private final BorrowHistoryService borrowHistoryService;

    @GetMapping("borrow-histories")
    public List<BorrowHistoryDto> getAllHistory(){
        return borrowHistoryService
                .getAllHistory()
                .stream()
                .map(BorrowHistoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("borrow-histories/{id}")
    public BorrowHistoryDto getHistoryById(@PathVariable("id") Long id){
        return BorrowHistoryMapper.toDto(borrowHistoryService.getHistoryById(id));
    }
}
