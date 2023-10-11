package com.Library.restAPI.exceptionHandler;

import com.Library.restAPI.exception.TokenNotFoundException;
import com.Library.restAPI.exception.WrongPasswordException;
import com.Library.restAPI.exception.WrongUsernameException;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class AuthExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(TokenNotFoundException.class)
    public ResponseEntity<ErrorMessage> tokenNotFound(){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorMessage("Login requite."));
    }

    @ExceptionHandler(WrongPasswordException.class)
    public ResponseEntity<ErrorMessage> wrongPassword(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage("Wrong password."));
    }

    @ExceptionHandler(WrongUsernameException.class)
    public ResponseEntity<ErrorMessage> wrongUsername(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage("Wrong username."));
    }
}
