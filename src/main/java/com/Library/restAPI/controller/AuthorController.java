package com.Library.restAPI.controller;

import com.Library.restAPI.dto.request.AuthorRequest;
import com.Library.restAPI.dto.response.AuthorDto;
import com.Library.restAPI.mapper.AuthorMapper;
import com.Library.restAPI.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/")
public class AuthorController {

    private final AuthorService authorService;
    private final AuthorMapper authorMapper;

    @GetMapping("authors/{id}")
    public AuthorDto getAuthor(@PathVariable("id") Long id){
        return AuthorMapper.toDto(authorService.getAuthorById(id));
    }

    @GetMapping("authors")
    public Page<AuthorDto> getAllAuthors(@PageableDefault(sort = "id", value = 50) Pageable pageable){
        return authorService.getAll(pageable).map(AuthorMapper::toDto);
    }
    
    @PostMapping("authors")
    public void createAuthor(@RequestBody AuthorRequest authorRequest){
        authorService.createAuthor(authorMapper.toEntity(authorRequest));
    }

    @PutMapping("authors/{id}")
    public void editAuthor(@PathVariable("id") Long id, @RequestBody AuthorRequest authorRequest){
        authorService.editAuthor(authorMapper.toEntity(id, authorRequest));
    }

    @DeleteMapping("authors/{id}")
    public void deleteAuthor(@PathVariable("id") Long id){
        authorService.deleteAuthor(id);
    }
}
