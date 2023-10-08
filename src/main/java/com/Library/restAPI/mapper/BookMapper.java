package com.Library.restAPI.mapper;


import com.Library.restAPI.dto.response.BookDto;
import com.Library.restAPI.dto.request.BookRequest;
import com.Library.restAPI.exception.AuthorNotFoundException;
import com.Library.restAPI.model.Author;
import com.Library.restAPI.model.Book;
import com.Library.restAPI.model.Category;
import com.Library.restAPI.repository.AuthorRepository;
import com.Library.restAPI.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class BookMapper {

    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;

    public BookDto toDto(Book book){
        return BookDto.builder()
                .id(book.getId())
                .tittle(book.getTittle())
                .ISBN(book.getISBN())
                .year(book.getYear())
                .deleteDate(book.getDeleteDate())
                .author(LinkMapper.toLink(book.getAuthor()))
                .categories(book.getCategories()
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

        List<Category> categories = bookRequest.categoriesId() == null? null:
                bookRequest.categoriesId()
                        .stream()
                        .map(categoryRepository::getReferenceById)
                        .toList();

        return Book.builder()
                .tittle(bookRequest.tittle())
                .ISBN(bookRequest.ISBN())
                .year(bookRequest.year())
                .deleteDate(bookRequest.deleteDate())
                .author(authorFromDB)
                .categories(categories)
                .build();
    }
}
