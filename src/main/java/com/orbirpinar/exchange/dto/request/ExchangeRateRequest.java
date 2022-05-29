package com.orbirpinar.exchange.dto.request;

import com.orbirpinar.exchange.validation.CurrencyValidator;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Setter
public class ExchangeRateRequest {

    @NotEmpty
    @NotNull
    @CurrencyValidator
    private String source;

    @NotEmpty
    @NotNull
    @CurrencyValidator
    private String target;

    public String getSource() {
        return source.toUpperCase();
    }

    public String getTarget() {
        return target.toUpperCase();
    }
}
