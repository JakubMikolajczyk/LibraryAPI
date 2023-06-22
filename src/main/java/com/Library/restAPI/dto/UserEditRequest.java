package com.Library.restAPI.dto;

public record UserEditRequest (
    String email,
    String name,
    String surname,
    String address,
    String city
){}
