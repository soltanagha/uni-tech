package com.uni.unitech.service.impl;

import com.uni.unitech.dto.CurrencyRateDto;
import com.uni.unitech.dto.mapper.CurrencyRateMapper;
import com.uni.unitech.entity.CurrencyRate;
import com.uni.unitech.exception.BadRequestException;
import com.uni.unitech.repository.CurrencyRateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class CurrencyRateServiceImplTest {

    @Mock
    private CurrencyRateMapper currencyRateMapper;

    @Mock
    private CurrencyRateRepository currencyRateRepository;

    @InjectMocks
    private CurrencyRateServiceImpl currencyRateService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create() {
        CurrencyRateDto currencyRateDto = new CurrencyRateDto();
        CurrencyRate currencyRate = new CurrencyRate();

        when(currencyRateMapper.toEntity(currencyRateDto)).thenReturn(currencyRate);
        when(currencyRateMapper.toDto(currencyRate)).thenReturn(currencyRateDto);
        when(currencyRateRepository.save(currencyRate)).thenReturn(currencyRate);

        CurrencyRateDto result = currencyRateService.create(currencyRateDto);

        assertEquals(currencyRateDto, result);
    }

    @Test
    void getCurrencyRate_success() {
        CurrencyRateDto currencyRateDto = new CurrencyRateDto();
        CurrencyRate currencyRate = new CurrencyRate();

        when(currencyRateRepository.findFirstByCurrencyFromAndCurrencyToOrderByCreationDateTimeDesc(
                currencyRateDto.getCurrencyFrom(), currencyRateDto.getCurrencyTo())).thenReturn(Optional.of(currencyRate));
        when(currencyRateMapper.toDto(currencyRate)).thenReturn(currencyRateDto);

        Optional<CurrencyRateDto> result = currencyRateService.getCurrencyRate(currencyRateDto);

        assertTrue(result.isPresent());
        assertEquals(currencyRateDto, result.get());
    }

    @Test
    void getCurrencyRate_notFound() {
        CurrencyRateDto currencyRateDto = new CurrencyRateDto();

        when(currencyRateRepository.findFirstByCurrencyFromAndCurrencyToOrderByCreationDateTimeDesc(
                currencyRateDto.getCurrencyFrom(), currencyRateDto.getCurrencyTo())).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> currencyRateService.getCurrencyRate(currencyRateDto));
    }
}
