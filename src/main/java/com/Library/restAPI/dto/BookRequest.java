package com.Library.restAPI.dto;

import com.Library.restAPI.model.Category;

import java.util.Date;
import java.util.List;

public record BookRequest (
        String tittle,
        String ISBN,
        int year,
        Date deleteDate,
        Long authorId,
        List<Long> categoriesId
){}
