package com.Library.restAPI.exception;

import com.Library.restAPI.model.Genre;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GenreDeleteException extends RuntimeException{
    private Genre genre;
}
