package com.orbirpinar.exchange.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ExchangeClientException extends RuntimeException{
    private String message;
    private String  errorCode;
}
