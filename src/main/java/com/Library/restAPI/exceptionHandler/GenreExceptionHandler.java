package com.Library.restAPI.exceptionHandler;

import com.Library.restAPI.dto.response.Link;
import com.Library.restAPI.exception.GenreDeleteException;
import com.Library.restAPI.exception.GenreNotFoundException;
import com.Library.restAPI.mapper.LinkMapper;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@ControllerAdvice
public class GenreExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(GenreNotFoundException.class)
    public ResponseEntity<ErrorMessage> genreNotFound(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage("Genre not found."));
    }

    @ExceptionHandler(GenreDeleteException.class)
    public ResponseEntity<List<Link>> genreDelete(GenreDeleteException exception){
        List<Link> links = exception.getGenre()
                .getBooks()
                .stream()
                .map(LinkMapper::toLink)
                .toList();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(links);
    }
}
