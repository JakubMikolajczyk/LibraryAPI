package com.Library.restAPI.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GenreTest {

    private final Validator validator;

    public GenreTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @ParameterizedTest
    @MethodSource("com.Library.restAPI.StringsProvider#okWithoutNumber")
    void genreNameSuccess(String name){
        //given
        Genre genre = Genre.builder()
                .name(name)
                .build();
        //when
        Set<ConstraintViolation<Genre>> violationSet = validator.validate(genre);
        //then
        assertTrue(violationSet.isEmpty());

    }

    @ParameterizedTest
    @NullSource
    @MethodSource("com.Library.restAPI.StringsProvider#wrongWithoutNumber")
    void genreWrongNameFail(String name){
        //given
        Genre genre = Genre.builder()
                .name(name)
                .build();
        //when
        Set<ConstraintViolation<Genre>> violationSet = validator.validate(genre);
        //then
        assertFalse(violationSet.isEmpty());
        assertEquals(1, violationSet.size());
    }
}