package com.orbirpinar.exchange.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ExternalApiError extends ApiSubError{

    private String message;
    private String code;
}
