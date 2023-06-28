package com.Library.restAPI.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record AuthorDto (
        Long id,
        String name,
        String surname
        //Add link to related books?
){}
