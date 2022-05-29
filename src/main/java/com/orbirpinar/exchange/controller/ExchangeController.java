package com.orbirpinar.exchange.controller;

import com.orbirpinar.exchange.dto.request.ExchangeConverterRequest;
import com.orbirpinar.exchange.dto.request.ExchangeRateRequest;
import com.orbirpinar.exchange.service.ExchangeService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.time.LocalDate;


@RestController
@RequestMapping("/api/v1/exchange")
public class ExchangeController {

    private final ExchangeService exchangeService;

    public ExchangeController(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    @PostMapping(value = "/rate")
    public ResponseEntity<?> getExchangeRate(@RequestBody @Valid ExchangeRateRequest request) {
        return ResponseEntity.ok(exchangeService.getExchangeRate(request));
    }

    @PostMapping(value = "/conversion")
    public ResponseEntity<?> convert(@RequestBody @Valid ExchangeConverterRequest request) {
        return ResponseEntity.ok(exchangeService.convert(request));
    }

    @GetMapping(value = "/conversion")
    public ResponseEntity<?> getAllConversion(
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
           @RequestParam(value = "pageNumber",  defaultValue = "0") Integer pageNumber
    ) {
        return ResponseEntity.ok(exchangeService.getAllConversions(pageNumber, pageSize));
    }

    @GetMapping(value = "/conversion", params = {"fromDate", "toDate"})
    public ResponseEntity<?> getAllConversionsBetweenTransactionDates(
            @RequestParam(value = "fromDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(value = "toDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @Positive @RequestParam(value = "pageSize",  defaultValue = "5") Integer pageSize,
            @Positive @RequestParam(value = "pageNumber",  defaultValue = "0") Integer pageNumber
    ) {
        return ResponseEntity.ok(exchangeService.getAllConversionBetweenTransactionDates(fromDate, toDate,
                pageNumber, pageSize));
    }

    @GetMapping(value = "/conversion", params = {"transactionId"})
    public ResponseEntity<?> getAllConversionsById(
            @RequestParam(value = "transactionId", required = false) String transactionId
    ) {
        return ResponseEntity.ok(exchangeService.getConversionByTransactionId(transactionId));
    }


}
