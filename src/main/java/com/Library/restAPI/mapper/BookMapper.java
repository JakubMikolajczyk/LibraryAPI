package com.Library.restAPI.mapper;


import com.Library.restAPI.dto.request.BookRequest;
import com.Library.restAPI.dto.response.BookDto;
import com.Library.restAPI.exception.AuthorNotFoundException;
import com.Library.restAPI.model.Author;
import com.Library.restAPI.model.Book;
import com.Library.restAPI.model.Genre;
import com.Library.restAPI.repository.AuthorRepository;
import com.Library.restAPI.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class BookMapper {

    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    public static BookDto toDto(Book book){
        return BookDto.builder()
                .id(book.getId())
                .tittle(book.getTittle())
                .ISBN(book.getISBN())
                .year(book.getYear())
                .deleteDate(book.getDeleteDate())
                .author(LinkMapper.toLink(book.getAuthor()))
                .genres(book.getGenres()
                        .stream()
                        .map(LinkMapper::toLink)
                        .collect(Collectors.toList()))
                .build();
    }

    public Book toEntity(Long id, BookRequest bookRequest){
        Book book = toEntity(bookRequest);
        book.setId(id);
        return book;
    }

    public Book toEntity(BookRequest bookRequest){
        if (bookRequest.authorId() == null)
            throw new AuthorNotFoundException();
        Author authorFromDB = authorRepository.getReferenceById(bookRequest.authorId());

        List<Genre> genres = bookRequest.genresId() == null? null:
                bookRequest.genresId()
                        .stream()
                        .map(genreRepository::getReferenceById)
                        .toList();

        return Book.builder()
                .tittle(bookRequest.tittle())
                .ISBN(bookRequest.ISBN())
                .year(bookRequest.year())
                .deleteDate(bookRequest.deleteDate())
                .author(authorFromDB)
                .genres(genres)
                .build();
    }
}
