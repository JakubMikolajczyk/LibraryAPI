package com.Library.restAPI.service;


import com.Library.restAPI.dto.request.LoginRequest;
import com.Library.restAPI.dto.request.PasswordChangeRequest;
import com.Library.restAPI.dto.request.RegisterRequest;
import com.Library.restAPI.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

        User login(LoginRequest loginRequest, HttpServletResponse response);
        void register(RegisterRequest registerRequest);
        void logout(HttpServletRequest request, HttpServletResponse response);
        void logoutAll(User user);
        void changePassword(Long id, PasswordChangeRequest passwordChangeRequest, HttpServletResponse response);
        User refreshToken(HttpServletRequest request, HttpServletResponse response);
}
