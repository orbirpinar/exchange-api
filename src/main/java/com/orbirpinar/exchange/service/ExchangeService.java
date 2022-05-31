package com.orbirpinar.exchange.service;

import com.orbirpinar.exchange.client.ExchangeClient;
import com.orbirpinar.exchange.client.dto.ExchangeRateClientResponseDto;
import com.orbirpinar.exchange.entity.Conversion;
import com.orbirpinar.exchange.exception.NotFoundException;
import com.orbirpinar.exchange.repository.ConversionRepository;
import com.orbirpinar.exchange.dto.request.ExchangeConverterRequest;
import com.orbirpinar.exchange.dto.request.ExchangeRateRequest;
import com.orbirpinar.exchange.dto.response.ExchangeConverterResponse;
import com.orbirpinar.exchange.dto.response.ExchangeRateResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class ExchangeService {

    private final ExchangeClient client;
    private final ConversionRepository conversionRepository;

    public ExchangeService(ExchangeClient client, ConversionRepository exchangeRateHistoryRepository) {
        this.client = client;
        this.conversionRepository = exchangeRateHistoryRepository;
    }

    public ExchangeRateResponse getExchangeRate(ExchangeRateRequest exchangeRateRequest) {
        ExchangeRateClientResponseDto exchangeRate = client.getExchangeRate(exchangeRateRequest.getTarget(), exchangeRateRequest.getSource());
        return new ExchangeRateResponse(exchangeRate.getResult());
    }


    public ExchangeConverterResponse convert(ExchangeConverterRequest exchangeConverterRequest) {
        ExchangeRateClientResponseDto clientResponse = client.convert(exchangeConverterRequest.getTarget(), exchangeConverterRequest.getSource(), exchangeConverterRequest.getAmount());
        Conversion conversion = new Conversion();
        conversion.setSource(exchangeConverterRequest.getSource());
        conversion.setTarget(exchangeConverterRequest.getTarget());
        conversion.setTransactionDate(clientResponse.getDate());
        conversion.setTargetAmount(clientResponse.getResult());
        conversion.setSource(exchangeConverterRequest.getSource());
        conversionRepository.save(conversion);
        return conversion.toResponse();
    }

    public Page<ExchangeConverterResponse> getAllConversions(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Conversion> conversions = conversionRepository.findAll(pageable);
        return conversions.map(Conversion::toResponse);
    }

    public Page<ExchangeConverterResponse> getAllConversionBetweenTransactionDates(LocalDate fromDate, LocalDate toDate,
                                                                                   int pageNumber,
                                                                                   int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Conversion> conversions = conversionRepository
                .findAllByBetweenTransactionDates(fromDate, toDate, pageable);
        return conversions.map(Conversion::toResponse);
    }

    public ExchangeConverterResponse getConversionByTransactionId(String transactionId) {
        Conversion conversion = conversionRepository.findById(transactionId)
                .orElseThrow(() -> new NotFoundException(transactionId));
        return conversion.toResponse();
    }
}
