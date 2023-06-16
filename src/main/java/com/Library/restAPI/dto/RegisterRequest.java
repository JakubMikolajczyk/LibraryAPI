package com.Library.restAPI.dto;


public record RegisterRequest(
        String username,
        String email,
        String name,
        String surname,
        String address,
        String city,
        String password
){ }
