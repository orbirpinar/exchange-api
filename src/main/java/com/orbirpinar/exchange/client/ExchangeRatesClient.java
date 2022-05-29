package com.orbirpinar.exchange.client;

import com.orbirpinar.exchange.client.dto.ExchangeRateClientResponseDto;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Component
public class ExchangeRatesClient implements ExchangeClient{

    private final RestTemplate restTemplate;

    public ExchangeRatesClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ExchangeRateClientResponseDto getExchangeRate(String to, String from) {
        return convert(to,from,BigDecimal.ONE);
    }

    public ExchangeRateClientResponseDto convert(String to, String from, BigDecimal amount) {
        return restTemplate.getForObject("/convert?to=" + to + "&from=" + from + "&amount=" + amount,
                ExchangeRateClientResponseDto.class);
    }

}
