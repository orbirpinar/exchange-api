package com.orbirpinar.exchange.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orbirpinar.exchange.client.dto.ExchangeRateClientResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class ExchangeClientErrorHandler extends DefaultResponseErrorHandler {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR ||
                response.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR;
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(response.getBody()))) {
            if(response.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR) {
                throw new ExchangeClientException("Server Error!!","500");
            }
            String httpBodyResponse = reader.lines().collect(Collectors.joining(" "));
            ExchangeRateClientResponseDto exchangeRateClientResponseDto = mapper.readValue(httpBodyResponse, ExchangeRateClientResponseDto.class);
            if(exchangeRateClientResponseDto.getError() != null) {
                throw new ExchangeClientException(exchangeRateClientResponseDto.getError().getMessage(),
                        exchangeRateClientResponseDto.getError().getCode());
            }
            if(exchangeRateClientResponseDto.getMessage() != null) {
                throw new ExchangeClientException(exchangeRateClientResponseDto.getMessage(),"api_key_error");
            }
            throw new ExchangeClientException("Unknown error","unknown_error");
        }
    }

}
