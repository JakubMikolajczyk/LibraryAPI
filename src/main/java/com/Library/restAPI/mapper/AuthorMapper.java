package com.Library.restAPI.mapper;

import com.Library.restAPI.dto.request.AuthorRequest;
import com.Library.restAPI.dto.response.AuthorDto;
import com.Library.restAPI.model.Author;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
public class AuthorMapper {

    public static AuthorDto toDto(Author author){
        return AuthorDto.builder()
                .id(author.getId())
                .name(author.getName())
                .surname(author.getSurname())
                .build();
    }

    public Author toEntity(AuthorRequest authorRequest){
        return Author.builder()
                .name(authorRequest.name())
                .surname(authorRequest.surname())
                .build();
    }

    public Author toEntity(Long id, AuthorRequest authorRequest){
        Author author = toEntity(authorRequest);
        author.setId(id);
        return author;
    }
}
