package com.Library.restAPI.dto;

import lombok.Builder;

@Builder
public record UserDto(
        String username,
        String email,
        String name,
        String surname,
        String address,
        String city
) {}
