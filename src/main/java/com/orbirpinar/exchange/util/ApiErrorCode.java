package com.orbirpinar.exchange.util;

public enum ApiErrorCode {

    VALIDATION_ERROR("01-exchange-validation"),
    MALFORMED_JSON("02-exchange-malformed-json"),
    TYPE_MISMATCH("03-exchange-type-mismatch"),
    EXCHANGE_RATE_CLIENT_ERROR("04-exchange-rate-api-error"),
    UNSUPPORTED_MEDIA_TYPE("05-exchange-unsupported-media-type"),
    RESOURCE_NOT_FOUND("06-exchange-resource-not-found");

    private final String code;

    ApiErrorCode(String code){
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
