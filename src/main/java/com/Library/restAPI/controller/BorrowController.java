package com.Library.restAPI.controller;

import com.Library.restAPI.dto.request.BorrowRequest;
import com.Library.restAPI.dto.request.BorrowUsernameRequest;
import com.Library.restAPI.dto.response.BorrowDto;
import com.Library.restAPI.mapper.BorrowMapper;
import com.Library.restAPI.service.BorrowService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/")
public class BorrowController {
    private final BorrowService borrowService;
    private final BorrowMapper borrowMapper;

    @GetMapping("borrows")
    public List<BorrowDto> getAllBorrows(){
        return borrowService
                .getAllBorrows()
                .stream()
                .map(BorrowMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("borrows/{id}")
    public BorrowDto getBorrowById(@PathVariable("id") Long id){
        return BorrowMapper.toDto(borrowService.getBorrowById(id));
    }

    @PostMapping("borrows")
    public void createBorrow(@RequestBody BorrowRequest borrowRequest){
        borrowService.createBorrow(borrowMapper.toEntity(borrowRequest));
    }

    @PostMapping("borrows/byUsername")
    public void createBorrowByUsername(@RequestBody BorrowUsernameRequest borrowUsernameRequest){
        borrowService.createBorrow(borrowMapper.toEntity(borrowUsernameRequest));
    }
    @DeleteMapping({"borrows/{id}"})
    public void deleteBorrow(@PathVariable("id") Long id){
        borrowService.deleteBorrowById(id);
    }
}
