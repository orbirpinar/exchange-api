package com.orbirpinar.exchange.config;


import com.orbirpinar.exchange.exception.ExchangeClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class ExchangeClientInterceptor implements ClientHttpRequestInterceptor {


    @Value("${exchange.client.apiKey}")
    private String API_KEY;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {
        request.getHeaders().add("accept", "application/json");
        request.getHeaders().add("content-type", "application/json");
        request.getHeaders().add("apikey", API_KEY);


        ClientHttpResponse response = execution.execute(request, body);
        if (response.getStatusCode().equals(HttpStatus.TOO_MANY_REQUESTS)) {
            log.error("Too many request 429");
            throw new ExchangeClientException("Too many request","too_many_request");
        }


        return response;
    }
}
