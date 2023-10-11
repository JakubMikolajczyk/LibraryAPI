package com.Library.restAPI.exceptionHandler;

import com.Library.restAPI.exception.BorrowHistoryNotFoundException;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class BorrowHistoryExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BorrowHistoryNotFoundException.class)
    public ResponseEntity<ErrorMessage> historyNotFound(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage("Borrow history not found."));
    }
}
