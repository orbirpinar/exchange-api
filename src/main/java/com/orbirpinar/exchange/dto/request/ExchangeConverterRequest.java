package com.orbirpinar.exchange.dto.request;

import com.orbirpinar.exchange.validation.CurrencyValidator;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
public class ExchangeConverterRequest {

    @NotNull
    @NotEmpty
    @CurrencyValidator
    private String source;

    @NotNull
    @NotEmpty
    @CurrencyValidator
    private String target;

    @NotNull
    @Positive
    private BigDecimal amount;

    public String getSource() {
        return source.toUpperCase();
    }

    public String getTarget() {
        return target.toUpperCase();
    }


}
