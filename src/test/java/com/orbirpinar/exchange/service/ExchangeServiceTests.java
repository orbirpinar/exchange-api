package com.orbirpinar.exchange.service;

import com.orbirpinar.exchange.client.ExchangeClient;
import com.orbirpinar.exchange.client.dto.ExchangeRateClientResponseDto;
import com.orbirpinar.exchange.dto.request.ExchangeConverterRequest;
import com.orbirpinar.exchange.dto.request.ExchangeRateRequest;
import com.orbirpinar.exchange.dto.response.ExchangeConverterResponse;
import com.orbirpinar.exchange.dto.response.ExchangeRateResponse;
import com.orbirpinar.exchange.entity.Conversion;
import com.orbirpinar.exchange.repository.ConversionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
public class ExchangeServiceTests {

    @Mock
    private ConversionRepository conversionRepositoryMock;

    @Mock
    private ExchangeClient exchangeClientMock;

    @InjectMocks
    private ExchangeService sut;


    @Test
    public void getExchangeRate_shouldReturnResult_WhenGivenRequestIsValid() {
        // Arrange
        ExchangeRateRequest request = new ExchangeRateRequest();
        request.setSource("USD");
        request.setTarget("TRY");
        BigDecimal expectedResult = BigDecimal.valueOf(16);
        ExchangeRateClientResponseDto response = new ExchangeRateClientResponseDto();
        response.setResult(expectedResult);
        when(exchangeClientMock.getExchangeRate(any(),any()))
                .thenReturn(response);

        // Act
        ExchangeRateResponse exchangeRate = sut.getExchangeRate(request);
        BigDecimal actualResult = exchangeRate.getExchangeRate();

        // Assert
        assertEquals(expectedResult,actualResult);
    }

    @Test
    public void convert_shouldReturnAmount_WhenGivenResultIsValid() {
        // Arrange
        ExchangeConverterRequest converterRequest = new ExchangeConverterRequest();
        BigDecimal amount = BigDecimal.valueOf(10);
        String target = "EUR";
        String source = "USD";
        converterRequest.setAmount(amount);
        converterRequest.setTarget(target);
        converterRequest.setSource(source);
        BigDecimal expectedResult = BigDecimal.valueOf(9.8);
        ExchangeRateClientResponseDto clientResponseDto = new ExchangeRateClientResponseDto();
        clientResponseDto.setResult(expectedResult);
        when(exchangeClientMock.convert(any(),any(),any()))
                .thenReturn(clientResponseDto);
        when(conversionRepositoryMock.save(any())).thenReturn(new Conversion());

        // Act
        ExchangeConverterResponse convert = sut.convert(converterRequest);
        BigDecimal actualResult = convert.getResult();

        // Assert
        assertEquals(expectedResult,actualResult);
    }

    @Test
    public void getAllConversions_shouldReturnListOfConversionsWithPagination_WhenCalled() {
        // Arrange
        Conversion conversion = getConversion();
        BigDecimal targetAmount = BigDecimal.valueOf(32.44);
        Page<Conversion> expectedConversions = new PageImpl<>(List.of(conversion));
        when(conversionRepositoryMock.findAll(any(Pageable.class))).thenReturn(expectedConversions);

        // Act
        Page<ExchangeConverterResponse> allConversions = sut.getAllConversions(0,1);

        // Assert
        assertEquals(1,allConversions.getContent().size());
        assertEquals(targetAmount,allConversions.getContent().get(0).getResult());
    }



    @Test
    public void getAllConversionsBetweenTransactionDates_shouldReturnListOfConversionsByFilter_WhenDatesAreValid() {
        // Arrange
        Conversion conversion = getConversion();
        when(conversionRepositoryMock.findAllByBetweenTransactionDates(
               any(),any(),any(Pageable.class)
        )).thenReturn(new PageImpl<>((List.of(conversion))));

        // Act
        Page<ExchangeConverterResponse> conversions = sut.getAllConversionBetweenTransactionDates(LocalDate.now(), LocalDate.now(),0,5);

        // Assert
        assertEquals(1,conversions.getContent().size());
    }

    @Test
    public void getConversionByTransactionId_shouldReturnOptionalResponse_WhenCalledWithExistingId() {


        // Arrange
        when(conversionRepositoryMock.findById(any())).thenReturn(Optional.of(getConversion()));

        // Act
        Optional<ExchangeConverterResponse> conversionByTransactionId = sut.getConversionByTransactionId(UUID.randomUUID().toString());

        // Assert
        assertTrue(conversionByTransactionId.isPresent());
        assertEquals(BigDecimal.valueOf(32.44),conversionByTransactionId.get().getResult());
    }

    @Test
    public void getConversionByTransactionId_shouldReturnEmpty_WhenCalledWithNotExistingId() {
        // Arrange
        when(conversionRepositoryMock.findById(any())).thenReturn(Optional.empty());

        // Act
        Optional<ExchangeConverterResponse> conversionByTransactionId = sut.getConversionByTransactionId(UUID.randomUUID().toString());

        // Assert
        assertTrue(conversionByTransactionId.isEmpty());
    }

    private Conversion getConversion() {
        Conversion conversion = new Conversion();
        conversion.setTransactionId(UUID.randomUUID().toString());
        conversion.setSource("USD");
        conversion.setTarget("TRY");
        conversion.setExchangeRate(BigDecimal.valueOf(16.22));
        conversion.setTargetAmount(BigDecimal.valueOf(32.44));
        conversion.setTransactionDate(LocalDate.now());
        return conversion;
    }

}
