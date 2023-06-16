package com.Library.restAPI.dto;

import lombok.Builder;

@Builder
public record LoginRequest(
        String username,
        String password
) { }
