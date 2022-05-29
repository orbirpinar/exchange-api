package com.orbirpinar.exchange.dto.request;

import com.orbirpinar.exchange.validation.CurrencyValidator;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Getter
@Setter
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
