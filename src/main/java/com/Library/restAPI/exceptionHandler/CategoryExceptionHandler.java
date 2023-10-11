package com.Library.restAPI.exceptionHandler;

import com.Library.restAPI.exception.CategoryNotFoundException;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CategoryExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorMessage> categoryNotFound(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage("Category not found."));
    }
}
