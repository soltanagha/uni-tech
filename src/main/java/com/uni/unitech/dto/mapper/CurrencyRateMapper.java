package com.uni.unitech.dto.mapper;

import com.uni.unitech.dto.CurrencyRateDto;
import com.uni.unitech.entity.CurrencyRate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CurrencyRateMapper {

    @Mapping(target = "currencyFrom", source = "currencyFrom")
    @Mapping(target = "currencyTo", source = "currencyTo")
    @Mapping(target = "rate", source = "rate")
    CurrencyRateDto toDto(CurrencyRate currencyRate);

    @Mapping(target = "currencyFrom", source = "currencyFrom")
    @Mapping(target = "currencyTo", source = "currencyTo")
    @Mapping(target = "rate", source = "rate")
    CurrencyRate toEntity(CurrencyRateDto currencyRateDto);
}