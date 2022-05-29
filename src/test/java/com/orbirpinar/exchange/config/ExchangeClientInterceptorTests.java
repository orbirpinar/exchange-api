package com.orbirpinar.exchange.config;

import com.orbirpinar.exchange.exception.ExchangeClientException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class ExchangeClientInterceptorTests {

    @Mock
    private ClientHttpRequestExecution mockClientHttpRequestExecution;

    @Mock
    private ClientHttpResponse mockClientHttpResponse;

    @Mock
    private HttpRequest mockHttpRequest;
    private final ExchangeClientInterceptor sut = new ExchangeClientInterceptor();

    public ExchangeClientInterceptorTests() {
    }


    @Test
    public void intercept_shouldSetHeader_WhenCalled() throws IOException {
        // Arrange
        final ClientHttpResponse actualResult;
        final byte[] inputBody = null;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("accept", "application/json");
        httpHeaders.add("content-type", "application/json");
        httpHeaders.add("apikey", "API KEY");
        when(mockHttpRequest.getHeaders()).thenReturn(httpHeaders);
        when(mockClientHttpResponse.getStatusCode()).thenReturn(HttpStatus.OK);
        when(mockClientHttpRequestExecution.execute(mockHttpRequest,inputBody)).thenReturn(mockClientHttpResponse);


        // Act
        actualResult = sut.intercept(mockHttpRequest, inputBody, mockClientHttpRequestExecution);


        // Assert
        assertSame(mockClientHttpResponse, actualResult);
        assertEquals(mockClientHttpResponse.getStatusCode(),HttpStatus.OK);
        assertNotSame(mockHttpRequest.getHeaders().getOrEmpty("apiKey").size(),0);
    }

    @Test
    public void intercept_shouldThrowException_WhenTooManyRequested() throws IOException {
        // Arrange
        final byte[] inputBody = null;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("accept", "application/json");
        httpHeaders.add("content-type", "application/json");
        httpHeaders.add("apikey", "API KEY");
        when(mockHttpRequest.getHeaders()).thenReturn(httpHeaders);
        when(mockClientHttpResponse.getStatusCode()).thenReturn(HttpStatus.TOO_MANY_REQUESTS);
        when(mockClientHttpRequestExecution.execute(mockHttpRequest,inputBody)).thenReturn(mockClientHttpResponse);

        // Act and assert
        assertThrows(ExchangeClientException.class,() -> {
            sut.intercept(mockHttpRequest, inputBody, mockClientHttpRequestExecution);
        });
        assertEquals(mockClientHttpResponse.getStatusCode(),HttpStatus.TOO_MANY_REQUESTS);
    }
}
