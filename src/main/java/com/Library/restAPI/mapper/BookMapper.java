package com.Library.restAPI.mapper;


import com.Library.restAPI.dto.BookDto;
import com.Library.restAPI.dto.BookRequest;
import com.Library.restAPI.dto.Link;
import com.Library.restAPI.model.Author;
import com.Library.restAPI.model.Book;
import com.Library.restAPI.model.Category;
import com.Library.restAPI.repository.AuthorRepository;
import com.Library.restAPI.repository.BookRepository;
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
    private final BookRepository bookRepository;

    public BookDto toDto(Book book){
        return BookDto.builder()
                .id(book.getId())
                .tittle(book.getTittle())
                .ISBN(book.getISBN())
                .year(book.getYear())
                .deleteDate(book.getDeleteDate())
                .author(book.getAuthor() == null ?
                        null
                        : new Link(book.getAuthor().getId(), "/api/v1/authors/"))
                .categories(book.getCategories()
                        .stream()
                        .map(category -> new Link(category.getId(), "/api/v1/categories/"))
                        .collect(Collectors.toList()))
                .build();
    }

    public Book toEntity(Long id, BookRequest bookRequest){
        Book bookFromDB = bookRepository.findById(id)
                .orElseThrow(RuntimeException::new);    //TODO Exception
        return toEntity(bookFromDB, bookRequest);
    }

    public Book toEntity(BookRequest bookRequest){
        Author authorFromDB = bookRequest.authorId() == null? null:
                authorRepository.getReferenceById(bookRequest.authorId());

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

    private Book toEntity(Book book, BookRequest bookRequest){
        Author authorFromDB = bookRequest.authorId() == null? null:
                authorRepository.getReferenceById(bookRequest.authorId());

        List<Category> categories = bookRequest.categoriesId() == null? null:
                bookRequest.categoriesId()
                        .stream()
                        .map(categoryRepository::getReferenceById)
                        .toList();

        book.setTittle(bookRequest.tittle());
        book.setISBN(bookRequest.ISBN());
        book.setYear(bookRequest.year());
        book.setDeleteDate(bookRequest.deleteDate());
        book.setAuthor(authorFromDB);
        book.setCategories(categories);
        return book;
    }
}
