package com.Library.restAPI.service;

import com.Library.restAPI.model.Author;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;

public interface AuthorService {
    public Author getAuthorById(Long id);
    public Page<Author> getAll(Pageable pageable);

    public void createAuthor(Author author);
    public void editAuthor(Author author);

    public void deleteAuthor(Long id);
}
