package com.Library.restAPI.exception;

import com.Library.restAPI.model.SpecimenBorrow;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SpecimenDeleteException extends RuntimeException{

    private final SpecimenBorrow specimenBorrow;
}
