package com.orbirpinar.exchange.controller;

import com.orbirpinar.exchange.aspect.LogAspect;
import com.orbirpinar.exchange.controller.params.ConversionParams;
import com.orbirpinar.exchange.dto.request.ExchangeConverterRequest;
import com.orbirpinar.exchange.dto.request.ExchangeRateRequest;
import com.orbirpinar.exchange.dto.response.ExchangeConverterResponse;
import com.orbirpinar.exchange.dto.response.ExchangeRateResponse;
import com.orbirpinar.exchange.exception.NotFoundException;
import com.orbirpinar.exchange.service.ExchangeService;
import com.orbirpinar.exchange.util.ApiErrorCode;
import org.h2.api.ErrorCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ExchangeController.class)
@AutoConfigureJsonTesters
@Import({AopAutoConfiguration.class, LogAspect.class})
public class ExchangeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JacksonTester<ExchangeConverterResponse> jsonExchangeConverterResponse;
    @Autowired
    private JacksonTester<ExchangeRateResponse> jsonExchangeRateResponse;

    @Autowired
    private JacksonTester<ExchangeRateRequest> jsonExchangeRateRequest;

    @Autowired
    private JacksonTester<ExchangeConverterRequest> jsonExchangeConverterRequest;

    @MockBean
    private ExchangeService exchangeService;


    @Test
    public void getExchangeRate_shouldReturnStatusCodeOK_WhenGivenSymbolsAreValid() throws Exception {

        // Arrange
        ExchangeRateRequest exchangeRateRequest = new ExchangeRateRequest();
        exchangeRateRequest.setSource("USD");
        exchangeRateRequest.setTarget("TRY");
        when(exchangeService.getExchangeRate(any())).thenReturn(new ExchangeRateResponse(BigDecimal.TEN));

        // Act
        MockHttpServletResponse response = mockMvc.perform(
                post("/api/v1/exchange/rate")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonExchangeRateRequest.write(exchangeRateRequest).getJson())
        ).andReturn().getResponse();
        String expected = jsonExchangeRateResponse.write(new ExchangeRateResponse(BigDecimal.TEN)).getJson();

        // Assert
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(expected,response.getContentAsString());
    }

    @Test
    public void getExchangeRate_shouldReturnBadRequest_WhenGivenSymbolsAreNotValid() throws Exception {

        // Arrange
        ExchangeRateRequest exchangeRateRequest = new ExchangeRateRequest();
        exchangeRateRequest.setSource("US");
        exchangeRateRequest.setTarget("TR");
        when(exchangeService.getExchangeRate(any())).thenReturn(null);

        // Act
        MockHttpServletResponse response = mockMvc.perform(
                post("/api/v1/exchange/rate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonExchangeRateRequest.write(exchangeRateRequest).getJson())
        ).andExpect(jsonPath("$.errorCode").value(ApiErrorCode.VALIDATION_ERROR.getCode()))
                .andReturn().getResponse();

        // Assert
        assertEquals(response.getStatus(),HttpStatus.BAD_REQUEST.value());
    }


    @Test
    public void convert_shouldReturnStatusCodeOK_WhenGivenSymbolsAndAmountAreValid() throws Exception {

        // Arrange
        ExchangeConverterRequest request = new ExchangeConverterRequest();
        request.setSource("USD");
        request.setTarget("TRY");
        request.setAmount(BigDecimal.valueOf(10));
        when(exchangeService.convert(any()))
                .thenReturn(new ExchangeConverterResponse(UUID.randomUUID().toString(),BigDecimal.TEN));

        // Act and Assert
        MockHttpServletResponse response = mockMvc.perform(post("/api/v1/exchange/conversion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonExchangeConverterRequest.write(request).getJson())
                ).andExpect(jsonPath("$.result").value(BigDecimal.TEN))
                .andReturn().getResponse();
        assertEquals(response.getStatus(),HttpStatus.OK.value());
    }



    @Test
    public void convert_shouldReturn400BadRequest_WhenGivenSymbolsAreNotValid() throws Exception {

        // Arrange
        ExchangeConverterRequest request = new ExchangeConverterRequest();
        request.setSource("US");
        request.setTarget("TR");
        request.setAmount(BigDecimal.valueOf(10));
        when(exchangeService.convert(any())).thenReturn(null);

        // Act and Assert
        mockMvc.perform(post("/api/v1/exchange/conversion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonExchangeConverterRequest.write(request).getJson()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(ApiErrorCode.VALIDATION_ERROR.getCode()));
    }

    @Test
    public void getAllConversion_shouldReturnListOfConversion_WhenPageParametersGiven() throws Exception {

        // Arrange
        Page<ExchangeConverterResponse> converterResponseList = new PageImpl<>(
                List.of(
                        new ExchangeConverterResponse(UUID.randomUUID().toString(), BigDecimal.TEN),
                        new ExchangeConverterResponse(UUID.randomUUID().toString(), BigDecimal.ONE)
                )
        );
        when(exchangeService.getAllConversions(anyInt(),anyInt())).thenReturn(converterResponseList);

        // Act and Assert
        mockMvc.perform(get("/api/v1/exchange/conversion")
                        .param("pageNumber","0")
                        .param("pageSize","1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.content[*].result",containsInAnyOrder(10,1)));

    }

    @Test
    public void getAllConversionsBetweenTransactionDates_shouldReturnListOfConversionsBetweenDates_WhenDatesAreValid() throws Exception {

        // Arrange
        Page<ExchangeConverterResponse> converterResponseList = new PageImpl<>(
                List
                        .of(new ExchangeConverterResponse(UUID.randomUUID().toString(),BigDecimal.TEN),
                                new ExchangeConverterResponse(UUID.randomUUID().toString(),BigDecimal.ONE))
        );
        when(exchangeService.getAllConversionBetweenTransactionDates(any(ConversionParams.class))).thenReturn(converterResponseList);

        // Act and assert
        mockMvc.perform(get("/api/v1/exchange/conversion")
                .param("fromDate", LocalDate.now().toString())
                .param("toDate",LocalDate.now().toString())
                        .param("pageNumber","0")
                        .param("pageSize","2")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.content[*].result",containsInAnyOrder(10,1)))
                .andExpect(jsonPath("$.totalPages").value("1"))
                .andExpect(jsonPath("$.number").value("0"));
    }

    @Test
    public void getAllConversionsBetweenTransactionDates_shouldReturnBadRequest_WhenDatesAreNotValidFormat() throws Exception {

        // Arrange
        when(exchangeService.getAllConversionBetweenTransactionDates(any(ConversionParams.class)))
                .thenThrow(IllegalArgumentException.class);

        // Act and assert
        mockMvc.perform(get("/api/v1/exchange/conversion")
                .param("fromDate", "01-06-2022")
                .param("toDate", LocalDate.now().toString())
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void getAllConversionsBetweenTransactionDates_shouldReturnBadRequest_WhenFromDateLaterThanToDate() throws Exception {

        // Arrange
        when(exchangeService.getAllConversionBetweenTransactionDates(any(ConversionParams.class)))
                .thenThrow(IllegalArgumentException.class);

        // Act and assert
        mockMvc.perform(get("/api/v1/exchange/conversion")
                .param("fromDate","2022-06-02" )
                .param("toDate", "2022-06-01")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(ApiErrorCode.VALIDATION_ERROR.getCode()));
    }


    @Test
    public void getConversionByTransactionId_shouldReturnSingleConversion_WhenIdExists() throws Exception {

        // Arrange
        ExchangeConverterResponse exchangeConverterResponse = new ExchangeConverterResponse(UUID.randomUUID().toString(), BigDecimal.TEN);
        when(exchangeService.getConversionByTransactionId(any()))
                .thenReturn(exchangeConverterResponse);

        // Act and Assert
        mockMvc.perform(get("/api/v1/exchange/conversion/" + UUID.randomUUID())
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().is2xxSuccessful())
                        .andExpect(jsonPath("$.result").value(10));

    }
    @Test
    public void getConversionByTransactionId_shouldReturnError_WhenGivenIdNotExists() throws Exception {

        // Arrange
        when(exchangeService.getConversionByTransactionId(any())).thenThrow(NotFoundException.class);

        // Act and assert
        mockMvc.perform(get("/api/v1/exchange/conversion/" + UUID.randomUUID())
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errorCode").value(ApiErrorCode.RESOURCE_NOT_FOUND.getCode()));
    }
}
