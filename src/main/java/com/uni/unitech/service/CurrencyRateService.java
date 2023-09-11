package com.uni.unitech.service;

import com.uni.unitech.dto.CurrencyRateDto;

import java.util.Optional;

public interface CurrencyRateService {

    CurrencyRateDto create(CurrencyRateDto currencyRateDto);
    Optional<CurrencyRateDto> getCurrencyRate(CurrencyRateDto currencyRateDto);

}
