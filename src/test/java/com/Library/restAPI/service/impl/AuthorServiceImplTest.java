package com.Library.restAPI.service.impl;

import com.Library.restAPI.exception.AuthorDeleteException;
import com.Library.restAPI.exception.AuthorNotFoundException;
import com.Library.restAPI.model.Author;
import com.Library.restAPI.repository.AuthorRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceImplTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorServiceImpl authorService;

    @Test
    void getAuthorById() {
        //given
        Author author = Instancio.create(Author.class);
        when(authorRepository.findById(author.getId())).thenReturn(Optional.of(author));
        //when
        Author authorFromService = authorService.getAuthorById(author.getId());
        //then
        assertEquals(author, authorFromService);
        verify(authorRepository).findById(author.getId());
    }

    @Test
    void getAuthorByIdNotFound() {
        //given
        Long id = new Random().nextLong();
        when(authorRepository.findById(anyLong())).thenReturn(Optional.empty());
        //when
        //then
        assertThrowsExactly(AuthorNotFoundException.class, () -> authorService.getAuthorById(id));
        verify(authorRepository).findById(id);
    }

    @Test
    void getAll() {
        //given
        List<Author> authorList = Instancio.ofList(Author.class).size(10).create();
        Pageable pageable = mock(Pageable.class);
        Page<Author> authorPage = new PageImpl<>(authorList, pageable, 10);
        when(authorRepository.findAll(pageable)).thenReturn(authorPage);
        //when
        Page<Author> authorPageFromService = authorService.getAll(pageable);
        //then
        assertEquals(authorPage, authorPageFromService);
        verify(authorRepository).findAll(pageable);
    }

    @Test
    void createAuthor() {
        //given
        Author author = Instancio.of(Author.class)
                .ignore(field(Author::getId))
                .ignore(field(Author::getBooks))
                .create();
        //when
        authorService.createAuthor(author);
        //then
        verify(authorRepository).save(author);
    }

    @Test
    void editAuthor() {
        //given
        Author author = Instancio.of(Author.class)
                .ignore(field(Author::getBooks))
                .create();
        //when
        authorService.createAuthor(author);
        //then
        verify(authorRepository).save(author);
    }

    @Test
    void deleteAuthor() {
        //given
        Long id = new Random().nextLong();
        //when
        authorService.deleteAuthor(id);
        //then
        verify(authorRepository).deleteById(id);
    }

    @Test
    void deleteAuthorDataIntegrityViolation() {
        //given
        Author author = Instancio.create(Author.class);
        Long id = author.getId();
        doThrow(new DataIntegrityViolationException("")).when(authorRepository).deleteById(id);
        when(authorRepository.findById(id)).thenReturn(Optional.of(author));
        //when
        //then
        AuthorDeleteException authorDeleteException = assertThrowsExactly(AuthorDeleteException.class, () -> authorService.deleteAuthor(id));
        assertEquals(author, authorDeleteException.getAuthor());
    }
}