package com.Library.restAPI.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SurnameValidator implements ConstraintValidator<Surname, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

        if(s == null)
            return false;

        Pattern pattern = Pattern.compile("(^[A-Z][a-z]*$)|(^[A-Z][a-z]*-[A-Z][a-z]*$)");
        Matcher matcher = pattern.matcher(s);
        return matcher.matches();
    }
}
