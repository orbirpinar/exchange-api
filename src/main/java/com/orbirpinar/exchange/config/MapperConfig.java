package com.orbirpinar.exchange.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Configuration;

import java.time.format.DateTimeFormatter;

@Configuration
public class MapperConfig {

        private final DateTimeFormatter  localDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        public ObjectMapper mapper() {
            JavaTimeModule javaTimeModule = new JavaTimeModule();
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(javaTimeModule);
            return mapper;
        }
}
