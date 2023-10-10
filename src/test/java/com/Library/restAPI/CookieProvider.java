package com.Library.restAPI;

import com.Library.restAPI.model.Role;
import com.Library.restAPI.model.User;
import com.Library.restAPI.security.JwtService;
import jakarta.servlet.http.Cookie;

import java.util.UUID;

public class CookieProvider {

    private final JwtService jwtService;

    public CookieProvider() {
        this.jwtService = new JwtService();
    }

    public Cookie generateCookie(String username, Long id, Role role){
        return jwtService.generateAccessCookie(User.builder()
                        .id(id)
                        .username(username)
                        .role(role)
                        .build(),
                UUID.randomUUID());
    }

    public Cookie generateCookie(Role role){
        if (role == null){
            return new Cookie("a", "a");
        }
        return jwtService.generateAccessCookie(User.builder()
                        .id(1L)
                        .username("test")
                        .role(role)
                        .build(),
                UUID.randomUUID());
    }

}
