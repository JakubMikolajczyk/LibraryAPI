package com.Library.restAPI.exceptionHandler;

import com.Library.restAPI.exception.BorrowNotFoundException;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class BorrowExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BorrowNotFoundException.class)
    public ResponseEntity<ErrorMessage> borrowNotFound(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage("Borrow not found."));
    }
}
