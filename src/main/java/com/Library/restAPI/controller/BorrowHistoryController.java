package com.Library.restAPI.controller;

import com.Library.restAPI.dto.request.BorrowHistoryEditRequest;
import com.Library.restAPI.dto.response.BorrowHistoryDto;
import com.Library.restAPI.mapper.BorrowHistoryMapper;
import com.Library.restAPI.security.UsernameAndIdPrincipal;
import com.Library.restAPI.service.BorrowHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/")
public class BorrowHistoryController {

    private final BorrowHistoryService borrowHistoryService;
    private final BorrowHistoryMapper borrowHistoryMapper;

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

    @GetMapping("users/me/borrow-histories/{id}")
    public BorrowHistoryDto getHistoryByIdAndUser(@PathVariable("id") Long id){
        return BorrowHistoryMapper.toDto(borrowHistoryService.getHistoryByIdAndUserId(id, UsernameAndIdPrincipal.getIdFromSecurityCtx()));
    }

    @GetMapping("users/me/borrow-histories")
    public List<BorrowHistoryDto> getMyHistory(@RequestParam(required = false) boolean showHidden){
        return borrowHistoryService
                .getAllHistoryByUserId(UsernameAndIdPrincipal.getIdFromSecurityCtx(),showHidden)
                .stream()
                .map(BorrowHistoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @PutMapping("users/me/borrow-histories/{id}")
    public void editMyHistory(@PathVariable("id") Long id, @RequestBody BorrowHistoryEditRequest borrowHistoryEditRequest){
        borrowHistoryService.editHistory(borrowHistoryMapper.toEntity(borrowHistoryEditRequest, id), UsernameAndIdPrincipal.getIdFromSecurityCtx());
    }
}
