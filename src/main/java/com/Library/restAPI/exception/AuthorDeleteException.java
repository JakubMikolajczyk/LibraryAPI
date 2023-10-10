package com.Library.restAPI.exception;

import com.Library.restAPI.model.Author;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthorDeleteException extends RuntimeException{

    private final Author author;

}
