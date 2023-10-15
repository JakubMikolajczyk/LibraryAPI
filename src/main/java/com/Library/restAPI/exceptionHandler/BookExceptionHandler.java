package com.Library.restAPI.exceptionHandler;

import com.Library.restAPI.exception.BookNotExistsException;
import com.Library.restAPI.exception.BookNotFoundException;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class BookExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ErrorMessage> bookNotFound(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage("Book not found."));
    }

    @ExceptionHandler(BookNotExistsException.class)
    public ResponseEntity<ErrorMessage> bookNotExists(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage("Book not exists."));
    }
}
