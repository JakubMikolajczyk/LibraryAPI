package com.Library.restAPI.controller;

import com.Library.restAPI.dto.request.BookRequest;
import com.Library.restAPI.dto.response.BookDto;
import com.Library.restAPI.dto.response.BorrowDto;
import com.Library.restAPI.dto.response.SpecimenDto;
import com.Library.restAPI.mapper.BookMapper;
import com.Library.restAPI.mapper.BorrowMapper;
import com.Library.restAPI.mapper.SpecimenMapper;
import com.Library.restAPI.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/")
public class BookController {

    private final BookService bookService;
    private final BookMapper bookMapper;

    @GetMapping("books")
    public Page<BookDto> getAllBooks(Pageable pageable){
        return bookService.getAllBooks(pageable)
                .map(BookMapper::toDto);
    }

    @PostMapping("books")
    public void createBook(@RequestBody BookRequest bookRequest){
        bookService.createBook(bookMapper.toEntity(bookRequest));
    }

    @GetMapping("books/{id}")
    public BookDto getBookById(@PathVariable Long id){
        return BookMapper.toDto(bookService.getBookById(id));
    }

    @PutMapping("books/{id}")
    public void editBookById(@PathVariable Long id, @RequestBody BookRequest bookRequest){
        bookService.editBook(bookMapper.toEntity(id, bookRequest));
    }

    @DeleteMapping("books/{id}")
    public void deleteBookById(@PathVariable Long id){
        bookService.deleteBookById(id);
    }

    @GetMapping("books/{bookId}/specimens")
    public List<SpecimenDto> getSpecimenByBookId(@PathVariable Long bookId){
        return bookService
                .getBookById(bookId)
                .getSpecimenBorrows()
                .stream()
                .map(SpecimenMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("books/{bookId}/borrows")
    public List<BorrowDto> getBorrowsByBookId(@PathVariable Long bookId){
        return bookService.getBookById(bookId)
                .getSpecimenBorrows()
                .stream()
                .map(BorrowMapper::toDto)
                .collect(Collectors.toList());
    }
}
