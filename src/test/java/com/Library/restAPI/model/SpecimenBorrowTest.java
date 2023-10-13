package com.Library.restAPI.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SpecimenBorrowTest {

    private final Validator validator;

    public SpecimenBorrowTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void specimenBorrowSuccess(){
        //given
        SpecimenBorrow specimenBorrow = SpecimenBorrow.builder()
                .book(new Book())
                .build();
        //when
        Set<ConstraintViolation<SpecimenBorrow>> violationSet = validator.validate(specimenBorrow);
        //then
        assertTrue(violationSet.isEmpty());
    }

    @Test
    void specimenBorrowDefaultCreate(){
        //given
        SpecimenBorrow specimenBorrow = new SpecimenBorrow();
        //when
        //then
        assertNull(specimenBorrow.getUser());
        assertNull(specimenBorrow.getStartTime());
    }

    @Test
    void specimenBorrowNullBookFail(){
        //given
        SpecimenBorrow specimenBorrow = SpecimenBorrow.builder()
                .book(null)
                .build();
        //when
        Set<ConstraintViolation<SpecimenBorrow>> violationSet = validator.validate(specimenBorrow);
        //then
        assertFalse(violationSet.isEmpty());
        assertEquals(1, violationSet.size());
    }

}