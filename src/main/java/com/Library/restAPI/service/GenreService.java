package com.Library.restAPI.service;

import com.Library.restAPI.model.Genre;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GenreService {

    List<Genre> getAllGenres();
    Genre getGenreById(Long id);

    void createGenre(Genre genre);
    void editGenre(Genre genre);
    void deleteGenreById(Long id);

}
