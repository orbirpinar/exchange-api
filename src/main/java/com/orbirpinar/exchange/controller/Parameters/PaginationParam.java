package com.orbirpinar.exchange.controller.Parameters;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaginationParam {

    @NotBlank
    private int pageSize = 5;

    @NotBlank
    private int pageNumber = 0;
}
