package com.uni.unitech.dto;

import com.uni.unitech.enumerated.CurrencyTypes;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyRateDto {

    @NotBlank
    private CurrencyTypes currencyFrom;
    @NotBlank
    private CurrencyTypes currencyTo;

    private Double rate;
}