package com.orbirpinar.exchange.controller;

import com.orbirpinar.exchange.controller.params.ConversionParams;
import com.orbirpinar.exchange.dto.request.ExchangeConverterRequest;
import com.orbirpinar.exchange.dto.request.ExchangeRateRequest;
import com.orbirpinar.exchange.dto.response.ExchangeConverterResponse;
import com.orbirpinar.exchange.dto.response.ExchangeRateResponse;
import com.orbirpinar.exchange.exception.ApiError;
import com.orbirpinar.exchange.service.ExchangeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/v1/exchange")
public class ExchangeController {

    private final ExchangeService exchangeService;

    public ExchangeController(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    @Operation(summary = "Get exchange rate from external api")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Getting to exchange rate",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExchangeRateResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "currency is not valid",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "target,source can't be null",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))})
    })
    @PostMapping(value = "/rate")
    public ResponseEntity<?> getExchangeRate(@RequestBody @Valid ExchangeRateRequest request) {
        return ResponseEntity.ok(exchangeService.getExchangeRate(request));
    }

    @Operation(summary = "Convert amount of currency to another currency and persist to database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Getting to target amount",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExchangeConverterResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "currency is not valid",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "target,source and amount can't be null",
                    content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class))})
    })
    @PostMapping(value = "/conversion")
    public ResponseEntity<?> convert(@RequestBody @Valid ExchangeConverterRequest request) {
        return ResponseEntity.ok(exchangeService.convert(request));
    }

    @Operation(summary = "Get all conversions by filters with pagination",
    description =
            "** If no parameter given then all conversion will be listed with pagination <br />" +
            "** If fromDate and toDate parameter given then all conversion will be listed between two dates with pagination <br /> " +
            "** If you want to filter by date then fromDate and toDate both must be specified <br />" +
            "** If pageNumber and pageSize not specified pageSize' s default value 5 and pageNumber' s default value 0")

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of conversion",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ExchangeConverterResponse.class))) }),
            @ApiResponse(responseCode = "400", description = "pageSize and pageNumber can't be negative",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))})
    })
    @GetMapping(value = "/conversion")
    public ResponseEntity<Page<ExchangeConverterResponse>> getAllConversion(
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
           @RequestParam(value = "pageNumber",  defaultValue = "0") Integer pageNumber
    ) {
        return ResponseEntity.ok(exchangeService.getAllConversions(pageNumber, pageSize));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "fromDate and toDate should be valid format see example",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))})
    })
    @GetMapping(value = "/conversion", params = {"fromDate", "toDate"})
    public ResponseEntity<?> getAllConversionsBetweenTransactionDates(
            @ParameterObject @Valid ConversionParams params
    ) {
        return ResponseEntity.ok(exchangeService.getAllConversionBetweenTransactionDates(params));
    }


    @Operation(summary = "Get single conversion by transactionId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get single conversion by transactionId",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExchangeConverterResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Resource Not Found with {transactionId}",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))})
    })
    @GetMapping(value = "/conversion/{transactionId}")
    public ResponseEntity<?> getByConversionsById(@PathVariable String transactionId) {
        return ResponseEntity.ok(exchangeService.getConversionByTransactionId(transactionId));
    }


}
