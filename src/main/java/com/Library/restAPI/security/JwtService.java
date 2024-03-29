package com.Library.restAPI.security;

import com.Library.restAPI.model.Role;
import com.Library.restAPI.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@RequiredArgsConstructor
@Service
public class JwtService {

    @Value("${library.app.jwtSecret}")
    private String SECRET_KEY = "5795D935271DB3C51CEC6DB51922B5795D935271DB3C51CEC6DB51922B";

    @Value("${library.app.jwtExpirationMsRefresh}")
    private int EXPIRATION_MS_REFRESH = 1000 * 60 * 24;

    @Value("${library.app.jwtExpirationMsAccess}")
    private int EXPIRATION_MS_ACCESS = 1000 * 60 * 5;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public UUID extractId(String token) {
        return UUID.fromString(extractClaim(token, Claims::getId));
    }

    public Long extractUserId(String token){
        Claims claims = extractAllClaims(token);
        return ((Integer) claims.get("userId")).longValue();
    }

    public Collection<? extends GrantedAuthority> extractAuthority(String token) {
        Claims claims = extractAllClaims(token);
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + (String) claims.get("role")));
    }

    public Role extractRole(String token){
        return Role.valueOf((String) extractAllClaims(token).get("role"));
    }
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }
        catch (ExpiredJwtException ex){
            return ex.getClaims();
        }
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String generateRefreshToken(User user, UUID tokenId){
        return generateToken(user, tokenId, EXPIRATION_MS_REFRESH);
    }
    private String generateAccessToken(User user, UUID tokenId){
        return generateToken(user, tokenId, EXPIRATION_MS_ACCESS);
    }


    private String generateToken(User user, UUID tokenId, int expiration) {
        return Jwts.builder()
                .claim("role", user.getRole())
                .claim("userId", user.getId())
                .setId(tokenId.toString())
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    public Cookie generateAccessCookie(User user, UUID tokenId){
        Cookie cookie = new Cookie("accessToken", generateAccessToken(user, tokenId));
//        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/api/v1/");
        cookie.setMaxAge(EXPIRATION_MS_ACCESS/1000); //1000 because it is in seconds not in ms
        return cookie;
    }

    public Cookie generateRefreshCookie(User user, UUID tokenId){
        Cookie cookie = new Cookie("refreshToken", generateRefreshToken(user, tokenId));
//        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(EXPIRATION_MS_REFRESH/1000);
        cookie.setPath("/api/v1/auth/refresh-token");
        return cookie;
    }

    public Cookie deleteAccessCookie(){
        Cookie cookie = new Cookie("accessToken", null);
        //        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/api/v1/");
        cookie.setMaxAge(0);
        return cookie;
    }

    public Cookie deleteRefreshToken(){
        Cookie cookie = new Cookie("refreshToken", null);
        //        cookie.setSecure(true);
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setPath("/api/v1/auth/refresh-token");
        return cookie;
    }

    public Cookie generateAccessCookieFromToken(String token){

        return generateAccessCookie(User.builder()
                .username(extractUsername(token))
                .id(extractUserId(token))
                .role(extractRole(token))
                .build(), extractId(token));
    }
    public boolean isExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    public boolean isAccessTokenValid(String token){
        return !isExpired(token);
    }

    public Optional<Cookie> getCookieByName(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null)
            return Optional.empty();

        return Arrays.stream(cookies)
                .filter(cookie1 -> cookie1.getName().equals(name))
                .findFirst();
    }
}