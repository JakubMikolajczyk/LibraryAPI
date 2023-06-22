package com.Library.restAPI.dto;

import com.Library.restAPI.model.Role;

public record UserEditAdminRequest(
        String username,
        String email,
        String name,
        String surname,
        String address,
        String city,
        Role role
) {
}
