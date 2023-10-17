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
@RequestMapping("api/v1/")
public class GenreController {

    private final GenreService genreService;
    private final GenreMapper genreMapper;

    @GetMapping("genres")
    public List<GenreDto> getAllGenres(){
        return genreService.getAllGenres()
                .stream()
                .map(GenreMapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping("genres")
    public void createGenre(@RequestBody GenreRequest genreRequest){
        genreService.createGenre(genreMapper.toEntity(genreRequest));
    }

    @GetMapping("genres/{id}")
    public GenreDto getGenre(@PathVariable("id") Long id){
        return GenreMapper.toDto(genreService.getGenreById(id));
    }

    @PutMapping("genres/{id}")
    public void editGenre(@PathVariable("id") Long id, @RequestBody GenreRequest genreRequest){
        genreService.editGenre(genreMapper.toEntity(id, genreRequest));
    }

    @DeleteMapping("genres/{id}")
    public void deleteGenre(@PathVariable("id") Long id){
        genreService.deleteGenreById(id);
    }
}
