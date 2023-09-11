package com.uni.unitech.service.impl;

import com.uni.unitech.dto.CurrencyRateDto;
import com.uni.unitech.dto.mapper.CurrencyRateMapper;
import com.uni.unitech.exception.BadRequestException;
import com.uni.unitech.exception.util.ExceptionCodes;
import com.uni.unitech.repository.CurrencyRateRepository;
import com.uni.unitech.service.CurrencyRateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@EnableCaching
@RequiredArgsConstructor
@Slf4j
public class CurrencyRateServiceImpl implements CurrencyRateService {

    private final CurrencyRateMapper currencyRateMapper;
    private final CurrencyRateRepository currencyRateRepository;

    @Override
    @CacheEvict(value = "currencyRate", key = "#currencyRateDto.currencyFrom + '_' + #currencyRateDto.currencyTo")
    @CachePut(value = "currencyRate", key = "#currencyRateDto.currencyFrom + '_' + #currencyRateDto.currencyTo")
    public CurrencyRateDto create(CurrencyRateDto currencyRateDto) {
        var currencyRate = currencyRateMapper.toEntity(currencyRateDto);
        currencyRate.setCreationDateTime(Instant.now());
        currencyRateRepository.save(currencyRate);
        return currencyRateMapper.toDto(currencyRate);
    }

    @Override
    @Cacheable(value = "currencyRate", key = "#currencyRateDto.currencyFrom + '_' + #currencyRateDto.currencyTo")
    public Optional<CurrencyRateDto> getCurrencyRate(CurrencyRateDto currencyRateDto) {
        log.info("===DATA FROM DATABASE===");
        var currencyRateOpt = currencyRateRepository.findFirstByCurrencyFromAndCurrencyToOrderByCreationDateTimeDesc(
                currencyRateDto.getCurrencyFrom(),currencyRateDto.getCurrencyTo());
        if (currencyRateOpt.isEmpty())
            throw new BadRequestException(ExceptionCodes.CURRENCY_RATE_NOT_FOUND);

        var currencyRate = currencyRateOpt.get();
        return Optional.ofNullable(currencyRateMapper.toDto(currencyRate));
    }

}
