package com.orbirpinar.exchange.validation;

import com.orbirpinar.exchange.util.CurrencyCode;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CurrencyValidatorHandler implements ConstraintValidator<CurrencyValidator,String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
            return CurrencyCode.contains(value);
    }
}
