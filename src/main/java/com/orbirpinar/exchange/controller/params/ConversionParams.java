package com.orbirpinar.exchange.controller.params;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.AssertTrue;
import java.time.LocalDate;

@Getter
@Setter
public class ConversionParams {

    private Integer pageSize = 5;

    private Integer pageNumber = 0;

    @Parameter(example = "2022-06-01",description = "format = 'yyyy-MM-dd'")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fromDate;

    @Parameter(example = "2022-06-01",description = "format = 'yyyy-MM-dd'")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate toDate;

    @AssertTrue(message = "fromDate field can't be later than toDate")
    private boolean IsValid() {
        if(fromDate != null && toDate != null) {
            return !fromDate.isAfter(toDate);
        }
        return true;
    }
}
