package com.orbirpinar.exchange.controller.Parameters;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.AssertTrue;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
public class TransactionDateParam {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate fromDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate toDate;

    @AssertTrue
    @JsonIgnore
    public boolean isValid() {
        if(fromDate != null) {
            if(toDate == null) {
                return false;
            }
        }
        if(toDate != null) {
            if(fromDate == null) {
                return false;
            }
        }

        return true;
    }
}
