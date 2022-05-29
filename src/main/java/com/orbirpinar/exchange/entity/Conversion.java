package com.orbirpinar.exchange.entity;

import com.orbirpinar.exchange.dto.response.ExchangeConverterResponse;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "conversion")
@Data
public class Conversion {

    @GenericGenerator(name = "uuid_", strategy = "org.hibernate.id.UUIDGenerator")
    @GeneratedValue(generator = "uuid_")
    @Id
    private String transactionId;

    private BigDecimal exchangeRate;

    private LocalDate transactionDate;

    private String source;

    private String target;

    @Column(name = "target_amount")
    private BigDecimal targetAmount;

    public ExchangeConverterResponse toResponse() {
        return new ExchangeConverterResponse(transactionId,targetAmount);
    }

}
