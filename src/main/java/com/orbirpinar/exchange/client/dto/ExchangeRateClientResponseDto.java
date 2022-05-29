package com.orbirpinar.exchange.client.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ExchangeRateClientResponseDto {

    private LocalDate date;

    private BigDecimal result;

    private boolean success;

    private ExchangeClientError error;

    String message;

}

