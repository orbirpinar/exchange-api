package com.orbirpinar.exchange.config;

import com.orbirpinar.exchange.exception.ExchangeClientErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class ExchangeRateClientConfig {

    @Value("${exchange.client.baseUrl}")
    private String BASE_URL;

    private final ExchangeClientInterceptor interceptor;

    public ExchangeRateClientConfig(ExchangeClientInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));


        List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
        if (CollectionUtils.isEmpty(interceptors)) {
            interceptors = new ArrayList<>();
        }

        interceptors.add(interceptor);
        restTemplate.setInterceptors(interceptors);
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(BASE_URL));

        restTemplate.setErrorHandler(new ExchangeClientErrorHandler());


        return restTemplate;
    }




}
