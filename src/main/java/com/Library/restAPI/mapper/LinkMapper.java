package com.Library.restAPI.mapper;

import com.Library.restAPI.dto.response.Link;
import com.Library.restAPI.model.Author;
import com.Library.restAPI.model.Book;
import com.Library.restAPI.model.Genre;
import com.Library.restAPI.model.User;

public class LinkMapper {

    public static Link toLink(Author author){
        return author == null ? null : new Link(author.getId(), "authors/");
    }

    public static Link toLink(Book book){
        return book == null ? null : new Link(book.getId(), "books/");
    }

    public static Link toLink(Genre genre){
        return genre == null ? null : new Link(genre.getId(), "genres/");
    }

    public static Link toLink(User user){
        return user == null ? null : new Link(user.getId(), "users/");
    }
}
