package com.Library.restAPI.service.impl;

import com.Library.restAPI.exception.GenreDeleteException;
import com.Library.restAPI.exception.GenreNotFoundException;
import com.Library.restAPI.model.Genre;
import com.Library.restAPI.repository.GenreRepository;
import com.Library.restAPI.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GenreServiceImp implements GenreService {

    private final GenreRepository genreRepository;


    @Override
    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    @Override
    public Genre getGenreById(Long id) {
        return genreRepository.findById(id)
                .orElseThrow(GenreNotFoundException::new);
    }

    @Override
    public void createGenre(Genre genre) {
        genreRepository.save(genre);
    }

    @Override
    public void editGenre(Genre genre) {
        genreRepository.save(genre);
    }

    @Override
    public void deleteGenreById(Long id) {
        try {
            genreRepository.deleteById(id);
        }
        catch (DataIntegrityViolationException exception){ //only possible if contain related book
            throw new GenreDeleteException(genreRepository.findById(id).get()); //throw only if genre exists
        }
    }
}
