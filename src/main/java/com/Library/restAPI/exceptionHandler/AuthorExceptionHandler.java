package com.Library.restAPI.exceptionHandler;


import com.Library.restAPI.dto.response.Link;
import com.Library.restAPI.exception.AuthorDeleteException;
import com.Library.restAPI.exception.AuthorNotFoundException;
import com.Library.restAPI.mapper.LinkMapper;
import com.Library.restAPI.model.Author;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@ControllerAdvice
public class AuthorExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AuthorNotFoundException.class)
    public ResponseEntity<ErrorMessage> authorNotFound(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage("Author not found."));
    }

    @ExceptionHandler(AuthorDeleteException.class)
    public ResponseEntity<List<Link>> authorDelete(AuthorDeleteException exception){
        Author authorFromException = exception.getAuthor();
        List<Link> links = authorFromException.getBooks().stream().map(LinkMapper::toLink).toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(links);
    }
}
