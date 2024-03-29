package com.Library.restAPI.exception;

import com.Library.restAPI.model.SpecimenBorrow;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BorrowedException extends RuntimeException{

    private final SpecimenBorrow specimenBorrow;

}
