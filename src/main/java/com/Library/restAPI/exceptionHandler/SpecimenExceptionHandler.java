package com.Library.restAPI.exceptionHandler;

import com.Library.restAPI.dto.response.Link;
import com.Library.restAPI.exception.SpecimenDeleteException;
import com.Library.restAPI.exception.SpecimenNotFoundException;
import com.Library.restAPI.mapper.LinkMapper;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class SpecimenExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(SpecimenNotFoundException.class)
    public ResponseEntity<ErrorMessage> specimenNotFound(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage("Specimen not found."));
    }

    @ExceptionHandler(SpecimenDeleteException.class)
    public ResponseEntity<Link> deleteBorrowed(SpecimenDeleteException exception){
        Link userLink = LinkMapper.toLink(exception.getSpecimenBorrow().getUser());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userLink);
    }
}
