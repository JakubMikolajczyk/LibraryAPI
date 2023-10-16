package com.Library.restAPI.mapper;

import com.Library.restAPI.dto.request.GenreRequest;
import com.Library.restAPI.dto.response.GenreDto;
import com.Library.restAPI.model.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class GenreMapper {

    public static GenreDto toDto(Genre genre){
        return GenreDto.builder()
                .id(genre.getId())
                .name(genre.getName())
                .books(genre.getBooks()
                        .stream()
                        .map(LinkMapper::toLink)
                        .collect(Collectors.toList()))
                .build();
    }

    public Genre toEntity(GenreRequest genreRequest){
        return Genre.builder()
                .name(genreRequest.name())
                .build();
    }

    public Genre toEntity(Long id, GenreRequest genreRequest){
        Genre genre = toEntity(genreRequest);
        genre.setId(id);
        return genre;
    }
}
