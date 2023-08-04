package com.Library.restAPI.controller;


import com.Library.restAPI.dto.request.LoginRequest;
import com.Library.restAPI.dto.request.RegisterRequest;
import com.Library.restAPI.dto.response.UserDto;
import com.Library.restAPI.mapper.UserMapper;
import com.Library.restAPI.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final UserMapper userMapper;

    @PostMapping("/register")
    public void register(@RequestBody RegisterRequest request){
        authService.register(request);
    }

    @PostMapping("/login")
    public UserDto login(@RequestBody LoginRequest request, HttpServletResponse response){
        return userMapper.toDto(authService.login(request, response));

    }

    @GetMapping("/refresh-token")
    public UserDto refreshToken(HttpServletRequest request, HttpServletResponse response){
        return userMapper.toDto(authService.refreshToken(request, response));
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response){
        authService.logout(request, response);
    }
}
