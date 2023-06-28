package com.Library.restAPI.service;

import com.Library.restAPI.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {

    public Book getBookById(Long id);
    public Page<Book> getAllBooks(Pageable pageable);

    public Page<Book> getAllBooksWithoutDeleted(Pageable pageable);
    public void createBook(Book book);
    public void editBook(Book book);

    public void deleteBookById(Long id);
}
