package com.orbirpinar.exchange.validation;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;

public class CurrencyValidatorHandlerTests {


    private final CurrencyValidatorHandler sut = new CurrencyValidatorHandler();
    @Test
    public void isValid_shouldReturnTrue_WhenCurrencyIsValid() {
        boolean result = sut.isValid("USD", null);
        Assertions.assertTrue(result);
    }

    @Test
    public void isValid_shouldReturnFalse_WhenCurrencyIsNotValid() {
        boolean result = sut.isValid("US", null);
        Assertions.assertFalse(result);
    }
}
