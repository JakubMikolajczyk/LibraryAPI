package com.Library.restAPI.mapper;

import com.Library.restAPI.dto.response.Link;
import com.Library.restAPI.model.*;

public class LinkMapper {

    public static Link toLink(Author author){
        return author == null ? null : new Link(author.getId(), "authors/");
    }

    public static Link toLink(Book book){
        return book == null ? null : new Link(book.getId(), "books/");
    }

    public static Link toLink(Category category){
        return category == null ? null : new Link(category.getId(), "categories/");
    }

    public static Link toLink(User user){
        return user == null ? null : new Link(user.getId(), "users/");
    }
}
