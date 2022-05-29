package com.orbirpinar.exchange.client;


import com.orbirpinar.exchange.client.dto.ExchangeClientError;
import com.orbirpinar.exchange.client.dto.ExchangeRateClientResponseDto;
import com.orbirpinar.exchange.dto.response.ExchangeConverterResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExchangeRatesClientTests {


    @Mock
    private RestTemplate restTemplateMock;

    @InjectMocks
    private ExchangeRatesClient sut;

    @Test
    public void convert_shouldReturnResult_WhenGivenDataIsValid() {
        ExchangeRateClientResponseDto responseDto = new ExchangeRateClientResponseDto();
        responseDto.setResult(BigDecimal.valueOf(16.2));
        String to = "USD";
        String from = "TRY";
        BigDecimal amount = BigDecimal.ONE;
        when(restTemplateMock.getForObject(anyString(), eq(ExchangeRateClientResponseDto.class)))
                .thenReturn(responseDto);
        ExchangeRateClientResponseDto convert = sut.convert(to, from, amount);

        assertEquals(BigDecimal.valueOf(16.2),convert.getResult());
    }

    @Test
    public void getExchangeRate_shouldReturnResult_WhenGivenDataIsValid() {
        ExchangeRateClientResponseDto responseDto = new ExchangeRateClientResponseDto();
        responseDto.setResult(BigDecimal.valueOf(16.2));
        String to = "USD";
        String from = "TRY";
        when(restTemplateMock.getForObject(anyString(), eq(ExchangeRateClientResponseDto.class)))
                .thenReturn(responseDto);
        ExchangeRateClientResponseDto convert = sut.getExchangeRate(to, from);

        assertEquals(BigDecimal.valueOf(16.2),convert.getResult());
    }

    @Test
    public void getExchangeRate_shouldReturnErrorResponse_WhenGivenDataIsNotValid() {
        ExchangeClientError exchangeClientError = new ExchangeClientError();
        exchangeClientError.setCode("unknown_error");
        exchangeClientError.setMessage("Some message");
        ExchangeRateClientResponseDto responseDto = new ExchangeRateClientResponseDto();
        responseDto.setError(exchangeClientError);
        String to = "US";
        String from = "TR";
        when(restTemplateMock.getForObject(anyString(), eq(ExchangeRateClientResponseDto.class)))
                .thenReturn(responseDto);
        ExchangeRateClientResponseDto convert = sut.getExchangeRate(to, from);

        assertNotNull(convert.getError());
        assertEquals("unknown_error",convert.getError().getCode());
    }
}
