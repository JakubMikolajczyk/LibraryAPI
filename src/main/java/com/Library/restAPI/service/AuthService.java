package com.Library.restAPI.service;


import com.Library.restAPI.dto.LoginRequest;
import com.Library.restAPI.dto.PasswordChangeRequest;
import com.Library.restAPI.dto.RegisterRequest;
import com.Library.restAPI.dto.UserDto;
import com.Library.restAPI.model.User;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.ResponseStatus;

public interface AuthService {

        User login(LoginRequest loginRequest, HttpServletResponse response);
        User register(RegisterRequest registerRequest, HttpServletResponse response);
        void logoutAll(User user);
        void changePassword(String username, PasswordChangeRequest passwordChangeRequest, HttpServletResponse response);
        User refreshToken(HttpServletRequest request, HttpServletResponse response);
}
