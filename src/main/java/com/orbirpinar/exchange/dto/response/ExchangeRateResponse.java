package com.orbirpinar.exchange.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class ExchangeRateResponse {

    @JsonProperty("exchange_rate")
    private BigDecimal exchangeRate;
}
