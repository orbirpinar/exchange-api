package com.orbirpinar.exchange.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@AllArgsConstructor
public class ExchangeConverterResponse {

    @JsonProperty("transaction_id")
    private String transactionId;

    private BigDecimal result;

}
