package com.Library.restAPI.service.impl;

import com.Library.restAPI.dto.request.LoginRequest;
import com.Library.restAPI.dto.request.PasswordChangeRequest;
import com.Library.restAPI.dto.request.RegisterRequest;
import com.Library.restAPI.exception.TokenNotFoundException;
import com.Library.restAPI.exception.UserNotFoundException;
import com.Library.restAPI.exception.WrongPasswordException;
import com.Library.restAPI.exception.WrongUsernameException;
import com.Library.restAPI.model.Token;
import com.Library.restAPI.model.User;
import com.Library.restAPI.repository.TokenRepository;
import com.Library.restAPI.repository.UserRepository;
import com.Library.restAPI.security.jwt.JwtService;
import com.Library.restAPI.service.AuthService;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {


    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User login(LoginRequest loginRequest, HttpServletResponse response) {
        User user = userRepository
                .findByUsername(loginRequest.username())
                .orElseThrow(WrongUsernameException::new);

        boolean match = passwordEncoder.matches(loginRequest.password(), user.getPassword());
        if (!match)
            throw new WrongPasswordException();

        UUID tokenId = UUID.randomUUID();   //Low probability of duplicate
        Cookie refreshCookie = jwtService.generateRefreshCookie(user, tokenId);
        saveUserToken(user, refreshCookie.getValue());
        response.addCookie(refreshCookie);
        response.addCookie(jwtService.generateAccessCookie(user, tokenId));

        return user;

    }

    @Override
    public void register(RegisterRequest registerRequest) {
        User user = User.builder()
                .username(registerRequest.username())
                .password(passwordEncoder.encode(registerRequest.password()))
                .email(registerRequest.email())
                .name(registerRequest.name())
                .surname(registerRequest.surname())
                .address(registerRequest.address())
                .city(registerRequest.city())
                .build();

        userRepository.save(user);
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        getCookieByName(request, "refreshToken")
                .ifPresent(cookie -> tokenRepository.deleteById(jwtService.extractId(cookie.getValue())));
        deleteCookies(response);
    }

    @Override
    public void logoutAll(User user) {
        tokenRepository.deleteAllByUserId(user.getId());
    }

    @Override
    @Transactional
    public void changePassword(Long id, PasswordChangeRequest passwordChangeRequest,
                               HttpServletResponse response) {
        User user = userRepository
                .findById(id)
                .orElseThrow(UserNotFoundException::new);
        boolean match = passwordEncoder.matches(passwordChangeRequest.oldPassword(), user.getPassword());

        if (!match)
            throw new WrongPasswordException();

        user.setPassword(passwordEncoder.encode(passwordChangeRequest.newPassword()));
        User savedUser = userRepository.save(user);
        logoutAll(savedUser);
        deleteCookies(response);
    }

    @Override
    public User refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String jwt = getCookieByName(request, "refreshToken")
                .orElseThrow(TokenNotFoundException::new)
                .getValue();

        Token tokenFromDB = tokenRepository
                .findTokenByIdAndExpireDateGreaterThan(jwtService.extractId(jwt), new Date())
                .orElseThrow(TokenNotFoundException::new);
        response.addCookie(jwtService.generateAccessCookieFromToken(jwt));

        return tokenFromDB.getUser();
    }

    private void saveUserToken(User user, String jwtToken) {
        Token token = Token
                .builder()
                .user(user)
                .expireDate(jwtService.extractExpiration(jwtToken))
                .id(jwtService.extractId(jwtToken))
                .build();

        tokenRepository.save(token);
    }

    private Optional<Cookie> getCookieByName(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null)
            return Optional.empty();

        return Arrays.stream(cookies)
                .filter(cookie1 -> cookie1.getName().equals(name))
                .findFirst();
    }

    private void deleteCookies(HttpServletResponse response) {
        response.addCookie(jwtService.deleteAccessCookie());
        response.addCookie(jwtService.deleteRefreshToken());
    }
}
