package com.orbirpinar.exchange.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CurrencyValidatorHandler.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@NotNull
public @interface CurrencyValidator {

    String message() default "Must be currency code!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}