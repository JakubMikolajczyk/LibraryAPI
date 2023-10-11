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

class AuthorTest {

    private final Validator validator;

    public AuthorTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @ParameterizedTest
    @MethodSource("com.Library.restAPI.StringsProvider#okNames")
    void authorNameSuccess(String name){
        //given
        Author author = Author.builder()
                .name(name)
                .surname("Test").build();
        //when
        Set<ConstraintViolation<Author>> violationSet = validator.validate(author);
        //then
        assertTrue(violationSet.isEmpty());
    }

    @ParameterizedTest
    @NullSource
    @MethodSource("com.Library.restAPI.StringsProvider#wrongNames")
    void authorWrongNameFail(String name){
        //given
        Author author = Author.builder()
                .name(name)
                .surname("Test")
                .build();
        //when
        Set<ConstraintViolation<Author>> violationSet = validator.validate(author);
        //then
        assertFalse(violationSet.isEmpty());
        assertEquals(1, violationSet.size());
    }

    @ParameterizedTest
    @MethodSource("com.Library.restAPI.StringsProvider#okSurnames")
    void authorSurnameSuccess(String surname){
        //given
        Author author = Author.builder()
                .name("Test")
                .surname(surname).build();
        //when
        Set<ConstraintViolation<Author>> violationSet = validator.validate(author);
        //then
        assertTrue(violationSet.isEmpty());
    }

    @ParameterizedTest
    @NullSource
    @MethodSource("com.Library.restAPI.StringsProvider#wrongSurnames")
    void authorWrongSurnameFail(String surname){
        //given
        Author author = Author.builder()
                .name("Test")
                .surname(surname)
                .build();
        //when
        Set<ConstraintViolation<Author>> violationSet = validator.validate(author);
        //then
        assertFalse(violationSet.isEmpty());
        assertEquals(1, violationSet.size());

    }
}