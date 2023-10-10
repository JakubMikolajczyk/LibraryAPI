package com.Library.restAPI.dto.response;

import lombok.Builder;

@Builder
public record DataErrorDto(
        String field,
        String message
){}

