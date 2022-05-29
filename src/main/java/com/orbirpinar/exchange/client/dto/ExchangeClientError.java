package com.orbirpinar.exchange.client.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExchangeClientError {
    private String code;
    private String message;
}
