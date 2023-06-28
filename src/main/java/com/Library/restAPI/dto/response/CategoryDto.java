package com.Library.restAPI.dto.response;

import com.Library.restAPI.dto.response.Link;
import lombok.Builder;

import java.util.List;

@Builder
public record CategoryDto (
        Long id,
        String name,
        List<Link> books
){}
