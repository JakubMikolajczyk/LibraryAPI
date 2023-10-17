package com.Library.restAPI.service.impl;

import com.Library.restAPI.exception.GenreDeleteException;
import com.Library.restAPI.exception.GenreNotFoundException;
import com.Library.restAPI.model.Genre;
import com.Library.restAPI.repository.GenreRepository;
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
class GenreServiceImpTest {

    @Mock
    private GenreRepository genreRepository;

    @InjectMocks
    private GenreServiceImp genreService;

    @Test
    void getGenreById() {
        //given
        Genre genre = Instancio.create(Genre.class);
        Long id = genre.getId();
        when(genreRepository.findById(id)).thenReturn(Optional.of(genre));
        //when
        Genre genreFromService = genreService.getGenreById(id);
        //then
        assertEquals(genre, genreFromService);
        verify(genreRepository).findById(id);
    }

    @Test
    void getGenreByIdNotFound() {
        //given
        Long id = new Random().nextLong();
        when(genreRepository.findById(anyLong())).thenReturn(Optional.empty());
        //when
        //then
        assertThrowsExactly(GenreNotFoundException.class, () -> genreService.getGenreById(id));
        verify(genreRepository).findById(id);
    }

    @Test
    void getAllGenres() {
        //given
        List<Genre> genreList = Instancio.ofList(Genre.class).size(10).create();
        when(genreRepository.findAll()).thenReturn(genreList);
        //when
        List<Genre> genreListFromService = genreService.getAllGenres();
        //then
        assertEquals(genreList, genreListFromService);
        verify(genreRepository).findAll();
    }

    @Test
    void createGenre() {
        //given
        Genre genre = Instancio.of(Genre.class)
                .ignore(field(Genre::getId))
                .ignore(field(Genre::getBooks))
                .create();
        //when
        genreService.createGenre(genre);
        //then
        verify(genreRepository).save(genre);
    }

    @Test
    void editGenre() {
        //given
        Genre genre = Instancio.of(Genre.class)
                .ignore(field(Genre::getBooks))
                .create();
        //when
        genreService.editGenre(genre);
        //then
        verify(genreRepository).save(genre);

    }

    @Test
    void deleteGenreById() {
        //given
        Long id = new Random().nextLong();
        //when
        genreService.deleteGenreById(id);
        //then
        verify(genreRepository).deleteById(id);
    }

    @Test
    void deleteGenreByIdDataIntegrityViolation() {
        //given
        Genre genre = Instancio.create(Genre.class);
        Long id = genre.getId();
        when(genreRepository.findById(id)).thenReturn(Optional.of(genre));
        doThrow(DataIntegrityViolationException.class).when(genreRepository).deleteById(id);
        //when
        GenreDeleteException exception = assertThrowsExactly(GenreDeleteException.class,
                () -> genreService.deleteGenreById(id));
        //then
        assertEquals(genre, exception.getGenre());
        verify(genreRepository).deleteById(id);
        verify(genreRepository).findById(id);
    }
}