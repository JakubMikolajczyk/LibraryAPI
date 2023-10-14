package com.Library.restAPI.controller;

import com.Library.restAPI.dto.request.GenreRequest;
import com.Library.restAPI.dto.response.GenreDto;
import com.Library.restAPI.mapper.GenreMapper;
import com.Library.restAPI.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/genres")
public class GenreController {

    private final GenreService genreService;
    private final GenreMapper genreMapper;

    @GetMapping
    public List<GenreDto> getAllGenres(){
        return genreService.getAllGenres()
                .stream()
                .map(genreMapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public void createGenre(@RequestBody GenreRequest genreRequest){
        genreService.createGenre(genreMapper.toEntity(genreRequest));
    }

    @GetMapping("/{id}")
    public GenreDto getGenre(@PathVariable Long id){
        return genreMapper.toDto(genreService.getGenreById(id));
    }

    @PutMapping("/{id}")
    public void editGenre(@PathVariable Long id, @RequestBody GenreRequest genreRequest){
        genreService.editGenre(genreMapper.toEntity(id, genreRequest));
    }

    @DeleteMapping("/{id}")
    public void deleteGenre(@PathVariable Long id){
        genreService.deleteGenreById(id);
    }
}
