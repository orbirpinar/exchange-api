package com.orbirpinar.exchange.client;

import com.orbirpinar.exchange.client.dto.ExchangeRateClientResponseDto;

import java.math.BigDecimal;

public interface ExchangeClient {

    ExchangeRateClientResponseDto getExchangeRate(String to, String from);
    ExchangeRateClientResponseDto convert(String to, String from, BigDecimal amount);
}
