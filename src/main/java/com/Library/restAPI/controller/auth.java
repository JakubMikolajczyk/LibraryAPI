package com.Library.restAPI.controller;


import com.Library.restAPI.dto.LoginRequest;
import com.Library.restAPI.dto.RegisterRequest;
import com.Library.restAPI.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class auth {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public void register(
            @RequestBody RegisterRequest request,
            HttpServletResponse response
            ){
        authService.register(request, response);
    }

    @PostMapping("/login")
    public void login(
            @RequestBody LoginRequest request,
            HttpServletResponse response
            ){
        authService.login(request, response);

    }

    @RequestMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response){
        authService.refreshToken(request, response);
    }

}
