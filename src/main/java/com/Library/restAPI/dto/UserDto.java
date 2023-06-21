package com.Library.restAPI.dto;

import com.Library.restAPI.model.Role;
import lombok.Builder;

@Builder
public record UserDto(
        Long id,
        String username,
        String email,
        String name,
        String surname,
        String address,
        String city,
        Role role
) {
}
